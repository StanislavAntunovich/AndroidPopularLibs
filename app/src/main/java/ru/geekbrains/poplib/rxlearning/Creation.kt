package ru.geekbrains.poplib.rxlearning


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class Creation {
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

            //chat - некая абстрактная реализация некоторого чата
//            chat.setOnMessageListener { message ->
//                emitter.onNext(messages)
//            }
//
//            chat.setOnDisconnectListener {
//                emitter.onComplete()
//            }
//
//            chat.setOnErrorListener { e ->
//                emitter.onError(e)
//            }


            for(i in 0..10){
                randomResultOperation().let {
                    if(it){
                        emitter.onNext("Success: $it")
                    } else {
                        emitter.onError(RuntimeException("Error"))
                        return@create
                    }
                }
            }
            emitter.onComplete()
        }
    }

    class Consumer(val producer: Producer) {
        val stringObserver = object : Observer<String> {
            var disposable: Disposable? = null

            override fun onComplete() {
                Timber.d("onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                Timber.d("onSubscribe")
                disposable = d
            }

            override fun onNext(s: String) {
                Timber.d("onNext: $s")
            }

            override fun onError(e: Throwable?) {
                Timber.e(e)
            }
        }

        fun consume() {
            //execJust()
            //execFromIterable()
            //execLambda()
            //execRange()
            //execInterval()
            //execFromCallable()
            //execCreate()
            execSubscribeOnObserveOn()
        }

        fun execJust() {
            producer.createJust()
                .subscribe(stringObserver)
        }

        fun execFromIterable() {
            producer.createFromIterable()
                .subscribe(stringObserver)
        }

        fun execLambda() {
            val disposable = producer.createFromIterable()
                .subscribe({ s ->
                    Timber.d("onNext: $s")
                }, { e ->
                    Timber.e(e)
                }, {
                    Timber.d("onComplete")
                })
        }

        fun execRange() {
            producer.createRange()
                .subscribe({ i ->
                    Timber.d("onNext: $i")
                }, { e ->
                    Timber.e(e)
                }, {
                    Timber.d("onComplete")
                })
        }

        fun execInterval() {
            val observer = object : Observer<Long> {
                var disposable: Disposable? = null
                override fun onComplete() {
                    Timber.d("onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    disposable = d

                }

                override fun onNext(l: Long) {
                    Timber.d("onNext: $l")
                    if (l > 10) {
                        disposable?.dispose()
                    }
                }

                override fun onError(e: Throwable?) {
                    Timber.e(e)
                }
            }
            producer.createInterval()
                .subscribe(observer)
        }


        fun execHotInterval() {
            val observer = object : Observer<Long> {
                var disposable: Disposable? = null
                override fun onComplete() {
                    Timber.d("onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    disposable = d

                }

                override fun onNext(l: Long) {
                    Timber.d("onNext: $l")
                    if (l > 10) {
                        disposable?.dispose()
                    }
                }

                override fun onError(e: Throwable?) {
                    Timber.e(e)
                }
            }

            val interval =  producer.createInterval()
            val hotInterval = interval.publish()
            hotInterval.connect()

            Thread.sleep(5000)

            interval.subscribe(observer)
        }

        fun execFromCallable(){
            producer.fromCallable()
                .subscribe({ s ->
                    Timber.d("onNext: $s")
                }, { e ->
                    Timber.e(e)
                }, {
                    Timber.d("onComplete")
                })
        }

        fun execCreate(){
            producer.create()
                .subscribe({ s ->
                    Timber.d("onNext: $s")
                }, { e ->
                    Timber.e(e)
                }, {
                    Timber.d("onComplete")
                })
        }

        fun execSubscribeOnObserveOn(){
            Timber.d("call subscription on: ${Thread.currentThread().name}")
            producer.fromCallable()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Timber.d("result on: ${Thread.currentThread().name}")
                    Timber.d("onNext: $it")
                }
        }
    }

}