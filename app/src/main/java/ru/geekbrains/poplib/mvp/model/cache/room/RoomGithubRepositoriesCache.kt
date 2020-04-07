package ru.geekbrains.poplib.mvp.model.cache.room

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.geekbrains.poplib.mvp.model.cache.IGithubRepositoriesCache
import ru.geekbrains.poplib.mvp.model.entity.GithubRepository
import ru.geekbrains.poplib.mvp.model.entity.GithubUser
import ru.geekbrains.poplib.mvp.model.entity.room.RoomGithubRepository
import ru.geekbrains.poplib.mvp.model.entity.room.db.Database

class RoomGithubRepositoriesCache(val database: Database) : IGithubRepositoriesCache {
    override fun getUserRepos(user: GithubUser) = Single.fromCallable {
        val roomUser = database.userDao.findByLogin(user.login) ?: throw RuntimeException("No such user in cache")

        return@fromCallable database.repositoryDao.findForUser(roomUser.login)
            .map { GithubRepository(it.id, it.name, it.forksCount, it.language ?: "") }

    }.subscribeOn(Schedulers.io())

    override fun putUserRepos(user: GithubUser, repos: List<GithubRepository>) = Completable.fromAction {
        val roomUser = database.userDao.findByLogin(user.login) ?: throw RuntimeException("No such user in cache")
        val roomRepos = repos.map { RoomGithubRepository(it.id, it.name, it.forksCount, roomUser.login, it.language) }
        database.repositoryDao.insert(roomRepos)
    }.subscribeOn(Schedulers.io())
}