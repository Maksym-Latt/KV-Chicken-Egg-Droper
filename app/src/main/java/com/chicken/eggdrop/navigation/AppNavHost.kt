package com.chicken.eggdrop.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chicken.eggdrop.ui.screens.game.GameScreen
import com.chicken.eggdrop.ui.screens.game.GameViewModel
import com.chicken.eggdrop.ui.screens.menu.MenuScreen
import com.chicken.eggdrop.ui.screens.menu.MenuViewModel
import com.chicken.eggdrop.ui.screens.skins.SkinsScreen
import com.chicken.eggdrop.ui.screens.skins.SkinsViewModel
import com.chicken.eggdrop.ui.screens.splash.SplashScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
            navController = navController,
            startDestination = NavigationDestination.Splash.route,
            modifier = Modifier.fillMaxSize()
    ) {
        composable(NavigationDestination.Splash.route) {
            SplashScreen(
                    onFinished = {
                        navController.navigate(NavigationDestination.Menu.route) {
                            popUpTo(NavigationDestination.Splash.route) { inclusive = true }
                        }
                    }
            )
        }

        composable(NavigationDestination.Menu.route) {
            val viewModel: MenuViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            MenuScreen(
                    state = uiState,
                    onPlay = { navController.navigate(NavigationDestination.Game.route) },
                    onOpenSkins = { navController.navigate(NavigationDestination.Skins.route) },
                    onToggleMusic = viewModel::toggleMusic,
                    onToggleSound = viewModel::toggleSound,
                    onStart = viewModel::onStart
            )
        }

        composable(NavigationDestination.Game.route) {
            val viewModel: GameViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            GameScreen(
                    uiState = uiState,
                    onDropEgg = viewModel::dropEgg,
                    onTogglePause = viewModel::togglePause,
                    onForcePause = viewModel::pauseGame,
                    onRestart = viewModel::restart,
                    onOpenMenu = {
                        navController.popBackStack(NavigationDestination.Menu.route, false)
                    },
                    onToggleMusic = viewModel::toggleMusic,
                    onToggleSound = viewModel::toggleSound,
                    onStart = viewModel::startGame
            )
        }

        composable(NavigationDestination.Skins.route) {
            val viewModel: SkinsViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            SkinsScreen(
                    state = uiState,
                    onBack = { navController.popBackStack() },
                    onSelect = viewModel::selectSkin
            )
        }
    }
}
