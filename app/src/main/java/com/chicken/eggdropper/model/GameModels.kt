package com.chicken.eggdropper.model

enum class BasketType(val points: Int, val radius: Float) {
    STANDARD(points = 1, radius = 0.12f),
    SMALL(points = 2, radius = 0.08f)
}

data class EggState(
    val isFalling: Boolean = false,
    val y: Float = 0f
)

data class GameUiState(
    val chickenX: Float = 0f,
    val chickenDirection: Int = 1,
    val basketX: Float = 0f,
    val basketDirection: Int = 1,
    val eggState: EggState = EggState(),
    val basketType: BasketType = BasketType.STANDARD,
    val score: Int = 0,
    val bestScore: Int = 0,
    val coins: Int = 0,
    val coinsEarned: Int = 0,
    val isPaused: Boolean = false,
    val isGameOver: Boolean = false,
    val isDropping: Boolean = false,
    val selectedSkin: ChickenSkin = DefaultSkins.first(),
    val message: String = "",
    val showIntro: Boolean = true
)

data class MenuUiState(
    val coins: Int = 0,
    val selectedSkin: ChickenSkin = DefaultSkins.first(),
    val isMusicEnabled: Boolean = true,
    val isSoundEnabled: Boolean = true
)

data class SkinsUiState(
    val skins: List<ChickenSkin> = DefaultSkins,
    val selectedSkin: ChickenSkin = DefaultSkins.first(),
    val coins: Int = 0,
    val purchasedSkins: Set<String> = DefaultSkins.filter { it.isDefault }.map { it.id }.toSet()
)
