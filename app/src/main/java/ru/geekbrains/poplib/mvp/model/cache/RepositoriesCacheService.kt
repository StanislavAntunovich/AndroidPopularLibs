package ru.geekbrains.poplib.mvp.model.cache

import ru.geekbrains.poplib.mvp.model.entity.GithubRepository
import ru.geekbrains.poplib.mvp.model.entity.room.RoomGithubRepository
import ru.geekbrains.poplib.mvp.model.entity.room.db.Database

class RepositoriesCacheService(private val database: Database) : IRepositoriesCache {

    override fun insert(userName: String, repositories: List<GithubRepository>) {
        val roomRepos = repositories.map { RoomGithubRepository(it.id, it.name, it.forksCount, userName, it.language) }
        database.repositoryDao.insert(roomRepos)
    }

    override fun findForUser(userName: String): List<GithubRepository> =
        database.userDao.findByLogin(userName)?.let { user ->
            val roomRepos = database.repositoryDao.findForUser(user.login)
            roomRepos.map { GithubRepository(it.id, it.name, it.forksCount, it.language ?: "") }
    } ?: emptyList()

}