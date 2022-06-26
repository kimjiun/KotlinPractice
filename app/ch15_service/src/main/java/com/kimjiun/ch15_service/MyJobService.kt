package com.kimjiun.ch15_service

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.SystemClock
import android.util.Log

class MyJobService : JobService() {

    override fun onCreate() {
        super.onCreate()
        Log.d("kkang","MyJobService.......onCreate()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("kkang","MyJobService.......onDestroy()")
    }

    override fun onStartJob(jobParameters: JobParameters): Boolean {

        jobParameters.extras.getString("extra_data")
        //.......................
        Log.d("kkang","JobSchedulerService... onStartJob....")
        Thread(Runnable {
            var sum = 0
            for (i in 1..10) {
                sum += i
                SystemClock.sleep(1000)
            }
            Log.d("kkang","JobSchedulerService... onStartJob... thread result : $sum")
            jobFinished(jobParameters, false)
        }).start()
        return true
    }


    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d("kkang","MyJobService.......onStopJob()")
        return false
    }
}