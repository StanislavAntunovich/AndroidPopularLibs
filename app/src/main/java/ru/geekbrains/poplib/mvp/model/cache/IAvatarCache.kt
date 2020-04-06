package ru.geekbrains.poplib.mvp.model.cache

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface IAvatarCache {
    fun insertOrUpdate(url: String, data: ByteArray): Completable
    fun findByUrl(url: String): Single<ByteArray>
}