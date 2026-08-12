package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameState
import com.example.model.HeroClass
import com.example.ui.theme.*

@Composable
fun HeaderStatBar(
    state: GameState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ParchmentSurface)
            .border(1.dp, ParchmentBorder)
            .padding(8.dp)
            .testTag("header_stat_bar")
    ) {
        // Name, Class, Level, Gold
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "${state.characterName} (Lvl ${state.level})",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${state.heroClass?.title ?: "Hero"}${state.subclass?.let { " - ${it.title}" } ?: ""}",
                    fontSize = 11.sp,
                    color = GoldSecondary
                )
            }

            // Gold Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(ObsidianBg, RoundedCornerShape(12.dp))
                    .border(1.dp, GoldPrimary, RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "🪙 ${state.gold} Gold",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Bars Row: HP, Resource, XP
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // HP Bar
            BarMeter(
                label = "HP",
                current = state.hp,
                max = state.maxHp,
                barColor = CrimsonHp,
                modifier = Modifier.weight(1f)
            )

            // Resource Bar
            val resourceColor = when (state.heroClass) {
                HeroClass.MAGE -> ManaBlue
                HeroClass.WARRIOR -> StaminaGreen
                HeroClass.ROGUE -> EnergyYellow
                HeroClass.CLERIC -> FaithGold
                null -> ManaBlue
            }

            BarMeter(
                label = state.heroClass?.resourceName ?: "Mana",
                current = state.resource,
                max = state.maxResource,
                barColor = resourceColor,
                modifier = Modifier.weight(1f)
            )

            // XP Bar
            BarMeter(
                label = "XP",
                current = state.xp,
                max = state.xpToNextLevel,
                barColor = GoldPrimary,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun BarMeter(
    label: String,
    current: Int,
    max: Int,
    barColor: Color,
    modifier: Modifier = Modifier
) {
    val progress = if (max > 0) (current.toFloat() / max).coerceIn(0f, 1f) else 0f

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 10.sp, color = TextParchment, fontWeight = FontWeight.Bold)
            Text(text = "$current/$max", fontSize = 10.sp, color = TextMuted)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(ObsidianBg)
                .border(0.5.dp, ParchmentBorder, RoundedCornerShape(4.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .background(barColor, RoundedCornerShape(4.dp))
            )
        }
    }
}
