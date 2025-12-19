package com.chicken.eggdrop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.chicken.eggdrop.navigation.AppNavHost
import com.chicken.eggdrop.ui.theme.ChickenEggDropperTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var soundEngine: SoundEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent { ChickenEggDropperTheme { AppNavHost() } }

        hideSystemUI()
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = android.graphics.Color.TRANSPARENT
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.navigationBars())
        controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        controller.isAppearanceLightStatusBars = true
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
        soundEngine.onAppForeground()
    }

    override fun onDestroy() {
        super.onDestroy()
        soundEngine.onAppBackground()
        soundEngine.persistSettings()
    }

    override fun onPause() {
        super.onPause()
        soundEngine.onAppBackground()
        soundEngine.persistSettings()
    }
}
