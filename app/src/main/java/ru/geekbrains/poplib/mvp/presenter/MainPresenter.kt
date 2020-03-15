package ru.geekbrains.poplib.mvp.presenter

import ru.geekbrains.poplib.mvp.model.CountersModel
import ru.geekbrains.poplib.mvp.view.MainView

class MainPresenter(val view: MainView, val model: CountersModel) {

    private fun getValue(index: Int): String {
        val value = model.next(index)
        return value.toString()
    }

    fun counter1Click() {
        val value = getValue(0)
        view.setTextCounter1(value)
    }

    fun counter2Click() {
        val value = getValue(1)
        view.setTextCounter2(value)

    }

    fun counter3Click() {
        val value = getValue(2)
        view.setTextCounter3(value)

    }


}