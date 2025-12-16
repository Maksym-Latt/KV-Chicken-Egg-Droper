package com.chicken.eggdropper.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.Text
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.chicken.eggdropper.R

@Composable
fun OutlineText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    outlineColor: Color = Color.Black,
    fontSize: TextUnit = 20.sp,
    textAlign: TextAlign = TextAlign.Center,
    maxLines: Int = Int.MAX_VALUE,
    fontWeight: FontWeight = FontWeight.Bold
) {
    val fontFamily = FontFamily(Font(R.font.poppins_bold))
    val style = TextStyle(
        color = color,
        fontSize = fontSize,
        textAlign = textAlign,
        fontFamily = fontFamily,
        fontWeight = fontWeight,
        shadow = Shadow(outlineColor, offset = Offset(2f, 2f), blurRadius = 4f)
    )
    Box(modifier = modifier) {
        Text(
            text = text,
            style = style,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis
        )
    }
}
