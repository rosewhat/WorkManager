package com.rosewhat.workmanager

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobWorkItem
import android.content.ComponentName
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.rosewhat.workmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.simpleService.setOnClickListener {
            // остановка сервиса
            stopService(MyForegroundService.newInstance(this))
            startService(MyService.newInstance(this, 25))
        }
        binding.simpleForegroundService.setOnClickListener {
            ContextCompat.startForegroundService(
                this,
                MyForegroundService.newInstance(this)

            )
        }
        binding.intentService.setOnClickListener {
            ContextCompat.startForegroundService(
                this, MyIntentService.newInstance(this)
            )
        }
        binding.jobSchedulers.setOnClickListener {
            val componentName = ComponentName(this, MyJobService::class.java)
            // требования сервиса
            val jobInfo = JobInfo.Builder(MyJobService.JOB_ID, componentName)
                // зарядка
                .setRequiresCharging(true)
                // wi fi
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                // устройство выключили и включили
                .build()
            val jbScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val intent = MyJobService.newIntent(2)
                jbScheduler.enqueue(jobInfo, JobWorkItem(intent))
            } else {
                startService(MyIntentService2.newInstance(this, 1))
            }
            binding.jobIntentService.setOnClickListener {
                MyJobIntentService.enqueue(this, 2)
            }
            binding.workManager.setOnClickListener {
                val workManager = WorkManager.getInstance(applicationContext)
                workManager.enqueueUniqueWork(
                    MyWorker.WORK_NAME,
                    ExistingWorkPolicy.APPEND,
                    MyWorker.makeRequest(page = 2)
                )
            }

        }

    }
}