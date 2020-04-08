package ru.geekbrains.poplib.di.modules


import dagger.Module
import dagger.Provides
import ru.geekbrains.poplib.mvp.model.cache.image.IImageCache
import ru.geekbrains.poplib.mvp.model.cache.image.room.RoomImageCache
import ru.geekbrains.poplib.mvp.model.entity.room.db.Database
import ru.geekbrains.poplib.mvp.model.network.NetworkStatus
import ru.geekbrains.poplib.ui.App
import ru.geekbrains.poplib.ui.image.GlideImageLoader
import ru.geekbrains.poplib.ui.network.AndroidNetworkStatus
import java.io.File
import javax.inject.Named

//Реализовать модуль и внедрение всего, что касается картинок. Кэш тоже сюда.
@Module
class ImageModule {

    @Named("basePath")
    @Provides
    fun basePath(app: App): String {
        val filesDir = app.externalCacheDir?.path
            ?: App.instance.getExternalFilesDir(null)?.path
            ?: App.instance.filesDir.path
        return filesDir + File.separator + "image_cache"
    }

    @Provides
    fun file(@Named("basePath") path: String): File {
        return File(path)
    }

    @Provides
    fun imageCache(database: Database, file: File): IImageCache {
        return RoomImageCache(database, file)
    }

    @Provides
    fun imageLoader(cache: IImageCache, networkStatus: NetworkStatus): GlideImageLoader {
        return GlideImageLoader(cache, networkStatus)
    }

}