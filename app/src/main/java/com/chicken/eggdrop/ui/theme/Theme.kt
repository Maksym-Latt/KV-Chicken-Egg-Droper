package com.chicken.eggdrop.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = LeafGreen,
    secondary = SunYellow,
    tertiary = ClayOrange,
    background = SkyBlue,
    surface = SkyBlueLight,
    onPrimary = ShadowBlack,
    onSecondary = ShadowBlack,
    onTertiary = ShadowBlack,
    onBackground = ShadowBlack,
    onSurface = ShadowBlack
)

@Composable
fun ChickenEggDropperTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
