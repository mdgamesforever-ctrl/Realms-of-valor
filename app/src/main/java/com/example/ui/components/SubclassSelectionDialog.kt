package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.window.Dialog
import com.example.model.HeroClass
import com.example.model.Subclass
import com.example.ui.theme.*

@Composable
fun SubclassSelectionDialog(
    heroClass: HeroClass,
    onSelectSubclass: (Subclass) -> Unit
) {
    val subclasses = Subclass.entries.filter { it.parentClass == heroClass }

    Dialog(onDismissRequest = { /* Modal force choice */ }) {
        Card(
            colors = CardDefaults.cardColors(containerColor = ParchmentSurface),
            border = BorderStroke(2.dp, GoldPrimary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("subclass_selection_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "★ LEVEL 3 REACHED ★",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent
                )

                Text(
                    text = "Choose Your Permanent Subclass",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextParchment,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 6.dp)
                )

                Text(
                    text = "This choice locks in your specialized skill tree for the rest of your adventure.",
                    fontSize = 11.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                subclasses.forEach { subclass ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = ObsidianBg),
                        border = BorderStroke(1.5.dp, GoldPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onSelectSubclass(subclass) }
                            .testTag("select_subclass_${subclass.name.lowercase()}")
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = subclass.title,
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent,
                                fontSize = 16.sp
                            )
                            Text(
                                text = subclass.description,
                                fontSize = 11.sp,
                                color = TextParchment,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
