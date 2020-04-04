package ru.geekbrains.poplib.mvp.model.repo

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.geekbrains.poplib.mvp.model.api.IDataSource
import ru.geekbrains.poplib.mvp.model.entity.GithubRepository

class GithubRepositoriesRepo(private val api: IDataSource) {

    fun getRepos(url: String) = api.getRepos(url).subscribeOn(Schedulers.io())
}