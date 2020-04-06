package ru.geekbrains.poplib.mvp.model.entity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CahedImage(
    @PrimaryKey val url: String,
    val imagePath: String
)