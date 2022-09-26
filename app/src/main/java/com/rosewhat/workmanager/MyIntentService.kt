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


class MyIntentService : IntentService(NAME) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    override fun onCreate() {
        super.onCreate()
        createNotification()
        setIntentRedelivery(true)
        // обещаем, что сервис покажет уведомление с загрузкой
        startForeground(NOTIFICATION_ID, createNotification())
    }

    // в другом потоке
    override fun onHandleIntent(intent: Intent?) {
        for (i in 0 until 100) {
            Thread.sleep(1000)
        }
    }

    private fun log(message: String) {
        Log.d("Service_TAG", message)
    }

    private fun createNotificationChannel() {
        // отображение сервисов
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun createNotification() = NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle("Title")
        .setContentText("Text")
        .setSmallIcon(R.drawable.ic_launcher_background)
        .build()

    companion object {

        fun newInstance(context: Context): Intent {
            return Intent(context, MyIntentService::class.java)

        }

        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
        private const val NOTIFICATION_ID = 1
        private const val NAME = "MyIntentService"
    }
}