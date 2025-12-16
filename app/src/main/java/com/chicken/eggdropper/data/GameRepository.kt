package com.chicken.eggdropper.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.chicken.eggdropper.model.ChickenSkin
import com.chicken.eggdropper.model.DefaultSkins
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.maxOf

private const val PREFS_NAME = "game_prefs"
private val Context.dataStore by preferencesDataStore(PREFS_NAME)

@Singleton
class GameRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val _selectedSkin = MutableStateFlow(DefaultSkins.first())
    val selectedSkin: StateFlow<ChickenSkin> = _selectedSkin

    private val _purchasedSkins = MutableStateFlow(DefaultSkins.filter { it.isDefault }.map { it.id }.toSet())
    val purchasedSkins: StateFlow<Set<String>> = _purchasedSkins

    private val _coins = MutableStateFlow(0)
    val coins: StateFlow<Int> = _coins

    private val _bestScore = MutableStateFlow(0)
    val bestScore: StateFlow<Int> = _bestScore

    private val _isMusicEnabled = MutableStateFlow(true)
    val isMusicEnabled: StateFlow<Boolean> = _isMusicEnabled

    private val _isSoundEnabled = MutableStateFlow(true)
    val isSoundEnabled: StateFlow<Boolean> = _isSoundEnabled

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized

    private val coinsKey = intPreferencesKey("coins")
    private val bestKey = intPreferencesKey("best_score")
    private val selectedSkinKey = stringPreferencesKey("selected_skin")
    private val purchasedSkinsKey = stringSetPreferencesKey("purchased_skins")
    private val musicEnabledKey = booleanPreferencesKey("music_enabled")
    private val soundEnabledKey = booleanPreferencesKey("sound_enabled")

    init {
        scope.launch { observePreferences() }
    }

    private suspend fun observePreferences() {
        context.dataStore.data.collect { prefs ->
            _coins.value = prefs[coinsKey] ?: 0
            _bestScore.value = prefs[bestKey] ?: 0
            val purchased = prefs[purchasedSkinsKey] ?: _purchasedSkins.value
            val ensuredPurchased =
                purchased + DefaultSkins.filter { it.isDefault }.map { it.id }.toSet()
            _purchasedSkins.value = ensuredPurchased

            if (prefs[purchasedSkinsKey] == null) {
                persist { it[purchasedSkinsKey] = ensuredPurchased }
            }

            val selectedId = prefs[selectedSkinKey] ?: _selectedSkin.value.id
            _selectedSkin.value = DefaultSkins.firstOrNull { it.id == selectedId }
                ?: DefaultSkins.first()

            _isMusicEnabled.value = prefs[musicEnabledKey] ?: true
            _isSoundEnabled.value = prefs[soundEnabledKey] ?: true
            _isInitialized.value = true
        }
    }

    fun updateBestScore(score: Int) {
        val newBest = maxOf(score, _bestScore.value)
        if (newBest == _bestScore.value) return
        _bestScore.value = newBest
        persist { it[bestKey] = newBest }
    }

    fun toggleMusic(): Boolean {
        val updated = !_isMusicEnabled.value
        _isMusicEnabled.value = updated
        persist { it[musicEnabledKey] = updated }
        return updated
    }

    fun toggleSound(): Boolean {
        val updated = !_isSoundEnabled.value
        _isSoundEnabled.value = updated
        persist { it[soundEnabledKey] = updated }
        return updated
    }

    fun selectSkin(id: String): Boolean {
        val target = DefaultSkins.firstOrNull { it.id == id } ?: return false
        val owned = target.isDefault || _purchasedSkins.value.contains(id)
        if (!owned && !spendCoins(target.price)) return false

        if (!target.isDefault) {
            val updated = _purchasedSkins.value + id
            _purchasedSkins.value = updated
            persist { it[purchasedSkinsKey] = updated }
        }

        _selectedSkin.value = target
        persist { it[selectedSkinKey] = id }
        return true
    }

    fun spendCoins(amount: Int): Boolean {
        if (_coins.value < amount) return false
        val updated = _coins.value - amount
        _coins.value = updated
        persist { it[coinsKey] = updated }
        return true
    }

    fun addCoins(amount: Int) {
        val updated = _coins.value + amount
        _coins.value = updated
        persist { it[coinsKey] = updated }
    }

    private fun persist(block: suspend (Preferences.MutablePreferences) -> Unit) {
        scope.launch { context.dataStore.edit { block(it) } }
    }
}
