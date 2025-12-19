package com.chicken.eggdrop.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameCoinsChip(
    coins: Int,
    iconRes: Int,
    addSign: Boolean = false,
) {
    val shape = RoundedCornerShape(32.dp)

    val textValue = if (addSign && coins > 0) {
        "+$coins"
    } else {
        coins.toString()
    }

    Box(
        modifier = Modifier
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    listOf(Color(0x1AFFFFFF), Color(0x1AFFFFFF))
                )
            )
            .border(2.dp, Color(0xFF2B2B2B), shape)
            .padding(horizontal = 14.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlineText(
                text = textValue,
                fontSize = 24.sp,
                stretchExpand = false,
                borderSize = 7f,
                brush = SolidColor(Color.White),
                borderColor = Color(0xFF000000)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(38.dp)
            )
        }
    }
}
