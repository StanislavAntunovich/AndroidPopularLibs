package ru.geekbrains.poplib.di

import dagger.Component
import ru.geekbrains.poplib.di.modules.*
import ru.geekbrains.poplib.mvp.presenter.MainPresenter
import ru.geekbrains.poplib.mvp.presenter.RepositoriesPresenter
import ru.geekbrains.poplib.mvp.presenter.RepositoryPresenter
import ru.geekbrains.poplib.ui.activity.MainActivity
import ru.geekbrains.poplib.ui.fragment.RepositoriesFragment
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        RepoModule::class,
        CiceroneModule::class,
        DataModule::class,
        ImageModule::class
    ]
)
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(mainPresenter: MainPresenter)
    fun inject(repositoriesPresenter: RepositoriesPresenter)
    fun inject(repositoriesFragment: RepositoriesFragment)
    fun inject(repositoryPresenter: RepositoryPresenter)
}