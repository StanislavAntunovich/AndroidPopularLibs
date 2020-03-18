package ru.geekbrains.poplib.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.geekbrains.poplib.mvp.model.entity.GithubRepository

@StateStrategyType(AddToEndSingleStrategy::class)
interface RepositoryView : MvpView {
    fun showRepository(repository: GithubRepository)
    fun showError()
}