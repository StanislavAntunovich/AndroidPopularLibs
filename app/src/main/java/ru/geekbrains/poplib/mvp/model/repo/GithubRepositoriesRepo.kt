package ru.geekbrains.poplib.mvp.model.repo

import io.reactivex.rxjava3.core.Observable
import ru.geekbrains.poplib.mvp.model.entity.GithubRepository

class GithubRepositoriesRepo {

    val repositories = listOf(
        GithubRepository("1", "name1", 100),
        GithubRepository("2", "name2", 200),
        GithubRepository("3", "name3", 300),
        GithubRepository("4", "name4", 400)
    )

    //Сделать так, чтобы getRepos возвращал Observable и не через just
    fun getRepos() = Observable.fromIterable(repositories)
}