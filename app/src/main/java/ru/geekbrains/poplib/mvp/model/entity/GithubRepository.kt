package ru.geekbrains.poplib.mvp.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//https://api.github.com/users/googlesamples/repos
@Parcelize
data class GithubRepository(
    val id: String,
    val name: String,
    val forksCount: Int
) : Parcelable