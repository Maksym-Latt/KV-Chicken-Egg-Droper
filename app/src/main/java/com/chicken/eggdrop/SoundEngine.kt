package com.chicken.eggdrop

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import androidx.annotation.RawRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundEngine @Inject constructor(@ApplicationContext private val context: Context) {
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
            if (gamePlayer != null && gamePlayer?.isPlaying == false) {
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
