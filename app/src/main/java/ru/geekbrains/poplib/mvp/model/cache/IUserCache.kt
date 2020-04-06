package ru.geekbrains.poplib.mvp.model.cache

import ru.geekbrains.poplib.mvp.model.entity.GithubUser

interface IUserCache {
    fun findByLogin(userName: String): GithubUser?
    fun insertOrUpdate(user: GithubUser)
}