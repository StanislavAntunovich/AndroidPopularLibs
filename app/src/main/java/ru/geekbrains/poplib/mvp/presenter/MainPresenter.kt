package ru.geekbrains.poplib.mvp.presenter

import moxy.InjectViewState
import moxy.MvpPresenter
import ru.geekbrains.poplib.mvp.view.MainView
import ru.geekbrains.poplib.navigation.Screens
import ru.geekbrains.poplib.rxlearning.Sources
import ru.terrakok.cicerone.Router

@InjectViewState
class MainPresenter(val router: Router) : MvpPresenter<MainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        router.replaceScreen(Screens.RepositoriesScreen())

        //Creation().exec()
        //Operator().exec()
        Sources().exec()
    }

    fun backClicked() {
        router.exit()
    }

}
