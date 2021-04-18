package com.dls.wow.dlsarch.adapter

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * 接受一个类型Tin的Call proxy，将其转化为类型Tout的Call
 * 方式就在 proxy执行后，转化为想要的Tout对象再返回
 */
internal abstract class CallDelegate<Tin, Tout>(val proxy: Call<Tin>) : Call<Tout> {


    override fun enqueue(callback: Callback<Tout>) = enqueueImpl(callback)

    abstract fun enqueueImpl(callback: Callback<Tout>)

    override fun isExecuted(): Boolean {
        return proxy.isExecuted
    }

    override fun cancel() {
        proxy.cancel()
    }

    override fun isCanceled(): Boolean {
        return proxy.isCanceled
    }

    override fun clone(): Call<Tout> {
        return cloneImpl()
    }

    abstract fun cloneImpl(): Call<Tout>

    override fun request(): Request {
        return proxy.request()
    }
}