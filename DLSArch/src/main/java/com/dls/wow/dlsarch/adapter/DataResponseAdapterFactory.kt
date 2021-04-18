package com.dls.wow.dlsarch.adapter

import com.dls.wow.dlsarch.response.DataResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


/**
 *  如果接口类型为 retrofit.Call<ServerResponse<T>>则交给ServerResponseAdapter处理。
 */
class DataResponseAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<Type, Call<DataResponse<Type>>>? {

        return when (getRawType(returnType)) {
            Call::class.java -> {
                val paramType = getParameterUpperBound(0, returnType as ParameterizedType)
                when (getRawType(paramType)) {
                    DataResponse::class.java -> {
                        return ServerResponseAdapter(returnType);
                    }
                    else -> null
                }
            }

            else -> null
        }


    }
}