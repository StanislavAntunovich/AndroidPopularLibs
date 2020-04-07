package ru.geekbrains.poplib.mvp.model.entity.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE RoomGithubRepository ADD COLUMN language TEXT")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE RoomCachedImage (url TEXT NOT NULL, localPath TEXT NOT NULL, PRIMARY KEY(url))")
    }
}