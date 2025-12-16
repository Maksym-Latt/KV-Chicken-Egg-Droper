package com.chicken.eggdropper.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chicken.eggdropper.AudioController
import com.chicken.eggdropper.data.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: GameRepository,
    private val audioController: AudioController
) : ViewModel() {
    val isReady: StateFlow<Boolean> = repository.isInitialized

    init {
        viewModelScope.launch {
            repository.isMusicEnabled.collect { audioController.setMusicEnabled(it) }
        }
        viewModelScope.launch {
            repository.isSoundEnabled.collect { audioController.setSoundEnabled(it) }
        }
    }
}
