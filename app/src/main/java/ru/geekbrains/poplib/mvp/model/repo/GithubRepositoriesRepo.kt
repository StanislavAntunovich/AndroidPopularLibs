package ru.geekbrains.poplib.mvp.model.repo

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.geekbrains.poplib.mvp.model.api.IDataSource
import ru.geekbrains.poplib.mvp.model.entity.GithubRepository
import ru.geekbrains.poplib.mvp.model.entity.GithubUser
import ru.geekbrains.poplib.mvp.model.entity.room.RoomGithubRepository
import ru.geekbrains.poplib.mvp.model.entity.room.db.Database
import ru.geekbrains.poplib.mvp.model.network.NetworkStatus
import java.lang.RuntimeException

//TODO: Практическое задание 1 - вытащить кэширование в отдельный класс RoomRepositoriesCache и внедрить его сюда через интфейс IRepositoriesCache
class GithubRepositoriesRepo(val api: IDataSource, val networkStatus: NetworkStatus, val database: Database) {

    fun getUserRepositories(user: GithubUser) = networkStatus.isOnlineSingle().flatMap { isOnline ->
        if (isOnline) {
            api.getUserRepos(user.reposUrl)
                .map { repos ->
                    repos.takeIf { it.isNotEmpty() }?.let {
                        val roomRepos = repos.map { RoomGithubRepository(it.id, it.name, it.forksCount, user.login, it.language) }
                        database.repositoryDao.insert(roomRepos)
                    }
                    repos
                }
        } else {
            Single.create<List<GithubRepository>> { emitter ->
                database.userDao.findByLogin(user.login)?.let { roomUser ->
                    val roomRepos = database.repositoryDao.findForUser(user.login)
                    val repos = roomRepos.map { GithubRepository(it.id, it.name, it.forksCount, it.language ?: "") }
                    emitter.onSuccess(repos)
                } ?: let {
                    emitter.onError(RuntimeException("No such user in cache"))
                }
            }
        }
    }.subscribeOn(Schedulers.io())

}