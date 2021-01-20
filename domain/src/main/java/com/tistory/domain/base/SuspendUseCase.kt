package com.tistory.domain.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class SuspendUseCase<in Params, Result : Any> {

    protected abstract suspend fun onSuccess(params: Params): Result

    private lateinit var result: Result

    suspend fun build(params: Params, listener: BaseListener<Result>) {
        withContext(Dispatchers.Main) {
            try {
                listener.onLoading()
                withContext(Dispatchers.IO) {
                    result = onSuccess(params)
                }
                listener.onSuccess(result)
            } catch (e: Exception) {
                listener.onError(e)
            }
            listener.onLoaded()
        }
    }
}