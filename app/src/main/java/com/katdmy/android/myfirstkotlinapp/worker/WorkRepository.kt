package com.katdmy.android.myfirstkotlinapp.worker

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import java.util.concurrent.TimeUnit

class WorkRepository {
    private val constraints = Constraints.Builder()
        .setRequiresCharging(true)
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()
    val periodDbUpdate = PeriodicWorkRequestBuilder<PeriodWorker>(8, TimeUnit.HOURS)
        .setConstraints(constraints)
        .build()
}