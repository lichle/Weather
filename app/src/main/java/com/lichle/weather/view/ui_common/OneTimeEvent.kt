package com.lichle.weather.view.ui_common

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

// Event handler for collecting one-time events
class EventHandler<T> {
    private val _events = Channel<T>()
    val events = _events.receiveAsFlow()

    suspend fun emitEvent(event: T) {
        _events.send(event)
    }
}