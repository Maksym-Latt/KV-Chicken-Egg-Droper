package com.chicken.eggdropper

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import androidx.annotation.RawRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioController @Inject constructor(@ApplicationContext private val context: Context) {
    private val soundPool: SoundPool =
            SoundPool.Builder()
                    .setAudioAttributes(
                            AudioAttributes.Builder()
                                    .setUsage(AudioAttributes.USAGE_GAME)
                                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                    .build()
                    )
                    .setMaxStreams(4)
                    .build()

    private val sounds: Map<Int, Int> =
            mapOf(
                    R.raw.sfx_drop to loadSound(R.raw.sfx_drop),
                    R.raw.sfx_drop_success to loadSound(R.raw.sfx_drop_success),
                    R.raw.sfx_miss to loadSound(R.raw.sfx_miss),
                    R.raw.sfx_lose to loadSound(R.raw.sfx_lose)
            )

    private var menuPlayer: MediaPlayer? = null
    private var gamePlayer: MediaPlayer? = null

    var isMusicEnabled: Boolean = true
        private set
    var isSoundEnabled: Boolean = true
        private set

    fun setMusicEnabled(enabled: Boolean) {
        isMusicEnabled = enabled
        if (!enabled) {
            pauseMusic()
        } else {
            // If we are setting it to true, we don't necessarily know which one to play here
            // without context,
            // but usually the specific screen will call playXMusic().
            // However, if we are just toggling settings in a screen, we might want to resume.
            // For now, adhering to the calling pattern where playXMusic checks the flag.
        }
    }

    fun setSoundEnabled(enabled: Boolean) {
        isSoundEnabled = enabled
    }

    fun playMenuMusic() {
        if (!isMusicEnabled) return

        // Pause game player if playing
        if (gamePlayer?.isPlaying == true) {
            gamePlayer?.pause()
        }

        if (menuPlayer == null) {
            menuPlayer = MediaPlayer.create(context, R.raw.menu_music)?.apply { isLooping = true }
        }

        menuPlayer?.let { player ->
            if (!player.isPlaying) {
                player.start()
            }
        }
    }

    fun playGameMusic() {
        if (!isMusicEnabled) return

        // Pause menu player if playing
        if (menuPlayer?.isPlaying == true) {
            menuPlayer?.pause()
        }

        if (gamePlayer == null) {
            gamePlayer = MediaPlayer.create(context, R.raw.game_music)?.apply { isLooping = true }
        }

        gamePlayer?.let { player ->
            if (!player.isPlaying) {
                player.start()
            }
        }
    }

    fun playDrop() = playEffect(R.raw.sfx_drop)

    fun playSuccess() = playEffect(R.raw.sfx_drop_success)

    fun playMiss() = playEffect(R.raw.sfx_miss)

    fun playLose() = playEffect(R.raw.sfx_lose)

    fun onAppForeground() {
        if (isMusicEnabled) {
            // Resume whichever was playing or intended to be playing?
            // Simple heuristic: if we have a player that was initialized, assume we want to resume
            // it?
            // Or better: Let the Activity/Fragments handle logic, but here we just ensure if they
            // were paused we might resume.
            // Given the current usage, players are paused on background.
            // We can check which one should be active.
            // But actually, the screen usually calls playXMusic on start.
            // onAppForeground is called from MainActivity onResume.
            // If we just came from background, we want to resume.

            // Try to resume the one that was most likely active.
            if (gamePlayer != null && gamePlayer?.isPlaying == false) {
                // But wait, if we are in Menu, gamePlayer might be non-null but paused.
                // This simple logic is tricky.
                // The previous code did: if (gamePlayer != null) gamePlayer?.start() else
                // menuPlayer?.start()
                // Note: This logic assumes if gamePlayer exists, we are in game.
                if (gamePlayer != null) gamePlayer?.start() else menuPlayer?.start()
            } else if (menuPlayer != null && menuPlayer?.isPlaying == false) {
                menuPlayer?.start()
            }
        }
    }

    fun onAppBackground() {
        if (menuPlayer?.isPlaying == true) menuPlayer?.pause()
        if (gamePlayer?.isPlaying == true) gamePlayer?.pause()
    }

    fun persistSettings() {
        // Stub for persisting settings when storage is added
    }

    fun stopMusic() {
        pauseMusic()
    }

    private fun pauseMusic() {
        if (menuPlayer?.isPlaying == true) menuPlayer?.pause()
        if (gamePlayer?.isPlaying == true) gamePlayer?.pause()
    }

    private fun playEffect(@RawRes res: Int) {
        if (!isSoundEnabled) return
        val soundId = sounds[res] ?: return
        soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
    }

    private fun loadSound(@RawRes res: Int): Int = soundPool.load(context, res, 1)
}
