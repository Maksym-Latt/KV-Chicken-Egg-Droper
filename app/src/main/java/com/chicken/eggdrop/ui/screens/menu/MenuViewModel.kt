package com.chicken.eggdrop.ui.screens.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chicken.eggdrop.SoundEngine
import com.chicken.eggdrop.data.GameRepository
import com.chicken.eggdrop.model.MenuUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MenuViewModel
@Inject
constructor(private val soundEngine: SoundEngine, private val repository: GameRepository) :
        ViewModel() {
    private val _uiState = MutableStateFlow(MenuUiState())
    val uiState: StateFlow<MenuUiState> = _uiState

    init {
        viewModelScope.launch {
            repository.selectedSkin.collect { skin ->
                _uiState.update { it.copy(selectedSkin = skin) }
            }
        }
        viewModelScope.launch {
            repository.coins.collect { coins -> _uiState.update { it.copy(coins = coins) } }
        }
        viewModelScope.launch {
            repository.isMusicEnabled.collect { enabled ->
                soundEngine.setMusicEnabled(enabled)
                if (enabled) soundEngine.playMenuMusic() else soundEngine.stopMusic()
                _uiState.update { it.copy(isMusicEnabled = enabled) }
            }
        }
        viewModelScope.launch {
            repository.isSoundEnabled.collect { enabled ->
                soundEngine.setSoundEnabled(enabled)
                _uiState.update { it.copy(isSoundEnabled = enabled) }
            }
        }
    }

    fun toggleMusic() {
        viewModelScope.launch {
            val enabled = repository.toggleMusic()
            soundEngine.setMusicEnabled(enabled)
            if (enabled) soundEngine.playMenuMusic() else soundEngine.stopMusic()
        }
    }

    fun toggleSound() {
        viewModelScope.launch {
            val enabled = repository.toggleSound()
            soundEngine.setSoundEnabled(enabled)
            _uiState.update { it.copy(isSoundEnabled = enabled) }
        }
    }

    fun onStart() {
        viewModelScope.launch {
            if (repository.isMusicEnabled.value) {
                soundEngine.playMenuMusic()
            }
        }
    }
}
