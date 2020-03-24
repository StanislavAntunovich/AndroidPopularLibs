package ru.geekbrains.poplib.rxlearning


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.schedulers.TestScheduler
import timber.log.Timber
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class Operator {
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

        fun createJust() = Observable.just("1", "2", "3", "3")

        fun createFromIterable() = Observable.fromIterable(listOf("1", "2", "3", "3"))

        fun createRange() = Observable.range(10, 10)

        fun createInterval() = Observable.interval(1, TimeUnit.SECONDS)

        fun fromCallable() = Observable.fromCallable {
            val result = randomResultOperation()
            return@fromCallable "Result of operation $result"
        }
        
        fun create() = Observable.create<String> { emitter ->
            for(i in 4..7){
                Thread.sleep(Random.nextInt(500).toLong())
                emitter.onNext(i.toString())
            }
            emitter.onComplete()
        }
    }

    class Consumer(val producer: Producer) {

        fun consume() {
            //execMap()
            //execFilter()
            //execDistinct()
            //execTake()
            //execSkip()
            //execMerge()
            execFlatMap()
            execSwitchMap()
        }

        fun execMap() {
            producer.createJust()
                .map { it + "x" }
                .subscribe({ s ->
                    Timber.d("onNext: $s")
                }, { e ->
                    Timber.e(e)
                }, {
                    Timber.d("onComplete")
                })
        }

        fun execFilter() {
            producer.createJust()
                .filter { it.toInt() > 1 }
                .subscribe({ s ->
                    Timber.d("onNext: $s")
                }, { e ->
                    Timber.e(e)
                }, {
                    Timber.d("onComplete")
                })
        }

        fun execDistinct() {
            producer.createJust()
                .distinct()
                .subscribe({ s ->
                    Timber.d("onNext: $s")
                }, { e ->
                    Timber.e(e)
                }, {
                    Timber.d("onComplete")
                })
        }

        fun execTake() {
            producer.createJust()
                .take(2)
                .subscribe({ s ->
                    Timber.d("onNext: $s")
                }, { e ->
                    Timber.e(e)
                }, {
                    Timber.d("onComplete")
                })
        }

        fun execSkip() {
            producer.createJust()
                .take(2)
                .subscribe({ s ->
                    Timber.d("onNext: $s")
                }, { e ->
                    Timber.e(e)
                }, {
                    Timber.d("onComplete")
                })
        }

        fun execMerge(){
            producer.create().subscribeOn(Schedulers.newThread())
                .mergeWith(producer.create())
                .subscribe({ s ->
                    Timber.d("onNext: $s")
                }, { e ->
                    Timber.e(e)
                }, {
                    Timber.d("onComplete")
                })
        }

        fun execFlatMap(){
            val testSchedulers = TestScheduler()
            producer.createJust()
                .flatMap { s ->
                    val delay = Random.nextInt(10).toLong()
                    return@flatMap Observable.just(s + "x").delay(delay, TimeUnit.SECONDS, testSchedulers)
                }
                .toList()
                .subscribe({ list ->
                    Timber.d("onNextFlatMap: $list")
                },{
                    Timber.e(it)
                })
            testSchedulers.advanceTimeBy(1, TimeUnit.MINUTES)
        }

        fun execSwitchMap(){
            val testSchedulers = TestScheduler()
            producer.createJust()
                .switchMap { s ->
                    val delay = Random.nextInt(10).toLong()
                    return@switchMap Observable.just(s + "x").delay(delay, TimeUnit.SECONDS, testSchedulers)
                }
                .toList()
                .subscribe({ list ->
                    Timber.d("onNextSwitchMap: $list")
                },{
                    Timber.e(it)
                })
            testSchedulers.advanceTimeBy(1, TimeUnit.MINUTES)
        }

    }

}