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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.eggdropper.R
import com.chicken.eggdropper.model.MenuUiState
import com.chicken.eggdropper.ui.components.GradientIconButton
import com.chicken.eggdropper.ui.components.MenuTitle
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
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderRow(onToggleSettings = { showSettings.value = true })
            Spacer(modifier = Modifier.weight(1f))
            MenuTitle(
                modifier = Modifier
            )

            Spacer(modifier = Modifier.weight(4f))
            Image(
                painter = painterResource(id = state.selectedSkin.idleRes),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(0.7f),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.weight(2f))

            GradientIconButton(
                iconRes = R.drawable.ic_shop,
                modifier = Modifier,
                onClick = onOpenSkins
            )

            Spacer(modifier = Modifier.weight(0.4f))

            PrimaryButton(
                text = "START",
                modifier = Modifier
                    .fillMaxWidth(0.6f),
                onClick = onPlay
            )

            Spacer(modifier = Modifier.weight(2f))
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
private fun HeaderRow(onToggleSettings: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        contentAlignment = Alignment.Center
    ) {
        GradientIconButton(
            modifier = Modifier.align(Alignment.CenterStart),
            iconRes = R.drawable.ic_settings,
            onClick = onToggleSettings
        )
    }
}
