package ru.geekbrains.poplib.di.modules

import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.geekbrains.poplib.mvp.model.entity.room.MIGRATION_1_2
import ru.geekbrains.poplib.mvp.model.entity.room.MIGRATION_2_3
import ru.geekbrains.poplib.mvp.model.entity.room.db.Database
import ru.geekbrains.poplib.ui.App
import javax.inject.Singleton

@Module
class DataModule {
    @Singleton
    @Provides
    fun database(app: App): Database {
        return Room.databaseBuilder(app, Database::class.java, Database.DB_NAME)
            .addMigrations(MIGRATION_1_2)
            .addMigrations(MIGRATION_2_3)
            .build()
    }
}