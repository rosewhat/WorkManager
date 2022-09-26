package com.rosewhat.workmanager

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*


class MyService : Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    override fun onCreate() {
        super.onCreate()
    }

    // главный поток
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val start = intent?.getIntExtra(EXTRA_START, 0) ?: 0
        coroutineScope.launch {
            for (i in 0 until start + 100) {
                delay(1000)
            }
        }
        // система убьет сервис -> restart, сервис не прилетит return START_STICKY

        // система убьет сервис -> not restart return START_NOT_STICKY
        //   система убьет сервис -> restart + интент снова прилетит с аргументами
        return START_REDELIVER_INTENT

    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    private fun log(message: String) {
        Log.d("Service_TAG", message)
    }

    companion object {

        private const val EXTRA_START = "start"

        fun newInstance(context: Context, start: Int): Intent {
            return Intent(context, MyService::class.java).apply {
                putExtra(EXTRA_START, start)
            }

        }
    }
}