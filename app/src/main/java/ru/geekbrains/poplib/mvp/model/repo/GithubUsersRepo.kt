package ru.geekbrains.poplib.mvp.model.repo

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.geekbrains.poplib.mvp.model.api.IDataSource
import ru.geekbrains.poplib.mvp.model.cache.IUserCache
import ru.geekbrains.poplib.mvp.model.entity.GithubUser
import ru.geekbrains.poplib.mvp.model.network.NetworkStatus
import java.lang.RuntimeException

//TODO: Практическое задание 1 - вытащить кэширование в отдельный класс RoomUserCache и внедрить его сюда через интфейс IUserCache
class GithubUsersRepo(val api: IDataSource, val networkStatus: NetworkStatus, val cacheService: IUserCache) {
    fun getUser(username: String) = networkStatus.isOnlineSingle().flatMap { isOnline ->
        if (isOnline) {
            api.getUser(username)
                .map { user ->
                    cacheService.insertOrUpdate(user)
                    user
                }
        } else {
            Single.create<GithubUser> { emitter ->
                cacheService.findByLogin(username)?.let { user ->
                    emitter.onSuccess(user)
                } ?: let {
//                    emitter.onError(RuntimeException("No such user in cache"))
                }
            }
        }
    }.subscribeOn(Schedulers.io())
}
