package ru.geekbrains.poplib.di.modules

import dagger.Module
import dagger.Provides
import ru.geekbrains.poplib.mvp.model.cache.IGithubRepositoriesCache
import ru.geekbrains.poplib.mvp.model.cache.IGithubUsersCache
import ru.geekbrains.poplib.mvp.model.cache.room.RoomGithubRepositoriesCache
import ru.geekbrains.poplib.mvp.model.cache.room.RoomGithubUsersCache
import ru.geekbrains.poplib.mvp.model.entity.room.db.Database

@Module
class CacheModule {

    @Provides
    fun usersCache(database: Database): IGithubUsersCache {
        return RoomGithubUsersCache(database)
    }

    @Provides
    fun repositoriesCache(database: Database): IGithubRepositoriesCache {
        return RoomGithubRepositoriesCache(database)
    }

}