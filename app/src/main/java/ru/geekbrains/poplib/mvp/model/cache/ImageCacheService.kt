package ru.geekbrains.poplib.mvp.model.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.geekbrains.poplib.mvp.model.entity.room.CahedImage
import ru.geekbrains.poplib.mvp.model.entity.room.db.Database
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.lang.RuntimeException

class ImageCacheService(private val database: Database, private val context: Context?) : IAvatarCache {
    override fun insertOrUpdate(url: String, data: ByteArray) =
        Completable.fromAction {
            val c = database.imagesDao.getCounter()
            var img = database.imagesDao.findByUrl(url)

            if (img == null) {
                val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                val dst = File(context!!.getExternalFilesDir(null), "$c.png")
                val stream = FileOutputStream(dst)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                bitmap.recycle()
                stream.close()
                img = CahedImage(url, dst.path)
            }
            database.imagesDao.insert(img)
        }.subscribeOn(Schedulers.io())


    override fun findByUrl(url: String) = Single.create<ByteArray> { emmiter ->
        val img = database.imagesDao.findByUrl(url)
        if (img == null) {
            emmiter.onError(RuntimeException("no image in cache"))
            return@create
        }
        val file = File(img.imagePath)
        emmiter.onSuccess(file.readBytes())
    }.subscribeOn(Schedulers.io())

}