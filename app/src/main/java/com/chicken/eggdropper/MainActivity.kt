package com.chicken.eggdropper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.chicken.eggdropper.navigation.AppNavHost
import com.chicken.eggdropper.ui.theme.ChickenEggDropperTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var audioController: AudioController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ChickenEggDropperTheme {
                AppNavHost()
            }
        }

        hideSystemUI()
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.navigationBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
        audioController.onAppForeground()
    }

    override fun onDestroy() {
        super.onDestroy()
        audioController.onAppBackground()
        audioController.persistSettings()
    }

    override fun onPause() {
        super.onPause()
        audioController.onAppBackground()
        audioController.persistSettings()
    }
}
