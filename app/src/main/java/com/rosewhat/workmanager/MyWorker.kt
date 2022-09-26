package com.rosewhat.workmanager

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import androidx.work.*


class MyWorker(
    context: Context,
    private val workerParameters: WorkerParameters
) : Worker(context, workerParameters) {
    // в другом потоке
    override fun doWork(): Result {
        val page = workerParameters.inputData.getInt(PAGE, 0)
        for (i in 0 until 100) {
            Thread.sleep(1000)
        }
        // при успешном load
        return Result.success()
    }

    companion object {
        private const val PAGE = "page"
        const val WORK_NAME = "work name"

        fun makeRequest(page: Int): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<MyWorker>().apply {
                setInputData(workDataOf(PAGE to page))
                    .setConstraints(makeConstrains())
            }.build()
        }

        private fun makeConstrains(): Constraints {
            return Constraints.Builder()
                .setRequiresCharging(true)
                .build()
        }
    }
}