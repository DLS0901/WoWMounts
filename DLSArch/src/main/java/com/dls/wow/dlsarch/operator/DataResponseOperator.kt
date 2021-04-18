package com.dls.wow.dlsarch.operator

import com.dls.wow.dlsarch.response.DataResponse

abstract class DataResponseOperator<T> : GlobalOperator {


    abstract  fun onSuccess( response:DataResponse.Success<T>)
    abstract  fun onServerError( response:DataResponse.ServerError<T>)
    abstract  fun onException( response: DataResponse.Error<T>)

}