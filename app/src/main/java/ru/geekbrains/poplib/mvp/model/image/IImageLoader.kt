package ru.geekbrains.poplib.mvp.model.image

import ru.geekbrains.poplib.mvp.model.cache.image.IImageCache

interface IImageLoader<T> {
    val cache: IImageCache
    fun loadInto(url: String, container: T)
}