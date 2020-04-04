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
        loadData()

        repositoryListPresenter.itemClickListener = { itemView ->
            val repository = repositoryListPresenter.repositories[itemView.pos]
            router.navigateTo(Screens.RepositoryScreen(repository))
        }
    }

    fun loadData() {
        usersRepo.getUser("googlesamples")
            .observeOn(mainThreadScheduler)
            .flatMap { user ->
                viewState.setUsername(user.login)
                viewState.loadAvatar(user.avatarUrl)
                return@flatMap repositoriesRepo.getUserRepositories(user)
            }
            .observeOn(mainThreadScheduler)
            .subscribe({ repos ->
                repositoryListPresenter.repositories.clear()
                repositoryListPresenter.repositories.addAll(repos)
                viewState.updateList()
            }, {
                Timber.e(it)
            })
    }

    fun backClicked(): Boolean {
        router.exit()
        return true
    }


}
