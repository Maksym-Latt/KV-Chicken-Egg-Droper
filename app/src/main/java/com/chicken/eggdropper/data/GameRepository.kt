package com.chicken.eggdropper.data

import com.chicken.eggdropper.model.ChickenSkin
import com.chicken.eggdropper.model.DefaultSkins
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor() {
    private val _selectedSkin = MutableStateFlow(DefaultSkins.first())
    val selectedSkin: StateFlow<ChickenSkin> = _selectedSkin

    private val _coins = MutableStateFlow(4500)
    val coins: StateFlow<Int> = _coins

    private val _bestScore = MutableStateFlow(0)
    val bestScore: StateFlow<Int> = _bestScore

    fun updateBestScore(score: Int) {
        _bestScore.value = maxOf(score, _bestScore.value)
    }

    fun selectSkin(skin: ChickenSkin) {
        _selectedSkin.value = skin
    }

    fun spendCoins(amount: Int): Boolean {
        if (_coins.value < amount) return false
        _coins.update { it - amount }
        return true
    }

    fun addCoins(amount: Int) {
        _coins.update { it + amount }
    }
}
