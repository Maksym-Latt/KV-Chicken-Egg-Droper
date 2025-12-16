package com.chicken.eggdropper.ui.screens.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chicken.eggdropper.AudioController
import com.chicken.eggdropper.data.GameRepository
import com.chicken.eggdropper.model.MenuUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val audioController: AudioController,
    private val repository: GameRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MenuUiState())
    val uiState: StateFlow<MenuUiState> = _uiState

    init {
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
            repository.isMusicEnabled.collect { enabled ->
                audioController.setMusicEnabled(enabled)
                if (enabled) audioController.playMenuMusic() else audioController.stopMusic()
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

    fun toggleMusic() {
        val enabled = repository.toggleMusic()
        audioController.setMusicEnabled(enabled)
        if (enabled) audioController.playMenuMusic() else audioController.stopMusic()
    }

    fun toggleSound() {
        val enabled = repository.toggleSound()
        audioController.setSoundEnabled(enabled)
    }
}
