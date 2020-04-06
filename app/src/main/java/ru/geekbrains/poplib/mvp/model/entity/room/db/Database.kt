package ru.geekbrains.poplib.mvp.model.entity.room.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.geekbrains.poplib.mvp.model.entity.room.*
import ru.geekbrains.poplib.mvp.model.entity.room.dao.ImagesDao
import ru.geekbrains.poplib.mvp.model.entity.room.dao.RepositoryDao
import ru.geekbrains.poplib.mvp.model.entity.room.dao.UserDao
import java.lang.RuntimeException

@androidx.room.Database(
    entities = [
        RoomGithubUser::class,
        RoomGithubRepository::class,
        CahedImage::class
    ],
    version = 3
)
abstract class Database : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val repositoryDao: RepositoryDao
    abstract val imagesDao: ImagesDao

    companion object {
        private const val DB_NAME = "database.db"

        @Volatile
        private var instance: Database? = null

        @Synchronized
        fun getInstance() = instance ?: throw RuntimeException("Database has not been created. Please call create(context)")

        fun create(context: Context) {
            instance ?: let {
                instance = Room.databaseBuilder(context, Database::class.java, DB_NAME)
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
            }
        }

    }
}