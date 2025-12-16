package com.chicken.eggdropper.ui.screens.skins

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.eggdropper.R
import com.chicken.eggdropper.model.ChickenSkin
import com.chicken.eggdropper.model.SkinsUiState
import com.chicken.eggdropper.ui.components.OutlineText
import com.chicken.eggdropper.ui.components.SecondaryButton

@Composable
fun SkinsScreen(
    state: SkinsUiState,
    onBack: () -> Unit,
    onSelect: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Color(0xFF8FD8FF), Color(0xFFb6f2ff)))
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.6f
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onBack() },
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(12.dp))
                OutlineText(
                    text = "Skins",
                    fontSize = 26.sp,
                    brush = SolidColor(Color.Black),
                    borderColor = Color.White
                )
                Spacer(modifier = Modifier.weight(1f))
                OutlineText(text = state.coins.toString(), brush = SolidColor(Color.White))
            }

            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.skins) { skin ->
                    SkinCard(
                        skin = skin,
                        isSelected = skin.id == state.selectedSkin.id,
                        isOwned = state.purchasedSkins.contains(skin.id),
                        coins = state.coins,
                        onSelect = { onSelect(skin.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SkinCard(
    skin: ChickenSkin,
    isSelected: Boolean,
    isOwned: Boolean,
    coins: Int,
    onSelect: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        color = Color.White.copy(alpha = 0.8f),
        shadowElevation = 6.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = skin.idleRes),
                contentDescription = null,
                modifier = Modifier.size(76.dp)
            )
            Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                OutlineText(
                    text = skin.name,
                    fontSize = 18.sp,
                    brush = SolidColor(Color.Black),
                    borderColor = Color.White
                )
                OutlineText(
                    text = skin.description,
                    fontSize = 14.sp,
                    brush = SolidColor(Color(0xFF3a3a3a)),
                    borderColor = Color.White,
                    borderSize = 4f,
                    stretchExpand = false,
                    textAlign = TextAlign.Start
                )
            }
            if (isSelected) {
                SecondaryButton(text = "Selected", modifier = Modifier.width(110.dp), onClick = {})
            } else {
                val affordable = skin.price == 0 || coins >= skin.price || isOwned
                SecondaryButton(
                    text = when {
                        isOwned || skin.price == 0 -> "Select"
                        else -> skin.price.toString()
                    },
                    modifier = Modifier.width(110.dp),
                    backgroundColor = if (affordable) Color(0xFFFFD16A) else Color(0xFFB0B0B0),
                    onClick = if (affordable) onSelect else {}
                )
            }
        }
    }
}
