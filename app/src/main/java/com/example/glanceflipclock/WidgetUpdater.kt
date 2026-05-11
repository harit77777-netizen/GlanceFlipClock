package com.example.glanceflipclock

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.TimeUnit

class WidgetUpdater(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            FlipClockWidget().updateAll(applicationContext)
            scheduleNextUpdate(applicationContext)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        fun startWork(context: Context) {
            scheduleNextUpdate(context)
        }
        
        private fun scheduleNextUpdate(context: Context) {
            val calendar = Calendar.getInstance()
            val secondsLeft = 60 - calendar.get(Calendar.SECOND)
            
            val workRequest = OneTimeWorkRequestBuilder<WidgetUpdater>()
                .setInitialDelay(secondsLeft.toLong(), TimeUnit.SECONDS)
                .build()
            
            WorkManager.getInstance(context).enqueueUniqueWork(
                "flip_clock_update",
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
        }
    }
}
