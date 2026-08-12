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
import com.example.model.GameState
import com.example.model.Item
import com.example.model.ItemType
import com.example.ui.theme.*

@Composable
fun InventoryView(
    state: GameState,
    onEquipItem: (item: Item) -> Unit,
    onUseItem: (itemId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ParchmentSurface)
            .border(1.dp, ParchmentBorder, RoundedCornerShape(8.dp))
            .padding(12.dp)
            .testTag("inventory_view")
    ) {
        // Equipment Summary Header
        Text(
            text = "EQUIPPED GEAR",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = GoldPrimary,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ObsidianBg, RoundedCornerShape(6.dp))
                .border(1.dp, ParchmentBorder, RoundedCornerShape(6.dp))
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column {
                Text("Weapon:", fontSize = 11.sp, color = TextMuted)
                Text(
                    text = state.equippedWeapon?.name ?: "None Equipped",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (state.equippedWeapon != null) GoldAccent else TextMuted
                )
            }

            Divider(modifier = Modifier.height(28.dp).width(1.dp), color = ParchmentBorder)

            Column {
                Text("Armor:", fontSize = 11.sp, color = TextMuted)
                Text(
                    text = state.equippedArmor?.name ?: "None Equipped",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (state.equippedArmor != null) GoldAccent else TextMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Inventory List
        Text(
            text = "INVENTORY BAG (${state.inventory.sumOf { it.quantity }} items)",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = GoldPrimary,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        val itemsList = state.inventory.filter { it.quantity > 0 }

        if (itemsList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Bag is currently empty.", color = TextMuted)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                items(itemsList) { itemQty ->
                    val item = itemQty.item
                    val isEquipped = (state.equippedWeapon?.id == item.id || state.equippedArmor?.id == item.id)

                    Card(
                        colors = CardDefaults.cardColors(containerColor = ObsidianBg),
                        border = BorderStroke(1.dp, if (isEquipped) GoldPrimary else ParchmentBorder),
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
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${item.name} (x${itemQty.quantity})",
                                        fontWeight = FontWeight.Bold,
                                        color = GoldAccent,
                                        fontSize = 14.sp
                                    )
                                    if (isEquipped) {
                                        Text(
                                            text = " [EQUIPPED]",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = StaminaGreen
                                        )
                                    }
                                }
                                Text(item.description, fontSize = 11.sp, color = TextParchment)
                            }

                            when (item.type) {
                                ItemType.WEAPON, ItemType.ARMOR -> {
                                    Button(
                                        onClick = { onEquipItem(item) },
                                        enabled = !isEquipped,
                                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = ObsidianBg),
                                        modifier = Modifier.testTag("equip_${item.id}")
                                    ) {
                                        Text(if (isEquipped) "Equipped" else "Equip", fontSize = 11.sp)
                                    }
                                }
                                ItemType.POTION -> {
                                    Button(
                                        onClick = { onUseItem(item.id) },
                                        colors = ButtonDefaults.buttonColors(containerColor = StaminaGreen),
                                        modifier = Modifier.testTag("use_${item.id}")
                                    ) {
                                        Text("Use", fontSize = 11.sp)
                                    }
                                }
                                ItemType.QUEST -> {
                                    Text("Quest Item", fontSize = 10.sp, color = GoldSecondary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
