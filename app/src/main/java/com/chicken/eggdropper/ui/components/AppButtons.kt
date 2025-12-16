package com.chicken.eggdropper.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.eggdropper.R

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF47C95E),
    outlineColor: Color = Color.Black,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(56.dp)
            .shadow(6.dp, RoundedCornerShape(16.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        color = backgroundColor,
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            OutlineText(
                text = text,
                borderColor = outlineColor,
                fontSize = 22.sp,
                brush = SolidColor(Color.White)
            )
        }
    }
}

@Composable
fun SecondaryButton(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFFFD16A),
    outlineColor: Color = Color.Black,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(48.dp)
            .shadow(4.dp, RoundedCornerShape(14.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        color = backgroundColor,
        shape = RoundedCornerShape(14.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            OutlineText(
                text = text,
                borderColor = outlineColor,
                fontSize = 18.sp,
                brush = SolidColor(Color.White)
            )
        }
    }
}

@Composable
fun CircleIconButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFFFCC4D),
    size: Dp = 46.dp,
    iconRes: Int = R.drawable.ic_launcher_foreground,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(size)
            .shadow(4.dp, CircleShape)
            .background(backgroundColor, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(painter = painterResource(id = iconRes), contentDescription = null, tint = Color.White)
    }
}
