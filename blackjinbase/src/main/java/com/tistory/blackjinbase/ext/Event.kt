package com.tistory.blackjinbase.ext

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

typealias EventLiveData<T> = LiveData<Event<T>>
//typealias EventMutableLiveData<T> = MutableLiveData<Event<T>>

fun <T> EventLiveData<T>.observeEvent(owner: LifecycleOwner, onChanged: (T) -> Unit) {
    observe(owner, EventObserver(onChanged))
}

class EventMutableLiveData<T>() : MutableLiveData<Event<T>>() {
    constructor(value: T) : this() {
        postEvent(value)
    }
}

fun <T> EventMutableLiveData<T>.postEvent(data: T) {
    postValue(Event(data))
}

fun <T> EventMutableLiveData<T>.observeEvent(owner: LifecycleOwner, onChanged: (T) -> Unit) {
    observe(owner, EventObserver(onChanged))
}

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
//https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}

/**
 * An [Observer] for [Event]s, simplifying the pattern of checking if the [Event]'s content has
 * already been handled.
 *
 * [onEventUnhandledContent] is *only* called if the [Event]'s contents has not been handled.
 */
class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>) {
        event.getContentIfNotHandled()?.let {
            onEventUnhandledContent(it)
        }
    }
}