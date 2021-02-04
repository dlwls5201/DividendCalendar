package com.tistory.domain.usecase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.tistory.blackjinbase.util.Dlog
import com.tistory.domain.base.BaseListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetLatestVersionUsecase(
    private val remoteConfig: FirebaseRemoteConfig
) {

    suspend fun get(listener: BaseListener<Int>) {
        withContext(Dispatchers.Main) {
            listener.onLoading()

            remoteConfig.fetch()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val updated = task.result
                        Dlog.d("Config params updated: $updated")

                        val version = remoteConfig.getString("version")
                        Dlog.d("version : $version")

                        try {
                            val versionInt = version.toIntOrNull() ?: -1
                            listener.onSuccess(versionInt)
                            listener.onLoaded()
                        } catch (e: Exception) {
                            listener.onError(e)
                            listener.onLoaded()
                        }

                    } else {
                        listener.onError(Throwable("remote config fail"))
                        listener.onLoaded()
                    }
                }
        }
    }
}