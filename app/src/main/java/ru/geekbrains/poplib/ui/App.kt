package ru.geekbrains.poplib.ui

import android.app.Application
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

class App : Application() {
    companion object {
        lateinit var instance: App
    }

    private val cicerone: Cicerone<Router> by lazy {
        Cicerone.create()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    val navigatorHolder: NavigatorHolder
        get() = cicerone.navigatorHolder
    val router: Router
        get() = cicerone.router

}