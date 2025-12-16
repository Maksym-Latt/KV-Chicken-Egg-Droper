package com.chicken.eggdropper.ui.screens.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.eggdropper.R
import com.chicken.eggdropper.ui.components.CoinsPill
import com.chicken.eggdropper.ui.components.OutlineText
import com.chicken.eggdropper.ui.components.PrimaryButton
import com.chicken.eggdropper.ui.components.SecondaryButton

@Composable
fun GameOverOverlay(
    coinsEarned: Int,
    best: Int,
    onRestart: () -> Unit,
    onMenu: () -> Unit,
    skinRes: Int
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        val shape = RoundedCornerShape(28.dp)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(0.35f))

                OutlineText(
                    text = "MISSED!",
                    fontSize = 68.sp,
                    borderSize = 12f,
                    brush = SolidColor(Color(0xFFF4D1E3)),
                    borderColor = Color(0xFF2B2B2B)
                )

                Spacer(modifier = Modifier.weight(0.25f))

                Image(
                    painter = painterResource(id = skinRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(0.95f),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.weight(0.2f))

                CoinsPill(
                    coins = coinsEarned,
                    eggIconRes = R.drawable.item_egg,
                    addSign = true
                )

                Spacer(modifier = Modifier.weight(0.22f))

                PrimaryButton(
                    text = "Try again",
                    modifier = Modifier.fillMaxWidth(0.7f),
                    onClick = onRestart
                )

                Spacer(modifier = Modifier.height(10.dp))

                SecondaryButton(
                    text = "Main menu",
                    modifier = Modifier.fillMaxWidth(0.76f),
                    onClick = onMenu
                )

                Spacer(modifier = Modifier.weight(0.4f))
            }
        }
    }
}
