package ru.geekbrains.poplib.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.geekbrains.poplib.R
import ru.geekbrains.poplib.mvp.model.CountersModel
import ru.geekbrains.poplib.mvp.presenter.MainPresenter
import ru.geekbrains.poplib.mvp.view.MainView

class MainActivity : AppCompatActivity(), MainView {

    val presenter = MainPresenter(this, CountersModel())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_counter1.setOnClickListener {
            presenter.counter1Click()
        }
        btn_counter2.setOnClickListener {
            presenter.counter2Click()
        }
        btn_counter3.setOnClickListener {
            presenter.counter3Click()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //outState.putIntArray("counters", counters.toIntArray())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
//        savedInstanceState.getIntArray("counters")?.toList()?.let {
//            counters.clear()
//            counters.addAll(it)
//        }
    }

    private fun setButtonText(button: Button, text: String) {
        button.text = text
    }


    override fun setTextCounter1(text: String) {
        setButtonText(btn_counter1, text)
    }

    override fun setTextCounter2(text: String) {
        setButtonText(btn_counter2, text)
    }

    override fun setTextCounter3(text: String) {
        setButtonText(btn_counter3, text)
    }

}
