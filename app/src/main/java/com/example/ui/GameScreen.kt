package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GameData
import com.example.model.GameState
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.GameViewModel

@Composable
fun GameScreen(
    viewModel: GameViewModel
) {
    val state by viewModel.gameState.collectAsState()

    if (state.characterName.isBlank()) {
        CharacterCreationScreen(
            onCharacterCreated = { name, bg, heroClass ->
                viewModel.createCharacter(name, bg, heroClass)
            }
        )
        return
    }

    if (state.gameEnded) {
        VictoryScreen(
            state = state,
            onRestartGame = { viewModel.restartGame() }
        )
        return
    }

    if (state.pendingSubclassChoice && state.subclass == null && state.heroClass != null) {
        SubclassSelectionDialog(
            heroClass = state.heroClass!!,
            onSelectSubclass = { subclass ->
                viewModel.chooseSubclass(subclass)
            }
        )
    }

    val currentLocation = GameData.REGIONS.flatMap { it.locations }
        .find { it.id == state.currentLocationId }
        ?: GameData.REGIONS.first().locations.first()

    val currentNpc = currentLocation.npcs.find { it.id == state.currentDialogueNpcId }
        ?: currentLocation.npcs.firstOrNull()

    val currentDialogueNode = currentNpc?.let { npc ->
        state.currentDialogueNodeId?.let { nodeId -> npc.dialogueNodes[nodeId] }
            ?: npc.dialogueNodes[npc.initialNodeId]
    }

    val scrollState = rememberScrollState()

    // Auto scroll narration log
    LaunchedEffect(state.narrativeLog.size) {
        if (state.narrativeLog.isNotEmpty()) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Persistent Top Header Stat Bar
        HeaderStatBar(state = state)

        // Top Navigation Bar (Main, Inventory, Skills, Quests, Shop)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ParchmentSurface)
                .border(0.5.dp, ParchmentBorder)
                .padding(horizontal = 4.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            val tabs = listOf(
                "MAIN" to "📜 Main",
                "INVENTORY" to "🎒 Bag",
                "SKILLS" to "✨ Skills",
                "QUESTS" to "🧭 Quests"
            )

            tabs.forEach { (tabKey, label) ->
                val isSelected = (state.activeTab == tabKey)
                TextButton(
                    onClick = { viewModel.setTab(tabKey) },
                    modifier = Modifier.testTag("nav_tab_${tabKey.lowercase()}")
                ) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) GoldAccent else TextMuted
                    )
                }
            }

            if (state.activeTab == "SHOP" || currentNpc?.shopInventory?.isNotEmpty() == true) {
                TextButton(
                    onClick = { viewModel.setTab("SHOP") },
                    modifier = Modifier.testTag("nav_tab_shop")
                ) {
                    Text(
                        text = "🏷 Shop",
                        fontSize = 11.sp,
                        fontWeight = if (state.activeTab == "SHOP") FontWeight.Bold else FontWeight.Normal,
                        color = if (state.activeTab == "SHOP") GoldAccent else TextMuted
                    )
                }
            }
        }

        // Active Tab Content Area
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            when (state.activeTab) {
                "INVENTORY" -> {
                    InventoryView(
                        state = state,
                        onEquipItem = { viewModel.equipItem(it) },
                        onUseItem = { viewModel.useItem(it) }
                    )
                }
                "SKILLS" -> {
                    SkillsView(
                        state = state,
                        onChooseSubclassClick = { /* Handled in state dialog */ }
                    )
                }
                "QUESTS" -> {
                    QuestsView(state = state)
                }
                "SHOP" -> {
                    ShopView(
                        state = state,
                        onBuyItem = { viewModel.buyItem(it) },
                        onSellItem = { viewModel.sellItem(it) },
                        onCloseShop = { viewModel.setTab("MAIN") }
                    )
                }
                else -> {
                    // MAIN STORY / COMBAT VIEW
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Background Location Illustration Frame
                        SceneBackgroundFrame(
                            location = currentLocation,
                            onTravelToLocation = { viewModel.travelToLocation(it) }
                        )

                        // COMBAT VIEW OR DIALOGUE STORY
                        if (state.combatState.inCombat) {
                            CombatView(
                                state = state,
                                onAttack = { viewModel.performCombatAttack() },
                                onDefend = { viewModel.performCombatDefend() },
                                onCastSkill = { viewModel.castSkillInCombat(it) },
                                onUseItem = { viewModel.useItem(it) }
                            )
                        } else {
                            // MAIN STORY NARRATION & DIALOGUE PANEL
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(ParchmentSurface)
                                    .border(1.dp, ParchmentBorder, RoundedCornerShape(8.dp))
                                    .padding(12.dp)
                            ) {
                                // Story / Narrative History Box
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(ObsidianBg, RoundedCornerShape(6.dp))
                                        .border(1.dp, ParchmentBorder, RoundedCornerShape(6.dp))
                                        .padding(10.dp)
                                ) {
                                    state.narrativeLog.takeLast(6).forEach { logLine ->
                                        Text(
                                            text = logLine,
                                            fontSize = 12.sp,
                                            color = TextParchment,
                                            modifier = Modifier.padding(bottom = 4.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // NPC Dialogue Box (if talking to NPC)
                                if (currentNpc != null && currentDialogueNode != null) {
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = ObsidianBg),
                                        border = BorderStroke(1.dp, GoldPrimary),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = "🗣 ${currentNpc.name} (${currentNpc.title})",
                                                    fontWeight = FontWeight.Bold,
                                                    color = GoldAccent,
                                                    fontSize = 13.sp
                                                )
                                            }

                                            Text(
                                                text = "\"${currentDialogueNode.text}\"",
                                                fontSize = 12.sp,
                                                color = TextParchment,
                                                modifier = Modifier.padding(vertical = 6.dp)
                                            )

                                            // Dialogue Choice Buttons
                                            Column(
                                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                                modifier = Modifier.padding(top = 4.dp)
                                            ) {
                                                currentDialogueNode.choices.forEach { choice ->
                                                    Button(
                                                        onClick = { viewModel.selectDialogueChoice(choice) },
                                                        enabled = !state.isActionProcessing,
                                                        colors = ButtonDefaults.buttonColors(
                                                            containerColor = ParchmentSurface,
                                                            contentColor = GoldAccent
                                                        ),
                                                        border = BorderStroke(1.dp, GoldPrimary),
                                                        shape = RoundedCornerShape(6.dp),
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .testTag("dialogue_choice_${choice.id}")
                                                    ) {
                                                        Text(
                                                            text = choice.text,
                                                            fontSize = 12.sp,
                                                            fontWeight = FontWeight.Medium,
                                                            modifier = Modifier.fillMaxWidth()
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Location Actions & NPCs list
                                if (currentLocation.npcs.size > 1) {
                                    Text("PEOPLE IN AREA:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldSecondary)
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        modifier = Modifier.padding(top = 4.dp)
                                    ) {
                                        currentLocation.npcs.forEach { npc ->
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(ObsidianBg)
                                                    .border(1.dp, GoldPrimary, RoundedCornerShape(6.dp))
                                                    .clickable { viewModel.startDialogueWithNpc(npc) }
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                                    .testTag("talk_to_${npc.id}")
                                            ) {
                                                Text("Talk to ${npc.name}", fontSize = 11.sp, color = TextParchment)
                                            }
                                        }
                                    }
                                }

                                // Location Combat Trigger Button
                                if (currentLocation.availableEnemyIds.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Button(
                                        onClick = {
                                            viewModel.startCombat(currentLocation.availableEnemyIds.first())
                                        },
                                        enabled = !state.isActionProcessing,
                                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonHp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("explore_combat_btn")
                                    ) {
                                        Text("⚔ HUNT IN AREA", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
