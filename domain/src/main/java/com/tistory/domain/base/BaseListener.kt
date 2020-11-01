package com.tistory.domain.base

abstract class BaseListener<T> {
    abstract fun onSuccess(data: T)
    abstract fun onLoading()
    abstract fun onError(error: Throwable)
    abstract fun onLoaded()
}