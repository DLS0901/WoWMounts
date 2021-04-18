package com.dls.wow.dlsarch.response

import com.dls.wow.dlsarch.operator.DataResponseOperator


fun <T> DataResponse<T>.onSuccess(onResult: (DataResponse.Success<T>) -> Unit): DataResponse<T> {
    if (this is DataResponse.Success) {
        onResult(this)
    }
    return this
}

fun <T> DataResponse<T>.onServerError(onResult: (DataResponse.ServerError<T>) -> Unit): DataResponse<T> {
    if (this is DataResponse.ServerError) {
        onResult(this)
    }
    return this
}

fun <T> DataResponse<T>.onError(onResult: (DataResponse.Error<T>) -> Unit): DataResponse<T> {
    if (this is DataResponse.Error) {
        onResult(this)
    }
    return this
}


@Suppress("UNCHECKED_CAST")
fun <T> DataResponse<T>.operate(): DataResponse<T> = apply {
    val operator = DlsArch.globalOperator
    if (operator != null && operator is DataResponseOperator<*>) {
        operator(operator as DataResponseOperator<T>)
    }
}

fun <T, OP : DataResponseOperator<T>> DataResponse<T>.operator(dataResponseOperator: OP): DataResponse<T> =
    apply {
        when (this) {
            is DataResponse.Success -> dataResponseOperator.onSuccess(this)
            is DataResponse.ServerError -> dataResponseOperator.onServerError(this)
            is DataResponse.Error -> dataResponseOperator.onException(this)
        }
    }

