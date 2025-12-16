package com.chicken.eggdropper.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MenuTitle(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        OutlineText(
            text = "CHICKEN",
            fontSize = 62.sp,
            borderSize = 12f,
            brush = Brush.verticalGradient(
                listOf(Color(0xFFE9AD53), Color(0xFFAD7219))
            ),
            borderColor = Color(0xFF2D2D2D),
            modifier = Modifier
        )

        OutlineText(
            text = "EGG",
            fontSize = 62.sp,
            borderSize = 12f,
            brush = Brush.verticalGradient(
                listOf(Color(0xFFB2833D), Color(0xFF82540E))
            ),
            borderColor = Color(0xFF2D2D2D),
            modifier = Modifier.offset(y = 42.dp)
        )

        OutlineText(
            text = "DROPPER",
            fontSize = 62.sp,
            borderSize = 12f,
            brush = Brush.verticalGradient(
                listOf(Color(0xFF84C946), Color(0xFF4A8115))
            ),
            borderColor = Color(0xFF2D2D2D),
            modifier = Modifier.offset(y = 84.dp)
        )
    }
}