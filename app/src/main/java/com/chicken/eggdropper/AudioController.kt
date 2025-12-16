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
class AudioController @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val soundPool: SoundPool = SoundPool.Builder()
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .setMaxStreams(4)
        .build()

    private val sounds: Map<Int, Int> = mapOf(
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

    fun toggleMusic() {
        isMusicEnabled = !isMusicEnabled
        if (!isMusicEnabled) {
            stopMusic()
        }
    }

    fun toggleSound() {
        isSoundEnabled = !isSoundEnabled
    }

    fun playMenuMusic() {
        if (!isMusicEnabled || menuPlayer?.isPlaying == true) return
        stopMusic()
        menuPlayer = MediaPlayer.create(context, R.raw.menu_music)?.apply {
            isLooping = true
            start()
        }
    }

    fun playGameMusic() {
        if (!isMusicEnabled || gamePlayer?.isPlaying == true) return
        stopMusic()
        gamePlayer = MediaPlayer.create(context, R.raw.game_music)?.apply {
            isLooping = true
            start()
        }
    }

    fun playDrop() = playEffect(R.raw.sfx_drop)

    fun playSuccess() = playEffect(R.raw.sfx_drop_success)

    fun playMiss() = playEffect(R.raw.sfx_miss)

    fun playLose() = playEffect(R.raw.sfx_lose)

    fun onAppForeground() {
        if (isMusicEnabled) {
            if (gamePlayer != null) gamePlayer?.start() else menuPlayer?.start()
        }
    }

    fun onAppBackground() {
        menuPlayer?.pause()
        gamePlayer?.pause()
    }

    fun persistSettings() {
        // Stub for persisting settings when storage is added
    }

    fun stopMusic() {
        menuPlayer?.release()
        gamePlayer?.release()
        menuPlayer = null
        gamePlayer = null
    }

    private fun playEffect(@RawRes res: Int) {
        if (!isSoundEnabled) return
        val soundId = sounds[res] ?: return
        soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
    }

    private fun loadSound(@RawRes res: Int): Int = soundPool.load(context, res, 1)
}
