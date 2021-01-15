package com.tistory.domain.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class SuspendUseCase<in Params, Result : Any> {

    protected abstract suspend fun onSuccess(params: Params): Result

    lateinit var restul: Result

    suspend fun build(params: Params, listener: BaseListener<Result>) {
        withContext(Dispatchers.Main) {
            try {
                listener.onLoading()
                withContext(Dispatchers.IO) {
                    restul = onSuccess(params)
                }
                listener.onSuccess(restul)
            } catch (e: Exception) {
                listener.onError(e)
            }
            listener.onLoaded()
        }
    }
}