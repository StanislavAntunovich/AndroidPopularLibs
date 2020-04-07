package ru.geekbrains.poplib.di

import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Versioned(val version: Int = 0)