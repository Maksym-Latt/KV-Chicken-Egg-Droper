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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.eggdropper.R
import com.chicken.eggdropper.model.BasketType
import com.chicken.eggdropper.model.GameUiState
import com.chicken.eggdropper.ui.components.OutlineText
import com.chicken.eggdropper.ui.components.PrimaryButton
import com.chicken.eggdropper.ui.components.SecondaryButton

@Composable
fun GameScreen(
    uiState: GameUiState,
    onDropEgg: () -> Unit,
    onTogglePause: () -> Unit,
    onRestart: () -> Unit,
    onOpenMenu: () -> Unit,
    onToggleMusic: () -> Unit,
    onToggleSound: () -> Unit
) {
    BackHandler(enabled = true) {
        onTogglePause()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF8FD8FF), Color(0xFFb6f2ff))
                )
            )
            .pointerInput(uiState.isPaused, uiState.isGameOver) {
                detectTapGestures { onDropEgg() }
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.6f
        )

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val chickenX = maxWidth * uiState.chickenX - 60.dp
            val basketX = maxWidth * uiState.basketX - 48.dp
            val eggY = maxHeight * uiState.eggState.y

            Image(
                painter = painterResource(id = R.drawable.item_plate),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(x = chickenX, y = 90.dp)
                    .size(120.dp),
                alpha = 0.9f
            )

            Image(
                painter = painterResource(id = if (uiState.eggState.isFalling) uiState.selectedSkin.focusRes else uiState.selectedSkin.idleRes),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(x = chickenX, y = 20.dp)
                    .size(140.dp)
            )

            if (uiState.eggState.isFalling) {
                Image(
                    painter = painterResource(id = R.drawable.item_egg),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = chickenX + 46.dp, y = 160.dp + eggY)
                        .size(48.dp)
                )
            }

            BasketRow(basketX = basketX, basketType = uiState.basketType)
        }

        TopHud(uiState, onTogglePause = onTogglePause)

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
private fun TopHud(uiState: GameUiState, onTogglePause: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = Color(0xFFffce72),
            shadowElevation = 6.dp
        ) {
            Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onTogglePause) {
                    Icon(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = null)
                }
                OutlineText(text = "${uiState.score} eggs", fontSize = 16.sp, color = Color.White)
            }
        }

        Surface(
            shape = RoundedCornerShape(14.dp),
            color = Color(0xFFffce72),
            shadowElevation = 6.dp
        ) {
            Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlineText(text = "Best ${uiState.bestScore}", fontSize = 16.sp, color = Color.White)
            }
        }
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

@Composable
private fun PauseOverlay(
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onMenu: () -> Unit,
    onToggleMusic: () -> Unit,
    onToggleSound: () -> Unit
) {
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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlineText(text = "Pause", fontSize = 26.sp, color = Color.Black)
                PrimaryButton(text = "Continue", modifier = Modifier.fillMaxWidth(), onClick = onResume)
                SecondaryButton(text = "Restart", modifier = Modifier.fillMaxWidth(), onClick = onRestart)
                SecondaryButton(text = "MENU", modifier = Modifier.fillMaxWidth(), onClick = onMenu)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SecondaryButton(text = "Music", modifier = Modifier.weight(1f), onClick = onToggleMusic)
                    SecondaryButton(text = "Sounds", modifier = Modifier.weight(1f), onClick = onToggleSound)
                }
            }
        }
    }
}

@Composable
private fun GameOverOverlay(
    score: Int,
    best: Int,
    onRestart: () -> Unit,
    onMenu: () -> Unit,
    skinRes: Int
) {
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlineText(text = "MISSED!", fontSize = 28.sp, color = Color(0xFFe74c3c))
                Image(painter = painterResource(id = skinRes), contentDescription = null, modifier = Modifier.size(140.dp))
                OutlineText(text = "Score: $score", fontSize = 20.sp, color = Color.Black)
                OutlineText(text = "Best: $best", fontSize = 18.sp, color = Color.Black)
                PrimaryButton(text = "Try again", modifier = Modifier.fillMaxWidth(), onClick = onRestart)
                SecondaryButton(text = "Main menu", modifier = Modifier.fillMaxWidth(), onClick = onMenu)
            }
        }
    }
}
