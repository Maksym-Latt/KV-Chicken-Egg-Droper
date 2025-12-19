package com.chicken.eggdrop.ui.screens.game

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.fontscaling.MathUtils.lerp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.chicken.eggdrop.R
import com.chicken.eggdrop.model.BasketType
import com.chicken.eggdrop.model.GameUiState
import com.chicken.eggdrop.ui.components.GameCoinsChip
import com.chicken.eggdrop.ui.components.GameGradientIconButton
import com.chicken.eggdrop.ui.components.OutlineText

@Composable
fun GameScreen(
    uiState: GameUiState,
    onDropEgg: () -> Unit,
    onTogglePause: () -> Unit,
    onForcePause: () -> Unit,
    onRestart: () -> Unit,
    onOpenMenu: () -> Unit,
    onToggleMusic: () -> Unit,
    onToggleSound: () -> Unit,
    onStart: () -> Unit
) {
    BackHandler(enabled = true) { onTogglePause() }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                onForcePause()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .pointerInput(
                    uiState.isPaused,
                    uiState.isGameOver,
                    uiState.showIntro
                ) { detectTapGestures { onDropEgg() } }
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val chickenWidth = 140.dp
            val plateWidth = 120.dp
            val basketSize =
                if (uiState.basketType == BasketType.SMALL) 82.dp else 120.dp

            val chickenX = (maxWidth - chickenWidth) * uiState.chickenX
            val plateX = chickenX + (chickenWidth - plateWidth) / 2

            val basketX = (maxWidth - basketSize) * uiState.basketX
            val basketCenterY = maxHeight - basketSize / 2 - 30.dp

            val eggStartY = 160.dp
            val eggFallDistance =
                (basketCenterY - eggStartY - 24.dp).coerceAtLeast(0.dp)
            val eggProgress = uiState.eggState.y.coerceIn(0f, 1f)
            val eggY = eggStartY + eggFallDistance * eggProgress

            val safeTop =
                WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()
            val hudHeightEstimate = 56.dp
            val chickenTopY = safeTop + hudHeightEstimate + 32.dp

            Image(
                painter = painterResource(id = R.drawable.item_plate),
                contentDescription = null,
                modifier =
                    Modifier
                        .align(Alignment.TopStart)
                        .offset(x = plateX, y = chickenTopY + 85.dp)
                        .size(plateWidth)
            )

            Image(
                painter =
                    painterResource(
                        id =
                            if (uiState.eggState.isFalling)
                                uiState.selectedSkin.focusRes
                            else uiState.selectedSkin.idleRes
                    ),
                contentDescription = null,
                modifier =
                    Modifier
                        .align(Alignment.TopStart)
                        .offset(x = chickenX, y = chickenTopY)
                        .size(chickenWidth)
            )

            if (uiState.eggState.isFalling) {
                Image(
                    painter = painterResource(id = R.drawable.item_egg),
                    contentDescription = null,
                    modifier =
                        Modifier
                            .align(Alignment.TopStart)
                            .offset(
                                x =
                                    chickenX +
                                            chickenWidth / 2 -
                                            24.dp,
                                y = eggY
                            )
                            .size(48.dp)
                )
            }

            BasketRow(basketX = basketX, basketType = uiState.basketType)

            uiState.floatingTexts.forEach { ft ->
                val textBasketSize = if (ft.basketType == BasketType.SMALL) 82.dp else 120.dp
                val basketTopY = maxHeight - textBasketSize - 30.dp
                val baseY = basketTopY - 44.dp
                val t = ft.t.coerceIn(0f, 1f)
                val scale =
                    if (t < 0.18f) {
                        lerp(0.75f, 1.25f, t / 0.18f)
                    } else {
                        lerp(1.25f, 1.0f, ((t - 0.18f) / 0.82f).coerceIn(0f, 1f))
                    }
                val wobbleSign = if ((ft.text.hashCode() and 1) == 0) 1f else -1f
                val floatUp = (-76.dp) * t
                val rotation = (6f * wobbleSign) * (1f - t)

                OutlineText(
                    text = ft.text,
                    fontSize = 30.sp,
                    brush = Brush.verticalGradient(
                        listOf(Color(0xFFB97D23), Color(0xFFFF9E00))),
                    borderColor = Color.Black,
                    borderSize = 3f,
                    modifier =
                        Modifier
                            .align(Alignment.TopStart)
                            .offset(x =basketX - 80.dp, y = baseY + floatUp)
                            .graphicsLayer {
                                alpha = ft.alpha
                                scaleX = scale
                                scaleY = scale
                                rotationZ = rotation
                            }
                )
            }
        }
        TopHud(uiState = uiState, onTogglePause = onTogglePause)

        if (uiState.showIntro) {
            IntroOverlay(onStart = onStart)
        }

        if (uiState.isPaused && !uiState.isGameOver) {
            PauseOverlay(
                isMusicEnabled = uiState.isMusicEnabled,
                isSoundEnabled = uiState.isSoundEnabled,
                onResume = onTogglePause,
                onRestart = onRestart,
                onMenu = onOpenMenu,
                onToggleMusic = onToggleMusic,
                onToggleSound = onToggleSound
            )
        }

        if (uiState.isGameOver) {
            GameOverOverlay(
                coinsEarned = uiState.coinsEarned,
                best = uiState.bestScore,
                onRestart = onRestart,
                onMenu = onOpenMenu,
                skinRes = uiState.selectedSkin.focusRes
            )
        }
    }
}

@Composable
private fun TopHud(uiState: GameUiState, onTogglePause: () -> Unit) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GameGradientIconButton(iconRes = R.drawable.ic_pause, onClick = onTogglePause)

        GameCoinsChip(coins = uiState.coins, iconRes = R.drawable.item_egg)
    }
}

@Composable
private fun BasketRow(basketX: Dp, basketType: BasketType) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.item_basket),
            contentDescription = null,
            modifier =
                Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = basketX, y = (-30).dp)
                    .size(if (basketType == BasketType.SMALL) 82.dp else 120.dp)
        )
    }
}
