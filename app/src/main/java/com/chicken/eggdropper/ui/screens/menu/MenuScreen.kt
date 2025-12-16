package com.chicken.eggdropper.ui.screens.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.eggdropper.R
import com.chicken.eggdropper.model.MenuUiState
import com.chicken.eggdropper.ui.components.CircleIconButton
import com.chicken.eggdropper.ui.components.OutlineText
import com.chicken.eggdropper.ui.components.PrimaryButton
import com.chicken.eggdropper.ui.components.SecondaryButton
import com.chicken.eggdropper.ui.components.SettingToggle

@Composable
fun MenuScreen(
    state: MenuUiState,
    onPlay: () -> Unit,
    onOpenSkins: () -> Unit,
    onToggleMusic: () -> Unit,
    onToggleSound: () -> Unit
) {
    val showSettings = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF8FD8FF), Color(0xFFb6f2ff))
                )
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.6f
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderRow(state, onToggleSettings = { showSettings.value = true })
            Spacer(modifier = Modifier.height(24.dp))
            OutlineText(
                text = "CHICKEN\nEGG\nDROPPER",
                fontSize = 32.sp,
                brush = SolidColor(Color.White),
                borderColor = Color(0xFF3b3b3b)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Image(
                painter = painterResource(id = state.selectedSkin.idleRes),
                contentDescription = null,
                modifier = Modifier.size(220.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            SecondaryButton(
                text = "Skins",
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth(),
                onClick = onOpenSkins
            )
            Spacer(modifier = Modifier.height(12.dp))
            PrimaryButton(
                text = "PLAY",
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .fillMaxWidth(),
                onClick = onPlay
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        if (showSettings.value) {
            SettingsOverlay(
                isMusicEnabled = state.isMusicEnabled,
                isSoundEnabled = state.isSoundEnabled,
                onDismiss = { showSettings.value = false },
                onToggleMusic = onToggleMusic,
                onToggleSound = onToggleSound
            )
        }
    }
}

@Composable
private fun HeaderRow(state: MenuUiState, onToggleSettings: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircleIconButton(
            modifier = Modifier.align(Alignment.CenterStart),
            iconRes = R.drawable.ic_launcher_foreground,
            onClick = onToggleSettings
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFFffd36b))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            OutlineText(
                text = state.coins.toString(),
                brush = SolidColor(Color.White),
                borderColor = Color(0xFF2d2d2d)
            )
        }
    }
}

@Composable
private fun SettingsOverlay(
    isMusicEnabled: Boolean,
    isSoundEnabled: Boolean,
    onDismiss: () -> Unit,
    onToggleMusic: () -> Unit,
    onToggleSound: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f))
            .pointerInput(Unit) { detectTapGestures(onTap = { onDismiss() }) },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .background(Color(0xFFffe7b8), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier
                            .size(26.dp)
                            .clickable { onDismiss() },
                        tint = Color.Black
                    )
                }
                OutlineText(
                    text = "Settings",
                    fontSize = 22.sp,
                    brush = SolidColor(Color.Black),
                    borderColor = Color.White
                )
                SettingToggle(
                    label = "Music",
                    checked = isMusicEnabled,
                    iconRes = R.drawable.ic_launcher_foreground,
                    onToggle = onToggleMusic
                )
                SettingToggle(
                    label = "Sounds",
                    checked = isSoundEnabled,
                    iconRes = R.drawable.ic_launcher_foreground,
                    onToggle = onToggleSound
                )
            }
        }
    }
}
