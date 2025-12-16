package com.chicken.eggdropper.ui.screens.skins

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chicken.eggdropper.data.GameRepository
import com.chicken.eggdropper.model.DefaultSkins
import com.chicken.eggdropper.model.SkinsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SkinsViewModel @Inject constructor(
    private val repository: GameRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SkinsUiState())
    val uiState: StateFlow<SkinsUiState> = _uiState

    init {
        viewModelScope.launch {
            repository.selectedSkin.collect { selected ->
                _uiState.update { it.copy(selectedSkin = selected) }
            }
        }
        viewModelScope.launch {
            repository.coins.collect { coins ->
                _uiState.update { it.copy(coins = coins) }
            }
        }
        viewModelScope.launch {
            repository.purchasedSkins.collect { owned ->
                _uiState.update { it.copy(purchasedSkins = owned) }
            }
        }
        _uiState.update { it.copy(skins = DefaultSkins) }
    }

    fun selectSkin(id: String) {
        val targetSkin = _uiState.value.skins.firstOrNull { it.id == id } ?: return

        viewModelScope.launch {
            repository.selectSkin(targetSkin.id)
        }
    }
}
