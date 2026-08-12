package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GameData
import com.example.model.Location
import com.example.ui.theme.*

@Composable
fun SceneBackgroundFrame(
    location: Location,
    onTravelToLocation: (locationId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Look up drawable resource ID safely
    val imageResId = rememberLocationDrawable(location.bgImageResName, context)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.5.dp, GoldPrimary, RoundedCornerShape(8.dp))
            .testTag("scene_bg_frame")
    ) {
        // Background Image
        if (imageResId != 0) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = location.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Gradient fallback
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(GoldAccent.copy(alpha = 0.2f), ObsidianBg)
                        )
                    )
            )
        }

        // Dark Vignette Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, ObsidianBg.copy(alpha = 0.85f))
                    )
                )
        )

        // Location & Region Banner
        val regionName = GameData.REGIONS.find { it.id == location.regionId }?.name ?: "Unknown Region"

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)
        ) {
            Text(
                text = regionName.uppercase(),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = GoldSecondary,
                letterSpacing = 1.sp
            )
            Text(
                text = location.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = GoldAccent
            )
            Text(
                text = location.description,
                fontSize = 11.sp,
                color = TextParchment,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Navigation Chips to Connected Locations
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(location.connectingLocationIds) { connectedId ->
                    val connLoc = GameData.REGIONS.flatMap { it.locations }.find { it.id == connectedId }
                    connLoc?.let { loc ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(ParchmentSurface.copy(alpha = 0.9f))
                                .border(1.dp, GoldPrimary, RoundedCornerShape(12.dp))
                                .clickable { onTravelToLocation(loc.id) }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .testTag("travel_to_${loc.id}")
                        ) {
                            Text(
                                text = "➔ ${loc.name}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun rememberLocationDrawable(bgName: String, context: android.content.Context): Int {
    return androidx.compose.runtime.remember(bgName) {
        context.resources.getIdentifier(bgName, "drawable", context.packageName)
    }
}
