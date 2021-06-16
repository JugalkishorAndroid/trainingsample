package com.jugal.trainingsample.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class CountDownService : Service() {

    lateinit var context: Context
    private var broadcaster: LocalBroadcastManager? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        broadcaster = LocalBroadcastManager.getInstance(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        iniCountDownTimer()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
    }

    private fun iniCountDownTimer() {
        val timer: CountDownTimer = object : CountDownTimer(DEFAULT_TIME, DEFAULT_TICK_UNIT) {
            override fun onTick(millisUntilFinished: Long) {
                val intent = Intent("broadCast")
                intent.putExtra(
                    "remaining_time",
                    (millisUntilFinished / DEFAULT_TICK_UNIT).toString()
                )
                broadcaster?.sendBroadcast(intent)
            }

            override fun onFinish() {
                val intent = Intent("broadCast")
                intent.putExtra("remaining_time", 0.toString())
                broadcaster?.sendBroadcast(intent)
            }
        }
        timer.start()
    }

    companion object {

        private const val DEFAULT_TICK_UNIT = 1_000L
        private const val DEFAULT_TIME = 5_000L
    }
}