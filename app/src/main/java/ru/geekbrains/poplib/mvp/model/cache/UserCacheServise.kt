package ru.geekbrains.poplib.mvp.model.cache

import ru.geekbrains.poplib.mvp.model.entity.GithubUser
import ru.geekbrains.poplib.mvp.model.entity.room.RoomGithubUser
import ru.geekbrains.poplib.mvp.model.entity.room.db.Database

class UserCacheServise(private val database: Database) : IUserCache {
    override fun findByLogin(userName: String): GithubUser? =
        database.userDao.findByLogin(userName)?.let {
            GithubUser(it.login, it.avatarUrl, it.reposUrl)
        }


    override fun insertOrUpdate(user: GithubUser) {
        val roomUser = database.userDao.findByLogin(user.login)?.apply {
            avatarUrl = user.avatarUrl
            reposUrl = user.reposUrl
        } ?: RoomGithubUser(user.login, user.avatarUrl, user.reposUrl)

        database.userDao.insert(roomUser)
    }
}