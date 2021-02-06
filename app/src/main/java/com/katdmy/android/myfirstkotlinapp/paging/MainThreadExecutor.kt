package com.katdmy.android.myfirstkotlinapp.paging

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

class MainThreadExecutor : Executor {

    private val mHandler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable?) {
        command?.let {
            mHandler.post(it)
        }
    }
}