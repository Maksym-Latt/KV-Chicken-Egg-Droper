package com.chicken.eggdropper.ui.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.eggdropper.ui.components.OutlineText
import com.chicken.eggdropper.ui.components.PrimaryButton


@Composable
fun IntroOverlay(onStart: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f)),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFffe5b4),
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlineText(
                    text = "How to play",
                    fontSize = 24.sp,
                    brush = SolidColor(Color.Black),
                    borderColor = Color.White
                )
                Text(
                    text = "Tap the screen to drop eggs. Catch them with the basket to score coins!",
                    color = Color.Black
                )
                Text(
                    text = "Each catch can also unlock coins for new skins.",
                    color = Color.Black
                )
                PrimaryButton(text = "Start", modifier = Modifier.fillMaxWidth(), onClick = onStart)
            }
        }
    }
}