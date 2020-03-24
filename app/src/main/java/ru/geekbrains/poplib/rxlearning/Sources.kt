package ru.geekbrains.poplib.rxlearning


import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.lang.RuntimeException
import kotlin.random.Random

class Sources {
    fun exec() {
        val producer = Producer()
        val consumer = Consumer(producer)
        consumer.consume()
    }

    class Producer {

        fun randomResultOperation(): Boolean {
            Timber.d("randomResultOperation executing on ${Thread.currentThread().name}}")
            Thread.sleep(Random.nextLong(2000))
            return listOf(true, false, true)[Random.nextInt(2)]
        }

        fun single() = Single.fromCallable {
            return@fromCallable "1"
        }

        fun maybe() = Maybe.create<String> { emitter ->
            try {
                randomResultOperation().let {
                    if (it) {
                        emitter.onSuccess("result of operation")
                    } else {
                        emitter.onComplete()
                    }
                }
            } catch (t: Throwable) {
                emitter.onError(t)
            }
        }

        fun completable() = Completable.create { emitter ->
            randomResultOperation().let {
                if (it) {
                    emitter.onComplete()
                } else {
                    emitter.onError(RuntimeException("Error"))
                }
            }
        }

        fun publishSubject() = PublishSubject.create<String>().apply {
            Completable.fromAction {
                Thread.sleep(2000)
                onNext("from apply")
            }.subscribeOn(Schedulers.computation()).subscribe()
        }

    }

    class Consumer(val producer: Producer) {

        fun consume() {
            //execSingle()
            //execMaybe()
            //execCompletable()
            execPublishSubject()
        }

        fun execSingle() {
            producer.single()
                .subscribe({ s ->
                    Timber.d("onNext: $s")
                }, {
                    Timber.e(it)
                })
        }

        fun execMaybe() {
            producer.maybe()
                .subscribe({ s ->
                    Timber.d("onSuccess: $s")
                }, {
                    Timber.e(it)
                }, {
                    Timber.d("onComplete")
                })
        }

        fun execCompletable(){
            producer.completable()
                .subscribe({
                    Timber.d("onComplete")
                }, {
                    Timber.e(it)
                })
        }

        fun execPublishSubject(){
            Timber.d("execPublishSubject start")
            val subject = producer.publishSubject()
            subject.subscribe({ s ->
                    Timber.d("onNext: $s")
                }, {
                    Timber.e(it)
                }, {
                    Timber.d("onComplete")
                })
            Timber.d("execPublishSubject after subscribe")
            subject.onNext("from exec")
        }
    }

}