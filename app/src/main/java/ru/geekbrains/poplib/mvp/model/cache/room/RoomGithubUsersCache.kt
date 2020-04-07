package ru.geekbrains.poplib.mvp.model.cache.room

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.geekbrains.poplib.mvp.model.cache.IGithubUsersCache
import ru.geekbrains.poplib.mvp.model.entity.GithubUser
import ru.geekbrains.poplib.mvp.model.entity.room.RoomGithubUser
import ru.geekbrains.poplib.mvp.model.entity.room.db.Database
import java.lang.RuntimeException

class RoomGithubUsersCache(val database: Database) : IGithubUsersCache {
    override fun getUser(username: String) = Single.fromCallable {
        return@fromCallable database.userDao.findByLogin(username)?.run { GithubUser(login, avatarUrl, reposUrl) }
            ?: throw RuntimeException("No such user in cache")
    }.subscribeOn(Schedulers.io())

    override fun putUser(user: GithubUser) = Completable.fromAction {
        val roomUser = database.userDao.findByLogin(user.login)?.apply {
            avatarUrl = user.avatarUrl
            reposUrl = user.reposUrl
        } ?: RoomGithubUser(user.login, user.avatarUrl, user.reposUrl)
        database.userDao.insert(roomUser)
    }.subscribeOn(Schedulers.io())
}