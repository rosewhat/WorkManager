package com.rosewhat.workmanager

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


class MyForegroundService : Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    override fun onCreate() {
        super.onCreate()
        createNotification()
        // обещаем, что сервис покажет уведомление с загрузкой
        startForeground(NOTIFICATION_ID, createNotification())
    }

    // главный поток
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        coroutineScope.launch {
            for (i in 0 until 100) {
                delay(1000)
            }
            // остановка сервиса
            stopSelf()
        }
        // система убьет сервис -> restart, сервис не прилетит return START_STICKY

        // система убьет сервис -> not restart return START_NOT_STICKY
        //   система убьет сервис -> restart + интент снова прилетит с аргументами
        return START_STICKY

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
            return Intent(context, MyForegroundService::class.java)

        }

        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
        private const val NOTIFICATION_ID = 1
    }
}