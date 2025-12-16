package com.chicken.eggdropper.ui.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.eggdropper.ui.components.OutlineText
import com.chicken.eggdropper.ui.components.PauseToggleRow
import com.chicken.eggdropper.ui.components.PrimaryButton
import com.chicken.eggdropper.ui.components.SecondaryButton

@Composable
fun PauseOverlay(
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onMenu: () -> Unit,
    onToggleMusic: () -> Unit,
    onToggleSound: () -> Unit
) {
    var musicChecked by rememberSaveable { mutableStateOf(true) }
    var soundsChecked by rememberSaveable { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFFD6A04A),
                            Color(0xFF9E6A12)
                        )
                    )
                )
                .border(
                    width = 3.dp,
                    color = Color(0xFF2B2B2B),
                    shape = RoundedCornerShape(28.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 22.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlineText(
                    text = "Pause",
                    fontSize = 48.sp,
                    brush = SolidColor(Color.White),
                )

                PrimaryButton(
                    text = "Continue",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onResume
                )

                SecondaryButton(
                    text = "Restart",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onRestart
                )

                SecondaryButton(
                    text = "MENU",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onMenu
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PauseToggleRow(
                        title = "Music",
                        checked = musicChecked,
                        onToggle = {
                            musicChecked = !musicChecked
                            onToggleMusic()
                        }
                    )

                    PauseToggleRow(
                        title = "Sounds",
                        checked = soundsChecked,
                        onToggle = {
                            soundsChecked = !soundsChecked
                            onToggleSound()
                        }
                    )
                }
            }
        }
    }
}
