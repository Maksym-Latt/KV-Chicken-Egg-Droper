package com.chicken.eggdropper.navigation

sealed class NavigationDestination(val route: String) {
    data object Splash : NavigationDestination("splash")
    data object Menu : NavigationDestination("menu")
    data object Game : NavigationDestination("game")
    data object Skins : NavigationDestination("skins")
}
