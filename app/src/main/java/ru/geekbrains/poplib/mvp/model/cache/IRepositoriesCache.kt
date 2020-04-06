package ru.geekbrains.poplib.mvp.model.cache

import ru.geekbrains.poplib.mvp.model.entity.GithubRepository

interface IRepositoriesCache {
    fun insert(userName: String, repositories: List<GithubRepository>)
    fun findForUser(userName: String): List<GithubRepository>
}