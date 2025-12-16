package com.chicken.eggdropper.ui.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.eggdropper.ui.components.OutlineText
import com.chicken.eggdropper.ui.components.PrimaryButton

@Composable
fun IntroOverlay(
    onStart: () -> Unit
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
                .clip(shape)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFFD6A04A),
                            Color(0xFF9E6A12)
                        )
                    )
                )
                .border(3.dp, Color(0xFF2B2B2B), shape)
                .padding(horizontal = 22.dp, vertical = 22.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlineText(
                    text = "HOW TO PLAY",
                    fontSize = 44.sp,
                    borderSize = 12f,
                    brush = SolidColor(Color.White),
                    borderColor = Color(0xFF2B2B2B)
                )

                OutlineText(
                    text = "Tap the screen",
                    fontSize = 22.sp,
                    stretchExpand = false,
                    brush = SolidColor(Color.White),
                    borderColor = Color(0xFF2B2B2B)
                )

                OutlineText(
                    text = "to drop eggs",
                    fontSize = 22.sp,
                    stretchExpand = false,
                    brush = SolidColor(Color.White),
                    borderColor = Color(0xFF2B2B2B),
                    modifier = Modifier.offset(y = (-6).dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlineText(
                    text = "Catch them with the basket",
                    fontSize = 20.sp,
                    stretchExpand = false,
                    brush = SolidColor(Color.White),
                    borderColor = Color(0xFF2B2B2B)
                )

                OutlineText(
                    text = "to earn eggs & skins",
                    fontSize = 20.sp,
                    stretchExpand = false,
                    brush = SolidColor(Color.White),
                    borderColor = Color(0xFF2B2B2B),
                    modifier = Modifier.offset(y = (-6).dp)
                )


                PrimaryButton(
                    text = "START",
                    modifier = Modifier.fillMaxWidth(0.7f),
                    onClick = onStart
                )

            }
        }
    }
}
