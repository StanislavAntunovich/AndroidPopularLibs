package ru.geekbrains.poplib.mvp.presenter

import moxy.InjectViewState
import moxy.MvpPresenter
import ru.geekbrains.poplib.mvp.model.entity.GithubRepository
import ru.geekbrains.poplib.mvp.view.RepositoryView
import ru.terrakok.cicerone.Router

@InjectViewState
class RepositoryPresenter(private val repository: GithubRepository?, private val router: Router) : MvpPresenter<RepositoryView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        if (repository != null)
            viewState.showRepository(repository)
        else
            viewState.showError()
    }

    fun backClicked(): Boolean {
        router.exit()
        return true
    }

}