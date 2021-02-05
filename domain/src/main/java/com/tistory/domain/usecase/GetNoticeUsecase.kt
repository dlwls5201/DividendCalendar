package com.tistory.domain.usecase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tistory.blackjinbase.util.Dlog
import com.tistory.domain.base.BaseListener
import com.tistory.domain.constant.Constant
import com.tistory.domain.model.NoticeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class GetNoticeUsecase(
    private val remoteConfig: FirebaseRemoteConfig
) {

    private val gson = Gson()

    suspend fun get(listener: BaseListener<List<NoticeResponse.ItemResponse>>) {
        withContext(Dispatchers.Main) {
            listener.onLoading()

            remoteConfig.fetch()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val updated = task.result
                        Dlog.d("Config params updated: $updated")

                        val noticeItemsJsonString = remoteConfig.getString("notice_items")
                        Dlog.d("noticeItemsJsonString : $noticeItemsJsonString")

                        try {
                            val noticeResponse =
                                convertJsonArrayToList<NoticeResponse>(noticeItemsJsonString)

                            val response = findNoticeForDisplayLanguage(noticeResponse)

                            listener.onSuccess(response.items)
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

    private inline fun <reified T> convertJsonArrayToList(jsonString: String): List<T> {
        return gson.fromJson(
            jsonString,
            TypeToken.getParameterized(ArrayList::class.java, T::class.java).type
        )
    }

    private fun findNoticeForDisplayLanguage(notices: List<NoticeResponse>): NoticeResponse {
        val language = Locale.getDefault().language.toLowerCase(Locale.ROOT)
        Dlog.d("language : $language")

        return notices.find { it.country == language }
            ?: notices.find { it.country == Constant.DEFAULT_LANGUAGE }
            ?: NoticeResponse()
    }
}