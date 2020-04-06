package ru.geekbrains.poplib.ui.image

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import io.reactivex.rxjava3.core.Scheduler
import ru.geekbrains.poplib.mvp.model.cache.IAvatarCache
import ru.geekbrains.poplib.mvp.model.image.IImageLoader
import ru.geekbrains.poplib.mvp.model.network.NetworkStatus
import java.io.ByteArrayOutputStream

class GlideImageLoader(val networkStatus: NetworkStatus, val cacheService: IAvatarCache, val mainScheduler: Scheduler) : IImageLoader<ImageView> {
    override fun loadInto(url: String, container: ImageView) {
        //Проверять наличие сети
        networkStatus.isOnlineSingle().subscribe { isOnline: Boolean ->
            if (isOnline) {
                Glide.with(container.context)
                    .asBitmap()
                    .load(url)
                    //При отсутствии сети грузить с диска через ByteArray
                    .listener(object : RequestListener<Bitmap> {

                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Bitmap?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            //TODO: Практическое задание 2 - реализовать свой кэш картинок. Картинки хранить на диске, в room хранить CahedImage(url, localPath)
                            //Кэш реализовать в отдельном классе, внедрять сюда через интерфейс
                            val stream = ByteArrayOutputStream()
                            resource?.compress(Bitmap.CompressFormat.PNG, 100, stream)
                            cacheService.insertOrUpdate(url, stream.toByteArray()).subscribe()
                            return false
                        }
                    }).into(container)
            } else {
                cacheService.findByUrl(url)
                    .observeOn(mainScheduler)
                    .subscribe({
                        Glide.with(container.context)
                            .load(it)
                            .into(container)
                }, {})
            }
        }


    }
}