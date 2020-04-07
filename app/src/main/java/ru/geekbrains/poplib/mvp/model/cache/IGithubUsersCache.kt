package ru.geekbrains.poplib.mvp.model.cache

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.geekbrains.poplib.mvp.model.entity.GithubUser


interface IGithubUsersCache {
    fun getUser(username: String): Single<GithubUser>
    fun putUser(user: GithubUser): Completable
}