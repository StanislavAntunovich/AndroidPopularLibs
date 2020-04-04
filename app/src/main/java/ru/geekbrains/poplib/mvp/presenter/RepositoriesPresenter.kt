package ru.geekbrains.poplib.mvp.presenter

import io.reactivex.rxjava3.core.Scheduler
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.geekbrains.poplib.mvp.model.entity.GithubRepository
import ru.geekbrains.poplib.mvp.model.repo.GithubRepositoriesRepo
import ru.geekbrains.poplib.mvp.model.repo.GithubUsersRepo
import ru.geekbrains.poplib.mvp.presenter.list.IRepositoryListPresenter
import ru.geekbrains.poplib.mvp.view.RepositoriesView
import ru.geekbrains.poplib.mvp.view.list.RepositoryItemView
import ru.geekbrains.poplib.navigation.Screens
import ru.terrakok.cicerone.Router
import timber.log.Timber

@InjectViewState
class RepositoriesPresenter(
    val mainThreadScheduler: Scheduler,
    val router: Router,
    val repositoriesRepo: GithubRepositoriesRepo,
    val usersRepo: GithubUsersRepo
) :
    MvpPresenter<RepositoriesView>() {


    class RepositoryListPresenter : IRepositoryListPresenter {
        val repositories = mutableListOf<GithubRepository>()
        override var itemClickListener: ((RepositoryItemView) -> Unit)? = null

        override fun getCount() = repositories.size

        override fun bindView(view: RepositoryItemView) {
            val repository = repositories[view.pos]
            view.setTitle(repository.name)
        }
    }

    val repositoryListPresenter = RepositoryListPresenter()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()

        repositoryListPresenter.itemClickListener = { itemView ->
            val repository = repositoryListPresenter.repositories[itemView.pos]
            router.navigateTo(Screens.RepositoryScreen(repository))
        }
    }

    fun searchBtnClicked(name: String) {
        if (name.isBlank() || name.isEmpty()) {
            viewState.showMessage("Please fill user name")
            return
        }
        loadUserData(name)
    }

    fun loadUserData(name: String) {
        clearData()
        viewState.clearSearch()
        usersRepo.getUser(name)
            .observeOn(mainThreadScheduler)
            .flatMap { user ->
                viewState.setUsername(user.login)
                viewState.loadAvatar(user.avatarUrl)
                return@flatMap repositoriesRepo.getRepos(user.reposUrl)
            }
            .observeOn(mainThreadScheduler)
            .subscribe({ repos ->
                repositoryListPresenter.repositories.clear()
                repositoryListPresenter.repositories.addAll(repos)
                viewState.updateList()
            }, {
                it.localizedMessage?.let { message ->
                    viewState.showMessage(message)
                }
                Timber.e(it)
            })
    }

    private fun clearData() {
        repositoryListPresenter.repositories.clear()
        viewState.updateList()
        viewState.setUsername("")
        viewState.loadAvatar("")

    }

    fun backClicked(): Boolean {
        router.exit()
        return true
    }


}
