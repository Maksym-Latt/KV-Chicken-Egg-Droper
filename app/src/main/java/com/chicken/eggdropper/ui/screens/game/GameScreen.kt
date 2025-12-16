package com.chicken.eggdropper.ui.screens.game

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.chicken.eggdropper.R
import com.chicken.eggdropper.model.BasketType
import com.chicken.eggdropper.model.GameUiState
import com.chicken.eggdropper.ui.components.CoinsPill
import com.chicken.eggdropper.ui.components.GradientIconButton
import com.chicken.eggdropper.ui.components.OutlineText
import com.chicken.eggdropper.ui.components.PrimaryButton
import com.chicken.eggdropper.ui.components.SecondaryButton

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
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(uiState.isPaused, uiState.isGameOver, uiState.showIntro) {
                detectTapGestures { onDropEgg() }
            }
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
            val basketSize = if (uiState.basketType == BasketType.SMALL) 82.dp else 120.dp

            val chickenX = (maxWidth - chickenWidth) * uiState.chickenX
            val plateX = chickenX + (chickenWidth - plateWidth) / 2

            val basketX = (maxWidth - basketSize) * uiState.basketX
            val basketCenterY = maxHeight - basketSize / 2 - 30.dp

            val eggStartY = 160.dp
            val eggFallDistance = (basketCenterY - eggStartY - 24.dp).coerceAtLeast(0.dp)
            val eggProgress = uiState.eggState.y.coerceIn(0f, 1f)
            val eggY = eggStartY + eggFallDistance * eggProgress

            val safeTop = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()
            val hudHeightEstimate = 56.dp
            val chickenTopY = safeTop + hudHeightEstimate + 32.dp

            Image(
                painter = painterResource(id = R.drawable.item_plate),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = plateX, y = chickenTopY + 85.dp)
                    .size(plateWidth)
            )

            Image(
                painter = painterResource(
                    id = if (uiState.eggState.isFalling) uiState.selectedSkin.focusRes else uiState.selectedSkin.idleRes
                ),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = chickenX, y = chickenTopY)
                    .size(chickenWidth)
            )

            if (uiState.eggState.isFalling) {
                Image(
                    painter = painterResource(id = R.drawable.item_egg),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = chickenX + chickenWidth / 2 - 24.dp, y = eggY)
                        .size(48.dp)
                )
            }

            BasketRow(basketX = basketX, basketType = uiState.basketType)
        }
        TopHud(uiState = uiState, onTogglePause = onTogglePause)

        if (uiState.showIntro) {
            IntroOverlay(onStart = onStart)
        }

        if (uiState.isPaused && !uiState.isGameOver) {
            PauseOverlay(
                onResume = onTogglePause,
                onRestart = onRestart,
                onMenu = onOpenMenu,
                onToggleMusic = onToggleMusic,
                onToggleSound = onToggleSound
            )
        }

        if (uiState.isGameOver) {
            GameOverOverlay(
                score = uiState.score,
                best = uiState.bestScore,
                onRestart = onRestart,
                onMenu = onOpenMenu,
                skinRes = uiState.selectedSkin.focusRes
            )
        }
    }
}

@Composable
private fun TopHud(
    uiState: GameUiState,
    onTogglePause: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GradientIconButton(
            iconRes = R.drawable.ic_pause,
            onClick = onTogglePause
        )

        CoinsPill(
            coins = uiState.coins,
            eggIconRes = R.drawable.item_egg
        )
    }
}


@Composable
private fun BasketRow(basketX: Dp, basketType: BasketType) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.item_basket),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = basketX, y = (-30).dp)
                .size(if (basketType == BasketType.SMALL) 82.dp else 120.dp)
        )
    }
}
