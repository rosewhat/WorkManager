package com.rosewhat.workmanager

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*


class MyJobService : JobService() {

    // главный поток
    override fun onStartJob(params: JobParameters?): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            coroutineScope.launch {
                var workItem = params?.dequeueWork()
                while (workItem != null) {
                    val page = workItem.intent?.getIntExtra(PAGE, 0)
                    for (i in 0 until 100) {
                        delay(1000)
                    }
                    // сервис в очереди заканчивает
                    params?.completeWork(workItem)
                    // взять новый сервис на очереди
                    workItem = params?.dequeueWork()
                }
            }
            jobFinished(params, false)

        }

        // сервис еще выполняется + сами завершим, false - сервис больше не выполняется
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        // выполниться еще раз
        return true
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }


    private fun log(message: String) {
        Log.d("Service_TAG", message)
    }

    companion object {
        const val JOB_ID = 111
        private const val PAGE = "page"
        fun newIntent(page: Int): Intent {
            return Intent().apply {
                putExtra(PAGE, page)
            }

        }
    }

}