package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.model.Skill
import com.example.ui.theme.*

@Composable
fun CombatView(
    state: GameState,
    onAttack: () -> Unit,
    onDefend: () -> Unit,
    onCastSkill: (skillId: String) -> Unit,
    onUseItem: (itemId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val combat = state.combatState
    val enemy = combat.enemy ?: return
    var showSkillsList by remember { mutableStateOf(false) }

    val combatLogScroll = rememberScrollState()

    // Auto scroll log to bottom
    LaunchedEffect(combat.combatLog.size) {
        if (combat.combatLog.isNotEmpty()) {
            combatLogScroll.animateScrollTo(combatLogScroll.maxValue)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ParchmentSurface)
            .border(1.5.dp, CrimsonHp, RoundedCornerShape(8.dp))
            .padding(12.dp)
            .testTag("combat_view")
    ) {
        // Enemy Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "⚔ ${enemy.name}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CrimsonHp
                )
                Text(
                    text = enemy.description,
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }

            Text(
                text = "Turn ${combat.turnNumber}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = GoldSecondary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Enemy HP Meter
        BarMeter(
            label = "ENEMY HP",
            current = combat.currentEnemyHp,
            max = enemy.maxHp,
            barColor = CrimsonHp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Combat Log Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(ObsidianBg)
                .border(1.dp, ParchmentBorder, RoundedCornerShape(6.dp))
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(combatLogScroll)
            ) {
                combat.combatLog.forEach { logEntry ->
                    Text(
                        text = logEntry,
                        fontSize = 12.sp,
                        color = TextParchment,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tactical Combat Actions
        if (!showSkillsList) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Attack
                Button(
                    onClick = onAttack,
                    enabled = !state.isActionProcessing,
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonHp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("combat_attack_btn")
                ) {
                    Text("⚔ ATTACK", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                // Defend
                Button(
                    onClick = onDefend,
                    enabled = !state.isActionProcessing,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = ObsidianBg),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("combat_defend_btn")
                ) {
                    Text("🛡 DEFEND", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Skills Panel Toggle
                Button(
                    onClick = { showSkillsList = true },
                    enabled = !state.isActionProcessing,
                    colors = ButtonDefaults.buttonColors(containerColor = ManaBlue),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("combat_skill_toggle_btn")
                ) {
                    Text("✨ SKILLS", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                // Health Potion Quick Use
                val hasPotion = state.inventory.any { it.item.id == "health_potion" && it.quantity > 0 }
                Button(
                    onClick = { onUseItem("health_potion") },
                    enabled = !state.isActionProcessing && hasPotion,
                    colors = ButtonDefaults.buttonColors(containerColor = StaminaGreen),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("combat_potion_btn")
                ) {
                    Text("🧪 POTION", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        } else {
            // SKILLS SELECTION PANEL
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ObsidianBg)
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("SELECT COMBAT SKILL", fontWeight = FontWeight.Bold, color = GoldAccent, fontSize = 14.sp)
                    TextButton(onClick = { showSkillsList = false }) {
                        Text("Back", color = TextMuted)
                    }
                }

                val availableSkills = GameData.ALL_SKILLS.filter { skill ->
                    state.unlockedSkillIds.contains(skill.id) && skill.resourceCost >= 0
                }

                if (availableSkills.isEmpty()) {
                    Text("No active combat skills available.", color = TextMuted, fontSize = 12.sp)
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        availableSkills.forEach { skill ->
                            val cooldown = state.skillCooldowns[skill.id] ?: 0
                            val canAfford = state.resource >= skill.resourceCost
                            val isUsable = (cooldown == 0 && canAfford && !state.isActionProcessing)

                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isUsable) ParchmentSurface else ObsidianBg
                                ),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = if (isUsable) GoldPrimary else ParchmentBorder
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(enabled = isUsable) {
                                        onCastSkill(skill.id)
                                        showSkillsList = false
                                    }
                                    .testTag("skill_btn_${skill.id}")
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(skill.name, fontWeight = FontWeight.Bold, color = if (isUsable) GoldAccent else TextMuted, fontSize = 13.sp)
                                        Text(skill.description, fontSize = 10.sp, color = TextParchment)
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("${skill.resourceCost} ${state.heroClass?.resourceName ?: "Resource"}", fontSize = 10.sp, color = ManaBlue)
                                        if (cooldown > 0) {
                                            Text("CD: $cooldown turn(s)", fontSize = 10.sp, color = CrimsonHp, fontWeight = FontWeight.Bold)
                                        } else if (!canAfford) {
                                            Text("Low Resource", fontSize = 10.sp, color = CrimsonHp)
                                        } else {
                                            Text("READY", fontSize = 10.sp, color = StaminaGreen, fontWeight = FontWeight.Bold)
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
}
