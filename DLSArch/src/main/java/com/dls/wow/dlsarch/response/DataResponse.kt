package com.dls.wow.dlsarch.response

import retrofit2.Response
import java.lang.Exception

/**
 *  服务器返回数据的封装，其他地方不可实现，
 *  根据类型就可以判断是成功获取
 */
sealed class DataResponse<T> {


    data class Success<T>(val response: Response<T>) : DataResponse<T>() {
        val statusCode: ServerStatusCode = getStatusCodeFromResponse(response)


        val data: T? = response.body()

        override fun toString(): String {
            return "ServerResponse.Success: [ statusCode = $statusCode , data = $data ]"
        }
    }

    data class ServerError<T>(val response: Response<T>) : DataResponse<T>() {

        val statusCode: ServerStatusCode = getStatusCodeFromResponse(response)
        val errorBody = response.errorBody()

        override fun toString(): String {
            return "ServerResponse.ServerError: [ statusCode = $statusCode , errorBody = $errorBody ]"
        }
    }

    data class Error<T>(val throwable: Throwable) : DataResponse<T>() {
        override fun toString(): String {
            return "ServerResponse.Exception: [ message =  ${throwable.localizedMessage} ]"
        }
    }

    fun getStatusCodeFromResponse(response: Response<T>): ServerStatusCode {
        return ServerStatusCode.values().find { it.code == response.code() }
            ?: ServerStatusCode.Unknown
    }


    companion object {

        /**
         * 执行一个block函数，返回一个reponse，如果结果在[successCodeRange]
         * 范围内，则认为执行成功
         */
        fun <T> handleOkHttpResponse(
            successCodeRange: IntRange = 200..299,
            block: () -> Response<T>
        ): DataResponse<T> {
            return try {
                val response = block()
                if (response.code() in successCodeRange) {
                    Success(response).operate()
                } else {
                    ServerError(response).operate()
                }
            } catch (e: Exception) {
                Error<T>(e)
            }.operate()
        }

        fun <T> exception(throwable: Throwable) = Error<T>(throwable).operate()
    }
}