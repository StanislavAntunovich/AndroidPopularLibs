package ru.geekbrains.poplib.mvp.presenter

import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.geekbrains.poplib.mvp.model.entity.GithubRepository
import ru.geekbrains.poplib.mvp.model.repo.GithubRepositoriesRepo
import ru.geekbrains.poplib.mvp.presenter.list.IRepositoryListPresenter
import ru.geekbrains.poplib.mvp.view.RepositoriesView
import ru.geekbrains.poplib.mvp.view.list.RepositoryItemView
import ru.geekbrains.poplib.navigation.Screens
import ru.terrakok.cicerone.Router
import timber.log.Timber

@InjectViewState
class RepositoriesPresenter(val mainScheduler: Scheduler, val repositoriesRepo: GithubRepositoriesRepo, val router: Router) : MvpPresenter<RepositoriesView>() {

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
    var reposDisposable: Disposable? = null
    val repositoriesObserver = object : Observer<GithubRepository> {
        override fun onComplete() {
            viewState.updateList()
        }

        override fun onSubscribe(d: Disposable?) {
            repositoryListPresenter.repositories.clear()
            reposDisposable = d
        }

        override fun onNext(t: GithubRepository) {
            repositoryListPresenter.repositories.add(t)
        }

        override fun onError(e: Throwable?) {

        }

    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()

        repositoryListPresenter.itemClickListener = { itemView ->
            val repository = repositoryListPresenter.repositories[itemView.pos]
            router.navigateTo(Screens.RepositoryScreen(repository))
        }
    }

    fun onPaused() {
        reposDisposable?.dispose()
    }

    fun onResume() {
        loadRepos()
    }

    fun loadRepos() {
        repositoriesRepo.getRepos()
            .subscribeOn(Schedulers.io())
            .observeOn(mainScheduler)
            .subscribe(repositoriesObserver)
    }

    fun backClicked() : Boolean {
        router.exit()
        return true
    }
}
