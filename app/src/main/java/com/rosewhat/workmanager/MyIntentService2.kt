package com.rosewhat.workmanager

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*


class MyIntentService2 : IntentService(NAME) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    override fun onCreate() {
        super.onCreate()
        setIntentRedelivery(true)

    }

    // в другом потоке
    override fun onHandleIntent(intent: Intent?) {
        for (i in 0 until 100) {
            val page = intent?.getIntExtra(PAGE, 0) ?: 0
            Thread.sleep(1000)
        }
    }

    private fun log(message: String) {
        Log.d("Service_TAG", message)
    }


    companion object {
        private const val NAME = "MyIntentService"
        private const val PAGE = "page"
        fun newInstance(context: Context, page: Int): Intent {
            return Intent(context, MyIntentService2::class.java).apply {
                putExtra(PAGE, 0)
            }
        }

    }
}