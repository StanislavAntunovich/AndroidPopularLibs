package ru.geekbrains.poplib.di.modules

import dagger.Module
import dagger.Provides
import ru.geekbrains.poplib.mvp.model.api.IDataSource
import ru.geekbrains.poplib.mvp.model.cache.IGithubRepositoriesCache
import ru.geekbrains.poplib.mvp.model.cache.IGithubUsersCache
import ru.geekbrains.poplib.mvp.model.network.NetworkStatus
import ru.geekbrains.poplib.mvp.model.repo.GithubRepositoriesRepo
import ru.geekbrains.poplib.mvp.model.repo.GithubUsersRepo
import javax.inject.Singleton

@Module(
    includes = [
        CacheModule::class,
        ApiModule::class
    ]
)
open class RepoModule {

    @Singleton
    @Provides
    open fun usersRepo(api: IDataSource, networkStatus: NetworkStatus, cache: IGithubUsersCache): GithubUsersRepo {
        return GithubUsersRepo(api, networkStatus, cache)
    }

    @Singleton
    @Provides
    fun repositoriesRepo(api: IDataSource, networkStatus: NetworkStatus, cache: IGithubRepositoriesCache): GithubRepositoriesRepo {
        return GithubRepositoriesRepo(api, networkStatus, cache)
    }


}
//Перерыв 10 минут