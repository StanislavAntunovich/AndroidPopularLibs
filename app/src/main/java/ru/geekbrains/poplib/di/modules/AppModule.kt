package ru.geekbrains.poplib.di.modules

import dagger.Module
import dagger.Provides
import ru.geekbrains.poplib.ui.App

@Module
class AppModule(val app: App) {

    @Provides
    fun app(): App {
        return app
    }

}