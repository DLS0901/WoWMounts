package com.dls.wow.dlsarch.adapter

import com.dls.wow.dlsarch.response.DataResponse
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.lang.reflect.Type

/**
 *
 * Type即为 Call<DataResponse<T>>
 */
class ServerResponseAdapter(private val returnType: Type) :
    CallAdapter<Type, Call<DataResponse<Type>>> {


    override fun adapt(call: Call<Type>): Call<DataResponse<Type>> {
        return ServerResponseCallDelegate(call)
    }

    override fun responseType(): Type = returnType
}

internal class ServerResponseCallDelegate<T>(proxy: Call<T>) :
    CallDelegate<T, DataResponse<T>>(proxy) {
    override fun enqueueImpl(callback: Callback<DataResponse<T>>) {
        proxy.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onResponse(
                    this@ServerResponseCallDelegate,
                    Response.success(DataResponse.exception(t))
                )
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                // 将Call<T> 转化为 Call<ServerResponse<Type>>
                val serverResponse = DataResponse.handleOkHttpResponse {
                    response
                }
                callback.onResponse(
                    this@ServerResponseCallDelegate,
                    Response.success(serverResponse)
                )
            }

        })
    }

    override fun cloneImpl(): Call<DataResponse<T>> = ServerResponseCallDelegate(proxy.clone())

    override fun timeout(): Timeout = Timeout.NONE

    override fun execute(): Response<DataResponse<T>> = try {
        val response = proxy.execute()
        Response.success(DataResponse.handleOkHttpResponse {
            response
        })
    } catch (e: Exception) {
        Response.success(DataResponse.exception<T>(e))
    }
}