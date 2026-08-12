package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameState
import com.example.ui.theme.*

@Composable
fun VictoryScreen(
    state: GameState,
    onRestartGame: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(20.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🏆 REALM SAVED! 🏆",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = GoldAccent,
            textAlign = TextAlign.Center
        )

        Text(
            text = "VICTORY OVER SHADOW LORD MALAKOR",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = GoldSecondary,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = ParchmentSurface),
            border = BorderStroke(2.dp, GoldPrimary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "The shadow barrier shatters into glittering stardust! Shadow Lord Malakor falls to his knees as radiant light floods the Obsidian Catacombs. Across the Whispering Woods and Sunfire Desert, bells chime celebrating the dawn of peace!",
                    fontSize = 13.sp,
                    color = TextParchment,
                    textAlign = TextAlign.Justify,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "HALL OF FAME HERO STATS",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ObsidianBg, RoundedCornerShape(6.dp))
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Hero Name: ${state.characterName}", color = TextParchment, fontSize = 12.sp)
                    Text("Class: ${state.heroClass?.title} (${state.subclass?.title ?: "Base"})", color = TextParchment, fontSize = 12.sp)
                    Text("Level Reached: ${state.level}", color = GoldAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Final Gold: 🪙 ${state.gold}", color = GoldSecondary, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onRestartGame,
            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = ObsidianBg),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("play_again_btn")
        ) {
            Text("BEGIN NEW ADVENTURE", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
