package ru.geekbrains.poplib.rxlearning

import timber.log.Timber

class EventBusEx {
    private val testEvents: List<Any> = listOf("hello", 15, 17, "world", 12, 15L)

    val stringConsumer = MEventBus.subject.subscribe { event ->
        if (event is String) {
            Timber.d("value from string consumer: $event")
        }
    }

    val intConsumer = MEventBus.subject.subscribe { event ->
        if (event is Int) {
            Timber.d("value from int consumer: $event")
        }
    }

    fun exec() {
        testEvents.forEach {
            MEventBus.push(it)
        }
    }
}