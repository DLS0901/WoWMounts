package com.dls.wow.dlsarch.executor

import android.os.Build
import android.os.Handler
import android.os.Looper
import java.lang.Exception
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

class ThreadPoolTaskExecutor : AbstractTaskExecutor() {

    private val mHandler: Handler by lazy { createAsyncHandler() }


    private val threadPoolExecutor = Executors.newFixedThreadPool(
        4,
        object : ThreadFactory {

            val THREAD_NAME_PREFIX = "dls_thread_%d"

            val threadNum = AtomicInteger(0);

            override fun newThread(r: Runnable?): Thread {
                val thread = Thread(r)
                thread.name = String.format(THREAD_NAME_PREFIX, threadNum.getAndIncrement())

                return thread
            }
        })

    override fun executeOnIOThread(runnable: Runnable) {
        threadPoolExecutor.execute(runnable)
    }

    override fun postOnMainThread(runnable: Runnable) {
        mHandler.post(runnable)
    }

    private fun createAsyncHandler(): Handler {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Handler.createAsync(Looper.getMainLooper())
        } else {
            try {
                Handler::class.java.getDeclaredConstructor(
                    Looper::class.java,
                    Handler.Callback::class.java,
                    Boolean::class.java
                ).newInstance(Looper.getMainLooper(), null, true);
            } catch (e: Exception) {
                e.printStackTrace()
                return Handler(Looper.getMainLooper())
            }
        }
    }
}