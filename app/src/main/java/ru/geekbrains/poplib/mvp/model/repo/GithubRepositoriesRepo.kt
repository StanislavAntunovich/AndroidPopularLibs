package ru.geekbrains.poplib.mvp.model.repo

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.geekbrains.poplib.mvp.model.api.IDataSource
import ru.geekbrains.poplib.mvp.model.cache.IRepositoriesCache
import ru.geekbrains.poplib.mvp.model.cache.IUserCache
import ru.geekbrains.poplib.mvp.model.entity.GithubRepository
import ru.geekbrains.poplib.mvp.model.entity.GithubUser
import ru.geekbrains.poplib.mvp.model.entity.room.RoomGithubRepository
import ru.geekbrains.poplib.mvp.model.network.NetworkStatus
import java.lang.RuntimeException

//TODO: Практическое задание 1 - вытащить кэширование в отдельный класс RoomRepositoriesCache и внедрить его сюда через интфейс IRepositoriesCache
class GithubRepositoriesRepo(val api: IDataSource, val networkStatus: NetworkStatus, val userService: IUserCache, val reposService: IRepositoriesCache) {

    fun getUserRepositories(user: GithubUser) = networkStatus.isOnlineSingle().flatMap { isOnline ->
        if (isOnline) {
            api.getUserRepos(user.reposUrl)
                .map { repos ->
                    repos.takeIf { it.isNotEmpty() }?.let {
                        reposService.insert(user.login, it)
                    }
                    repos
                }
        } else {
            Single.create<List<GithubRepository>> { emitter ->
                userService.findByLogin(user.login)?.let {
                    val repos = reposService.findForUser(user.login)
                    emitter.onSuccess(repos)
                } ?: let {
//                    emitter.onError(RuntimeException("No such user in cache"))
                }
            }
        }
    }.subscribeOn(Schedulers.io())

}