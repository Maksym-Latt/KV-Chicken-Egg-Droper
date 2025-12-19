package com.chicken.eggdrop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun PauseToggleRow(
    title: String,
    checked: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlineText(
            text = title,
            fontSize = 28.sp,
            borderSize = 8f,
            stretchExpand = false,
            brush = SolidColor(Color.White),
            borderColor = Color(0xFF2B2B2B)
        )

        Spacer(modifier = Modifier.weight(1f))

        PauseSwitch(
            checked = checked,
            onCheckedChange = { onToggle() }
        )
    }
}

@Composable
private fun PauseSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val shape = RoundedCornerShape(18.dp)

    val track = if (checked) {
        Brush.verticalGradient(listOf(Color(0xFFE9B15A), Color(0xFFB17819)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFFC9C9C9), Color(0xFF8F8F8F)))
    }

    Box(
        modifier = Modifier
            .size(width = 72.dp, height = 40.dp)
            .clip(shape)
            .background(track, shape)
            .border(2.dp, Color(0xFF2B2B2B), shape)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onCheckedChange(!checked) }
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(29.dp)
                .align(if (checked) Alignment.CenterEnd else Alignment.CenterStart)
                .clip(RoundedCornerShape(15.dp))
                .background(Color(0xFFF7D6D6))
                .border(2.dp, Color(0xFFF7D6D6), RoundedCornerShape(12.dp))
        )
    }
}
