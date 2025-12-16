package com.chicken.eggdropper.ui.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chicken.eggdropper.AudioController
import com.chicken.eggdropper.data.GameRepository
import com.chicken.eggdropper.model.BasketType
import com.chicken.eggdropper.model.EggState
import com.chicken.eggdropper.model.GameUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.abs
import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class GameViewModel @Inject constructor(
    private val repository: GameRepository,
    private val audioController: AudioController
) : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState

    init {
        audioController.playGameMusic()
        listenToSharedState()
        startLoops()
    }

    private fun listenToSharedState() {
        viewModelScope.launch {
            repository.selectedSkin.collect { skin ->
                _uiState.update { it.copy(selectedSkin = skin) }
            }
        }
        viewModelScope.launch {
            repository.coins.collect { coins ->
                _uiState.update { it.copy(coins = coins) }
            }
        }
        viewModelScope.launch {
            repository.bestScore.collect { best ->
                _uiState.update { it.copy(bestScore = best) }
            }
        }
    }

    private fun startLoops() {
        viewModelScope.launch {
            while (true) {
                delay(16)
                tick()
            }
        }
    }

    private fun tick() {
        val state = _uiState.value
        if (state.isPaused || state.isGameOver) return
        moveChicken(state)
        moveBasket(state)
        if (state.eggState.isFalling) {
            advanceEgg(state)
        }
    }

    private fun moveChicken(state: GameUiState) {
        val speed = 0.004f
        var newX = state.chickenX + speed * state.chickenDirection
        var direction = state.chickenDirection
        if (newX < 0.1f) {
            newX = 0.1f
            direction = 1
        } else if (newX > 0.9f) {
            newX = 0.9f
            direction = -1
        }
        _uiState.update { it.copy(chickenX = newX, chickenDirection = direction) }
    }

    private fun moveBasket(state: GameUiState) {
        val speed = if (state.basketType == BasketType.SMALL) 0.006f else 0.004f
        var newX = state.basketX + speed * state.basketDirection
        var direction = state.basketDirection
        if (newX < 0.15f) {
            newX = 0.15f
            direction = 1
        } else if (newX > 0.85f) {
            newX = 0.85f
            direction = -1
        }
        _uiState.update { it.copy(basketX = newX, basketDirection = direction) }
    }

    private fun advanceEgg(state: GameUiState) {
        val fallSpeed = 0.025f
        val newY = state.eggState.y + fallSpeed
        if (newY >= 1f) {
            resolveLanding(state)
        } else {
            _uiState.update { it.copy(eggState = EggState(isFalling = true, y = newY)) }
        }
    }

    private fun resolveLanding(state: GameUiState) {
        val hit = abs(state.chickenX - state.basketX) <= state.basketType.radius
        if (hit) {
            audioController.playSuccess()
            val gainedCoins = if (state.basketType == BasketType.SMALL) 120 else 60
            repository.addCoins(gainedCoins)
            val newScore = state.score + state.basketType.points
            repository.updateBestScore(newScore)
            _uiState.update {
                it.copy(
                    eggState = EggState(),
                    score = newScore,
                    basketType = nextBasketType(),
                    message = "+${gainedCoins}"
                )
            }
        } else {
            audioController.playMiss()
            audioController.playLose()
            _uiState.update {
                it.copy(
                    eggState = EggState(),
                    isGameOver = true,
                    message = "MISSED!"
                )
            }
        }
    }

    fun dropEgg() {
        val state = _uiState.value
        if (state.eggState.isFalling || state.isPaused || state.isGameOver) return
        audioController.playDrop()
        _uiState.update { it.copy(eggState = EggState(isFalling = true, y = 0f)) }
    }

    fun togglePause() {
        _uiState.update { it.copy(isPaused = !it.isPaused) }
    }

    fun restart() {
        _uiState.update {
            it.copy(
                chickenX = 0.5f,
                chickenDirection = 1,
                basketX = 0.3f,
                basketDirection = 1,
                eggState = EggState(),
                basketType = BasketType.STANDARD,
                score = 0,
                isPaused = false,
                isGameOver = false,
                message = ""
            )
        }
    }

    fun toggleMusic() {
        audioController.toggleMusic()
        if (audioController.isMusicEnabled) audioController.playGameMusic()
    }

    fun toggleSound() {
        audioController.toggleSound()
    }

    private fun nextBasketType(): BasketType {
        return if (Random.nextFloat() > 0.65f) BasketType.SMALL else BasketType.STANDARD
    }
}
