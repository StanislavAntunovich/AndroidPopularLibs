package ru.geekbrains.poplib.mvp.model.entity.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.geekbrains.poplib.mvp.model.entity.room.CahedImage

@Dao
interface ImagesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(image: CahedImage)

    @Query("SELECT * FROM Cahedimage WHERE url = :url LIMIT 1")
    fun findByUrl(url: String): CahedImage?

    @Query("SELECT count(url) FROM CahedImage")
    fun getCounter(): Int
}