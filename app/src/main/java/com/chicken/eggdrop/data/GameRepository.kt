package com.chicken.eggdrop.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.chicken.eggdrop.model.ChickenSkin
import com.chicken.eggdrop.model.DefaultSkins
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

private const val PREFS_NAME = "game_prefs"
private val Context.dataStore by preferencesDataStore(PREFS_NAME)

@Singleton
class GameRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /* -----------------------------
     * Keys
     * ----------------------------- */
    private val coinsKey = intPreferencesKey("coins")
    private val bestKey = intPreferencesKey("best_score")
    private val selectedSkinKey = stringPreferencesKey("selected_skin")
    private val purchasedSkinsKey = stringSetPreferencesKey("purchased_skins")
    private val musicEnabledKey = booleanPreferencesKey("music_enabled")
    private val soundEnabledKey = booleanPreferencesKey("sound_enabled")

    /* -----------------------------
     * Scope for stateIn (application-level)
     * ----------------------------- */
    private val repoScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /* -----------------------------
     * Data flow
     * ----------------------------- */
    private val prefsFlow: Flow<Preferences> = context.dataStore.data

    val coins: StateFlow<Int> =
        prefsFlow
            .map { it[coinsKey] ?: 0 }
            .stateIn(repoScope, SharingStarted.Eagerly, 0)

    val bestScore: StateFlow<Int> =
        prefsFlow
            .map { it[bestKey] ?: 0 }
            .stateIn(repoScope, SharingStarted.Eagerly, 0)

    val isMusicEnabled: StateFlow<Boolean> =
        prefsFlow
            .map { it[musicEnabledKey] ?: true }
            .stateIn(repoScope, SharingStarted.Eagerly, true)

    val isSoundEnabled: StateFlow<Boolean> =
        prefsFlow
            .map { it[soundEnabledKey] ?: true }
            .stateIn(repoScope, SharingStarted.Eagerly, true)

    val purchasedSkins: StateFlow<Set<String>> =
        prefsFlow
            .map { prefs ->
                val defaults = DefaultSkins.filter { it.isDefault }.map { it.id }.toSet()
                val stored = prefs[purchasedSkinsKey].orEmpty()
                stored + defaults
            }
            .stateIn(
                repoScope,
                SharingStarted.Eagerly,
                DefaultSkins.filter { it.isDefault }.map { it.id }.toSet()
            )

    val selectedSkin: StateFlow<ChickenSkin> =
        combine(prefsFlow, purchasedSkins) { prefs, _ ->
            val selectedId = prefs[selectedSkinKey]
            DefaultSkins.firstOrNull { it.id == selectedId } ?: DefaultSkins.first()
        }.stateIn(repoScope, SharingStarted.Eagerly, DefaultSkins.first())

    /* -----------------------------
     * Write operations (suspend)
     * ----------------------------- */
    suspend fun updateBestScore(score: Int) {
        val current = bestScore.value
        val newBest = maxOf(score, current)
        if (newBest == current) return

        context.dataStore.edit { it[bestKey] = newBest }
    }

    suspend fun toggleMusic(): Boolean {
        val updated = !isMusicEnabled.value
        context.dataStore.edit { it[musicEnabledKey] = updated }
        return updated
    }

    suspend fun toggleSound(): Boolean {
        val updated = !isSoundEnabled.value
        context.dataStore.edit { it[soundEnabledKey] = updated }
        return updated
    }

    suspend fun selectSkin(id: String): Boolean {
        val target = DefaultSkins.firstOrNull { it.id == id } ?: return false

        val owned = target.isDefault || purchasedSkins.value.contains(id)
        if (!owned) {
            val spent = spendCoins(target.price)
            if (!spent) return false
        }

        context.dataStore.edit { prefs ->
            if (!target.isDefault) {
                val current = prefs[purchasedSkinsKey].orEmpty()
                prefs[purchasedSkinsKey] = current + id
            }
            prefs[selectedSkinKey] = id
        }
        return true
    }

    suspend fun spendCoins(amount: Int): Boolean {
        val current = coins.value
        if (current < amount) return false

        context.dataStore.edit { prefs ->
            prefs[coinsKey] = (prefs[coinsKey] ?: 0) - amount
        }
        return true
    }

    suspend fun addCoins(amount: Int) {
        context.dataStore.edit { prefs ->
            val current = prefs[coinsKey] ?: 0
            prefs[coinsKey] = current + amount
        }
    }
}
