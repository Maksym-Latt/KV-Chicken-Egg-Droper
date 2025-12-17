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
class GameViewModel
@Inject
constructor(private val repository: GameRepository, private val audioController: AudioController) :
        ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState

    init {
        listenToSharedState()
        randomizeBasketPosition()
        startLoops()
    }

    private fun listenToSharedState() {
        viewModelScope.launch {
            repository.selectedSkin.collect { skin ->
                _uiState.update { it.copy(selectedSkin = skin) }
            }
        }
        viewModelScope.launch {
            repository.coins.collect { coins -> _uiState.update { it.copy(coins = coins) } }
        }
        viewModelScope.launch {
            repository.bestScore.collect { best -> _uiState.update { it.copy(bestScore = best) } }
        }
        viewModelScope.launch {
            repository.isMusicEnabled.collect { enabled ->
                audioController.setMusicEnabled(enabled)
                if (enabled) audioController.playGameMusic() else audioController.stopMusic()
                _uiState.update { it.copy(isMusicEnabled = enabled) }
            }
        }
        viewModelScope.launch {
            repository.isSoundEnabled.collect { enabled ->
                audioController.setSoundEnabled(enabled)
                _uiState.update { it.copy(isSoundEnabled = enabled) }
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
        if (state.isPaused || state.isGameOver || state.showIntro) return
        moveChicken(state)
        if (state.eggState.isFalling) {
            advanceEgg(state)
        }
    }

    private fun moveChicken(state: GameUiState) {
        val speed = 0.004f
        var newX = state.chickenX + speed * state.chickenDirection
        var direction = state.chickenDirection
        if (newX < 0f) {
            newX = 0f
            direction = 1
        } else if (newX > 1f) {
            newX = 1f
            direction = -1
        }
        _uiState.update { it.copy(chickenX = newX, chickenDirection = direction) }
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
            val newScore = state.score + state.basketType.points
            val newCoinsEarned = state.coinsEarned + gainedCoins

            viewModelScope.launch {
                repository.addCoins(gainedCoins)
                repository.updateBestScore(newScore)
            }

            _uiState.update {
                it.copy(
                        eggState = EggState(),
                        score = newScore,
                        coinsEarned = newCoinsEarned,
                        basketType = nextBasketType(),
                        basketX = randomBasketX(),
                        message = "+$gainedCoins"
                )
            }
        } else {
            audioController.playMiss()
            audioController.playLose()

            _uiState.update {
                it.copy(eggState = EggState(), isGameOver = true, message = "MISSED!")
            }
        }
    }

    fun dropEgg() {
        val state = _uiState.value
        if (state.eggState.isFalling || state.isPaused || state.isGameOver || state.showIntro)
                return
        audioController.playDrop()
        _uiState.update { it.copy(eggState = EggState(isFalling = true, y = 0f)) }
    }

    fun togglePause() {
        _uiState.update { it.copy(isPaused = !it.isPaused) }
    }

    fun pauseGame() {
        _uiState.update { current ->
            if (current.isGameOver) current else current.copy(isPaused = true)
        }
    }

    fun restart() {
        val basketX = randomBasketX()
        _uiState.update {
            it.copy(
                    chickenX = 0f,
                    chickenDirection = 1,
                    basketX = basketX,
                    basketDirection = 0,
                    eggState = EggState(),
                    basketType = BasketType.STANDARD,
                    score = 0,
                    coinsEarned = 0,
                    isPaused = false,
                    isGameOver = false,
                    message = "",
                    showIntro = false
            )
        }
    }

    fun toggleMusic() {
        viewModelScope.launch {
            val enabled = repository.toggleMusic()

            audioController.setMusicEnabled(enabled)
            if (enabled) {
                audioController.playGameMusic()
            } else {
                audioController.stopMusic()
            }
        }
    }

    fun toggleSound() {
        viewModelScope.launch {
            val enabled = repository.toggleSound()
            audioController.setSoundEnabled(enabled)
            _uiState.update { it.copy(isSoundEnabled = enabled) }
        }
    }

    fun startGame() {
        _uiState.update { it.copy(showIntro = false, isPaused = false, isGameOver = false) }
        if (audioController.isMusicEnabled) audioController.playGameMusic()
    }

    private fun nextBasketType(): BasketType {
        return if (Random.nextFloat() > 0.65f) BasketType.SMALL else BasketType.STANDARD
    }

    private fun randomizeBasketPosition() {
        _uiState.update { it.copy(basketX = randomBasketX(), basketDirection = 0) }
    }

    private fun randomBasketX(): Float = Random.nextFloat().coerceIn(0f, 1f)
}
