package com.chicken.eggdropper.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.eggdropper.R

@Composable
fun SettingToggle(
    label: String,
    checked: Boolean,
    iconRes: Int,
    modifier: Modifier = Modifier,
    onToggle: () -> Unit
) {
    Row(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .background(Color(0xFFffd36b), RoundedCornerShape(16.dp))
            .clickable { onToggle() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(painter = painterResource(id = iconRes), contentDescription = null, tint = Color.White)
        OutlineText(
            text = label,
            fontSize = 16.sp,
            brush = SolidColor(Color.White),
            borderColor = Color.Black
        )
        OutlineText(
            text = if (checked) "ON" else "OFF",
            fontSize = 16.sp,
            brush = SolidColor(Color.White),
            borderColor = Color.Black
        )
    }
}
