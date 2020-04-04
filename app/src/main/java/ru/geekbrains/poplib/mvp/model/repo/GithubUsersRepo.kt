package ru.geekbrains.poplib.mvp.model.repo

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.geekbrains.poplib.mvp.model.api.IDataSource
import ru.geekbrains.poplib.mvp.model.entity.GithubUser
import ru.geekbrains.poplib.mvp.model.entity.room.RoomGithubUser
import ru.geekbrains.poplib.mvp.model.entity.room.db.Database
import ru.geekbrains.poplib.mvp.model.network.NetworkStatus
import java.lang.RuntimeException

//TODO: Практическое задание 1 - вытащить кэширование в отдельный класс RoomUserCache и внедрить его сюда через интфейс IUserCache
class GithubUsersRepo(val api: IDataSource, val networkStatus: NetworkStatus, val database: Database) {
    fun getUser(username: String) = networkStatus.isOnlineSingle().flatMap { isOnline ->
        if (isOnline) {
            api.getUser(username)
                .map { user ->
                    val roomUser = database.userDao.findByLogin(username)?.apply {
                        avatarUrl = user.avatarUrl
                        reposUrl = user.reposUrl
                    } ?: RoomGithubUser(user.login, user.avatarUrl, user.reposUrl)
                    database.userDao.insert(roomUser)
                    user
                }
        } else {
            Single.create<GithubUser> { emitter ->
                database.userDao.findByLogin(username)?.let { roomUser ->
                    emitter.onSuccess(GithubUser(roomUser.login, roomUser.avatarUrl, roomUser.reposUrl))
                } ?: let {
                    emitter.onError(RuntimeException("No such user in cache"))
                }
            }
        }
    }.subscribeOn(Schedulers.io())
}
