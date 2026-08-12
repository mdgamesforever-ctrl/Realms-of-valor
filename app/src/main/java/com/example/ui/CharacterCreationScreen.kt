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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CharacterBackground
import com.example.model.HeroClass
import com.example.ui.theme.*

@Composable
fun CharacterCreationScreen(
    onCharacterCreated: (name: String, background: CharacterBackground, heroClass: HeroClass) -> Unit
) {
    var name by remember { mutableStateOf("Valerius") }
    var selectedBackground by remember { mutableStateOf(CharacterBackground.NOBLE_EXILE) }
    var selectedClass by remember { mutableStateOf(HeroClass.WARRIOR) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title Banner
        Text(
            text = "REALMS OF VALOR",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = GoldAccent,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
        )

        Text(
            text = "Create Your Hero",
            fontSize = 18.sp,
            color = TextParchment,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Character Name Input
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Character Name", color = TextParchment) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GoldPrimary,
                unfocusedBorderColor = ParchmentBorder,
                focusedTextColor = TextParchment,
                unfocusedTextColor = TextParchment
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("char_name_input")
                .padding(bottom = 20.dp)
        )

        // Background Selection Section
        Text(
            text = "CHOOSE ORIGIN BACKGROUND",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = GoldPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        CharacterBackground.entries.forEach { bg ->
            val isSelected = (bg == selectedBackground)
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) ParchmentSurface else ObsidianBg
                ),
                border = BorderStroke(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) GoldPrimary else ParchmentBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { selectedBackground = bg }
                    .testTag("bg_choice_${bg.name.lowercase()}")
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = bg.title,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) GoldAccent else TextParchment,
                        fontSize = 16.sp
                    )
                    Text(
                        text = bg.description,
                        fontSize = 12.sp,
                        color = TextParchment,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = "Bonus: ${bg.bonusStatText}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = GoldSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Class Selection Section
        Text(
            text = "CHOOSE BASE HERO CLASS",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = GoldPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        HeroClass.entries.forEach { heroClass ->
            val isSelected = (heroClass == selectedClass)
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) ParchmentSurface else ObsidianBg
                ),
                border = BorderStroke(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) GoldPrimary else ParchmentBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { selectedClass = heroClass }
                    .testTag("class_choice_${heroClass.name.lowercase()}")
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = heroClass.title,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) GoldAccent else TextParchment,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "Resource: ${heroClass.resourceName}",
                            fontSize = 12.sp,
                            color = GoldSecondary
                        )
                    }

                    Text(
                        text = heroClass.description,
                        fontSize = 12.sp,
                        color = TextParchment,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Text(
                        text = "Passive: ${heroClass.passiveTrait}",
                        fontSize = 11.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    // Stat Grid Preview
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ParchmentBorder.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text("HP: ${heroClass.baseHp}", fontSize = 11.sp, color = CrimsonHp)
                        Text("${heroClass.resourceName}: ${heroClass.baseResource}", fontSize = 11.sp, color = ManaBlue)
                        Text("ATK: ${heroClass.baseAttack}", fontSize = 11.sp, color = TextParchment)
                        Text("DEF: ${heroClass.baseDefense}", fontSize = 11.sp, color = TextParchment)
                        Text("MAG: ${heroClass.baseMagic}", fontSize = 11.sp, color = TextParchment)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Start Journey Button
        Button(
            onClick = {
                onCharacterCreated(name, selectedBackground, selectedClass)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldPrimary,
                contentColor = ObsidianBg
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("start_game_button")
        ) {
            Text(
                text = "BEGIN YOUR QUEST",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
