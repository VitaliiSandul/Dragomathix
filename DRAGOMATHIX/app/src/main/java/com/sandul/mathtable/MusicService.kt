package com.sandul.mathtable

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MusicService : Service() {

    companion object {
        var mediaPlayer: MediaPlayer? = null
        fun soundOn(){
            mediaPlayer!!.start()
        }
        fun soundOff(){
            mediaPlayer!!.pause()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaPlayer!!.isLooping = true
        mediaPlayer!!.setVolume(100f, 100f)
        mediaPlayer!!.start()
        mediaPlayer!!.pause()
        return START_REDELIVER_INTENT
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.dragon)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer!!.stop()
        mediaPlayer!!.release()
    }
}
