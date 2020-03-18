package ru.geekbrains.poplib.mvp.model.entity

import java.io.Serializable

//https://api.github.com/users/googlesamples/repos
data class GithubRepository(
    val id: String,
    val name: String,
    val forksCount: Int
) : Serializable