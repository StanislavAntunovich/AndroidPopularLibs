package ru.geekbrains.poplib.rxlearning

import io.reactivex.rxjava3.subjects.PublishSubject

object MEventBus {
    val subject = PublishSubject.create<Any>()

    fun push(obj: Any) {
        subject.onNext(obj)
    }
}