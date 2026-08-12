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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GameData
import com.example.model.GameState
import com.example.ui.theme.*

@Composable
fun QuestsView(
    state: GameState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ParchmentSurface)
            .border(1.dp, ParchmentBorder, RoundedCornerShape(8.dp))
            .padding(12.dp)
            .testTag("quests_view")
    ) {
        Text(
            text = "ACTIVE QUEST LOG",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = GoldPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        val activeQuests = GameData.QUESTS.values.toList()

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            items(activeQuests) { quest ->
                val currentStep = state.questProgress[quest.id] ?: 1
                val isCompleted = currentStep >= quest.maxSteps
                val stepDesc = quest.stepDescriptions[currentStep] ?: "Quest completed!"

                Card(
                    colors = CardDefaults.cardColors(containerColor = ObsidianBg),
                    border = BorderStroke(1.dp, if (isCompleted) StaminaGreen else GoldPrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = quest.title,
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent,
                                fontSize = 14.sp
                            )
                            Text(
                                text = if (isCompleted) "COMPLETED" else "Step $currentStep/${quest.maxSteps}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isCompleted) StaminaGreen else GoldSecondary
                            )
                        }

                        Text(
                            text = quest.description,
                            fontSize = 11.sp,
                            color = TextParchment,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(ParchmentBorder.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                                .padding(6.dp)
                        ) {
                            Text(
                                text = "Current Objective: $stepDesc",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextParchment
                            )
                        }
                    }
                }
            }
        }
    }
}
