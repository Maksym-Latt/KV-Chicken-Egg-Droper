package com.chicken.eggdrop.ui.screens.menu

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.chicken.eggdrop.R
import com.chicken.eggdrop.model.MenuUiState
import com.chicken.eggdrop.ui.components.GameGradientIconButton
import com.chicken.eggdrop.ui.components.MenuTitle
import com.chicken.eggdrop.ui.components.GamePrimaryButton

@Composable
fun MenuScreen(
        state: MenuUiState,
        onPlay: () -> Unit,
        onOpenSkins: () -> Unit,
        onToggleMusic: () -> Unit,
        onToggleSound: () -> Unit,
        onStart: () -> Unit = {}
) {
    val showSettings = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { onStart() }

    val infinite = rememberInfiniteTransition(label = "menu_infinite")

    val heroFloatY by
            infinite.animateFloat(
                    initialValue = 0f,
                    targetValue = -14f,
                    animationSpec =
                            infiniteRepeatable(
                                    animation =
                                            tween(
                                                    durationMillis = 1400,
                                                    easing = FastOutSlowInEasing
                                            ),
                                    repeatMode = RepeatMode.Reverse
                            ),
                    label = "hero_float_y"
            )

    val startPulse by
            infinite.animateFloat(
                    initialValue = 1f,
                    targetValue = 1.05f,
                    animationSpec =
                            infiniteRepeatable(
                                    animation =
                                            tween(
                                                    durationMillis = 900,
                                                    easing = FastOutSlowInEasing
                                            ),
                                    repeatMode = RepeatMode.Reverse
                            ),
                    label = "start_pulse"
            )

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
                painter = painterResource(id = R.drawable.bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
        )

        Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderRow(onToggleSettings = { showSettings.value = true })
            Spacer(modifier = Modifier.weight(1f))
            MenuTitle(modifier = Modifier)

            Spacer(modifier = Modifier.weight(4f))

            Image(
                    painter = painterResource(id = state.selectedSkin.idleRes),
                    contentDescription = null,
                    modifier =
                            Modifier.fillMaxWidth(0.6f).graphicsLayer { translationY = heroFloatY },
                    contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.weight(2f))

            GameGradientIconButton(
                    iconRes = R.drawable.ic_shop,
                    modifier = Modifier,
                    onClick = onOpenSkins
            )

            Spacer(modifier = Modifier.weight(0.8f))

            Box(
                    modifier =
                            Modifier.graphicsLayer {
                                scaleX = startPulse
                                scaleY = startPulse
                            }
            ) {
                GamePrimaryButton(
                        text = "START",
                        modifier = Modifier.fillMaxWidth(0.6f),
                        onClick = onPlay
                )
            }

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
            modifier = Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.safeDrawing),
            contentAlignment = Alignment.Center
    ) {
        GameGradientIconButton(
                modifier = Modifier.align(Alignment.CenterStart),
                iconRes = R.drawable.ic_settings,
                onClick = onToggleSettings
        )
    }
}
