package ru.geekbrains.poplib.ui

import android.app.Application
import ru.geekbrains.poplib.di.AppComponent
import ru.geekbrains.poplib.di.DaggerAppComponent
import ru.geekbrains.poplib.di.modules.AppModule
import timber.log.Timber

class App : Application() {

    companion object {
        lateinit var instance: App
    }

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        Timber.plant(Timber.DebugTree())

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

}