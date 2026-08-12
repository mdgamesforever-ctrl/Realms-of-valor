package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GameData
import com.example.model.GameState
import com.example.model.Subclass
import com.example.ui.theme.*

@Composable
fun SkillsView(
    state: GameState,
    onChooseSubclassClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val heroClass = state.heroClass ?: return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ParchmentSurface)
            .border(1.dp, ParchmentBorder, RoundedCornerShape(8.dp))
            .padding(12.dp)
            .testTag("skills_view")
    ) {
        // Subclass Banner / Prompt
        if (state.subclass != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ObsidianBg, RoundedCornerShape(6.dp))
                    .border(1.dp, GoldPrimary, RoundedCornerShape(6.dp))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Subclass Specialization", fontSize = 11.sp, color = TextMuted)
                    Text(state.subclass.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
                }
                Text("Specialized Path", fontSize = 11.sp, color = GoldSecondary)
            }
        } else if (state.level >= 3) {
            Card(
                colors = CardDefaults.cardColors(containerColor = GoldPrimary.copy(alpha = 0.2f)),
                border = BorderStroke(1.5.dp, GoldAccent),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("★ SUBCLASS UNLOCKED!", fontWeight = FontWeight.Bold, color = GoldAccent, fontSize = 14.sp)
                        Text("Choose your permanent subclass specialization path now.", fontSize = 11.sp, color = TextParchment)
                    }
                    Button(
                        onClick = onChooseSubclassClick,
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = ObsidianBg),
                        modifier = Modifier.testTag("choose_subclass_btn")
                    ) {
                        Text("SELECT", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }
        } else {
            Text(
                text = "Reach Level 3 to unlock Subclass Specialization!",
                fontSize = 11.sp,
                color = TextMuted,
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Skills Tree List
        Text(
            text = "${heroClass.title.uppercase()} SKILL TREE",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = GoldPrimary,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        val classSkills = GameData.ALL_SKILLS.filter { skill ->
            skill.parentClass == heroClass &&
                    (skill.subclass == null || skill.subclass == state.subclass)
        }.sortedBy { it.levelRequired }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        ) {
            items(classSkills) { skill ->
                val isUnlocked = state.unlockedSkillIds.contains(skill.id)

                Card(
                    colors = CardDefaults.cardColors(containerColor = ObsidianBg),
                    border = BorderStroke(1.dp, if (isUnlocked) GoldPrimary else ParchmentBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = skill.name + if (skill.subclass != null) " [${skill.subclass.title}]" else "",
                                fontWeight = FontWeight.Bold,
                                color = if (isUnlocked) GoldAccent else TextMuted,
                                fontSize = 14.sp
                            )

                            if (isUnlocked) {
                                Text("UNLOCKED", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = StaminaGreen)
                            } else {
                                Text("Lvl ${skill.levelRequired} Required", fontSize = 10.sp, color = CrimsonHp)
                            }
                        }

                        Text(
                            text = skill.description,
                            fontSize = 11.sp,
                            color = TextParchment,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Cost: ${skill.resourceCost} ${heroClass.resourceName}",
                                fontSize = 10.sp,
                                color = ManaBlue
                            )
                            Text(
                                text = "Cooldown: ${skill.cooldown} turn(s)",
                                fontSize = 10.sp,
                                color = GoldSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}
