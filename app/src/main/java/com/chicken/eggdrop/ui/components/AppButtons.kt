package com.chicken.eggdrop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GamePrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    height: Dp = 64.dp,
    cornerRadius: Dp = 18.dp,
    outerBorderWidth: Dp = 2.dp,
    gradient: Brush = Brush.verticalGradient(
        listOf(Color(0xFF9BE45A), Color(0xFF4EA61D))
    ),
    outerBorderColor: Color = Color(0xFF2B2B2B),
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier
            .height(height)
            .clip(shape)
            .background(gradient)
            .border(outerBorderWidth, outerBorderColor, shape)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        OutlineText(
            text = text,
            fontSize = 32.sp,
            borderSize = 10f,
            borderColor = Color(0xFF2B2B2B),
            brush = SolidColor(Color.White),
            modifier = Modifier.padding(bottom = 3.dp)
        )
    }
}


@Composable
fun GameSecondaryButton(
    text: String,
    modifier: Modifier = Modifier,
    height: Dp = 64.dp,
    cornerRadius: Dp = 18.dp,
    outerBorderWidth: Dp = 2.dp,
    gradient: Brush = Brush.verticalGradient(
        listOf(Color(0xFFF1B65B), Color(0xFFA46A0F))
    ),
    outerBorderColor: Color = Color(0xFF2B2B2B),
    onClick: () -> Unit
) {
   GamePrimaryButton(
       text = text,
       modifier = modifier,
       height = height,
       cornerRadius = cornerRadius,
       outerBorderWidth = outerBorderWidth,
       gradient = gradient,
       outerBorderColor = outerBorderColor,
       onClick = onClick
   )
}

@Composable
fun GameGradientIconButton(
    modifier: Modifier = Modifier,
    width: Dp = 72.dp,
    height: Dp = 60.dp,
    cornerRadius: Dp = 20.dp,

    gradient: Brush = Brush.verticalGradient(
        listOf(Color(0xFFF6C26B), Color(0xFFC58A2E))
    ),

    borderColor: Color = Color(0xFF2D2D2D),
    borderWidth: Dp = 2.dp,

    iconRes: Int,
    iconSize: Dp = 40.dp,
    iconTint: Color = Color(0xfff6e4fd),

    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(width, height)
            .clip(RoundedCornerShape(cornerRadius))
            .background(gradient)
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(cornerRadius)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(iconSize),
            tint = iconTint
        )
    }
}