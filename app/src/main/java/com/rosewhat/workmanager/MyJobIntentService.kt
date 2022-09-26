package com.rosewhat.workmanager

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService


class MyJobIntentService : JobIntentService() {


    override fun onHandleWork(intent: Intent) {
        val page = intent.getIntExtra(PAGE, 0) ?: 0
        for (i in 0 until 100) {
            Thread.sleep(1000)
        }
    }


    private fun log(message: String) {
        Log.d("Service_TAG", message)
    }


    companion object {
        private const val PAGE = "page"
        private const val JOB_ID = 111

        fun enqueue(context: Context, page: Int) {
            enqueueWork(
                context,
                MyJobIntentService::class.java,
                JOB_ID,
                newInstance(context, page)
            )
        }

        private fun newInstance(context: Context, page: Int): Intent {
            return Intent(context, MyJobIntentService::class.java).apply {
                putExtra(PAGE, 0)
            }
        }



    }
}