package ru.geekbrains.poplib.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_repositories.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.geekbrains.poplib.R
import ru.geekbrains.poplib.mvp.model.repo.GithubRepositoriesRepo
import ru.geekbrains.poplib.mvp.presenter.RepositoriesPresenter
import ru.geekbrains.poplib.mvp.view.RepositoriesView
import ru.geekbrains.poplib.ui.App
import ru.geekbrains.poplib.ui.BackButtonListener
import ru.geekbrains.poplib.ui.adapter.RepositoriesRVAdapter

class RepositoriesFragment : MvpAppCompatFragment(), RepositoriesView, BackButtonListener {

    companion object {
        fun newInstance() = RepositoriesFragment()
    }

    @InjectPresenter
    lateinit var presenter: RepositoriesPresenter

    var adapter: RepositoriesRVAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        View.inflate(context, R.layout.fragment_repositories, null)

    @ProvidePresenter
    fun providePresenter() = RepositoriesPresenter(GithubRepositoriesRepo(), App.instance.router)

    override fun init() {
        rv_repos.layoutManager = LinearLayoutManager(context)
        adapter = RepositoriesRVAdapter(presenter.repositoryListPresenter)
        rv_repos.adapter = adapter
    }

    override fun updateList() {
        adapter?.notifyDataSetChanged()
    }

    override fun backClicked() = presenter.backClicked()
}