package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.model.Item
import com.example.ui.theme.*

@Composable
fun ShopView(
    state: GameState,
    onBuyItem: (item: Item) -> Unit,
    onSellItem: (item: Item) -> Unit,
    onCloseShop: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf("BUY") } // BUY or SELL

    val currentNpc = GameData.REGIONS.flatMap { it.locations }
        .flatMap { it.npcs }
        .find { it.id == state.currentDialogueNpcId }

    val shopItems = currentNpc?.shopInventory ?: listOf(
        GameData.ITEMS["health_potion"]!!,
        GameData.ITEMS["mana_potion"]!!,
        GameData.ITEMS["desert_scimitar"]!!,
        GameData.ITEMS["sandstone_plate"]!!
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ParchmentSurface)
            .border(1.5.dp, GoldPrimary, RoundedCornerShape(8.dp))
            .padding(12.dp)
            .testTag("shop_view")
    ) {
        // Shop Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "🏷 Merchant Shop (${currentNpc?.name ?: "Bazaar"})",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent
                )
                Text(
                    text = "Your Gold: 🪙 ${state.gold} Gold",
                    fontSize = 13.sp,
                    color = GoldSecondary
                )
            }

            Button(
                onClick = onCloseShop,
                colors = ButtonDefaults.buttonColors(containerColor = ObsidianBg, contentColor = TextMuted)
            ) {
                Text("Close")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Tab Toggle: BUY / SELL
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ObsidianBg, RoundedCornerShape(6.dp))
                .padding(4.dp)
        ) {
            Button(
                onClick = { selectedTab = "BUY" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == "BUY") GoldPrimary else Color.Transparent,
                    contentColor = if (selectedTab == "BUY") ObsidianBg else TextParchment
                ),
                modifier = Modifier
                    .weight(1f)
                    .testTag("shop_buy_tab")
            ) {
                Text("BUY GOODS", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }

            Button(
                onClick = { selectedTab = "SELL" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == "SELL") GoldPrimary else Color.Transparent,
                    contentColor = if (selectedTab == "SELL") ObsidianBg else TextParchment
                ),
                modifier = Modifier
                    .weight(1f)
                    .testTag("shop_sell_tab")
            ) {
                Text("SELL INVENTORY", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (selectedTab == "BUY") {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                items(shopItems) { item ->
                    val canAfford = state.gold >= item.value

                    Card(
                        colors = CardDefaults.cardColors(containerColor = ObsidianBg),
                        border = BorderStroke(1.dp, if (canAfford) GoldPrimary else ParchmentBorder),
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
                                Text(item.name, fontWeight = FontWeight.Bold, color = GoldAccent, fontSize = 14.sp)
                                Text(item.description, fontSize = 11.sp, color = TextParchment)
                                val statText = buildString {
                                    if (item.attackBonus > 0) append("+${item.attackBonus} Atk ")
                                    if (item.defenseBonus > 0) append("+${item.defenseBonus} Def ")
                                    if (item.magicBonus > 0) append("+${item.magicBonus} Mag ")
                                }
                                if (statText.isNotBlank()) {
                                    Text(statText, fontSize = 10.sp, color = GoldSecondary)
                                }
                            }

                            Button(
                                onClick = { onBuyItem(item) },
                                enabled = canAfford && !state.isActionProcessing,
                                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = ObsidianBg),
                                modifier = Modifier.testTag("buy_${item.id}")
                            ) {
                                Text("🪙 ${item.value} Gold", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        } else {
            // SELL INVENTORY TAB
            val sellableInventory = state.inventory.filter { it.quantity > 0 }

            if (sellableInventory.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Your inventory is empty.", color = TextMuted)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                ) {
                    items(sellableInventory) { itemQty ->
                        val item = itemQty.item
                        val sellPrice = kotlin.math.max(1, item.value / 2)

                        Card(
                            colors = CardDefaults.cardColors(containerColor = ObsidianBg),
                            border = BorderStroke(1.dp, ParchmentBorder),
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
                                    Text("${item.name} (x${itemQty.quantity})", fontWeight = FontWeight.Bold, color = TextParchment, fontSize = 14.sp)
                                    Text(item.description, fontSize = 11.sp, color = TextMuted)
                                }

                                Button(
                                    onClick = { onSellItem(item) },
                                    enabled = !state.isActionProcessing,
                                    colors = ButtonDefaults.buttonColors(containerColor = StaminaGreen),
                                    modifier = Modifier.testTag("sell_${item.id}")
                                ) {
                                    Text("Sell for 🪙 $sellPrice", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
