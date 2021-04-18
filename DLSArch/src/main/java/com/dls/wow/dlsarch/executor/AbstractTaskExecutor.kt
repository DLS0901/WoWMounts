package com.dls.wow.dlsarch.executor

import android.os.Looper

abstract class AbstractTaskExecutor {

    private val isMainThread:Boolean
        get() {
            return Looper.myLooper() == Looper.getMainLooper()
        }


    abstract fun executeOnIOThread(runnable: Runnable)

    abstract fun postOnMainThread(runnable: Runnable)

    fun executeOnMainThread(runnable: Runnable){
        if (isMainThread){
            runnable.run()
        }else{
            executeOnIOThread(runnable)
        }
    }
}