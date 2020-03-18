package ru.geekbrains.poplib.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_repository.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.geekbrains.poplib.R
import ru.geekbrains.poplib.mvp.model.entity.GithubRepository
import ru.geekbrains.poplib.mvp.presenter.RepositoryPresenter
import ru.geekbrains.poplib.mvp.view.RepositoryView
import ru.geekbrains.poplib.ui.App
import ru.geekbrains.poplib.ui.BackButtonListener

class RepositoryFragment : MvpAppCompatFragment(), RepositoryView, BackButtonListener {

    @InjectPresenter
    lateinit var presenter: RepositoryPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return View.inflate(context, R.layout.fragment_repository, null)
    }


    override fun backClicked() = presenter.backClicked()

    override fun showRepository(repository: GithubRepository) {
        tv_error.visibility = View.GONE
        ll_info_container.visibility = View.VISIBLE

        tv_repo_id.text = repository.id
        tv_repo_name.text = repository.name
        tv_forks_count.text = repository.forksCount.toString()

    }

    override fun showError() {
        ll_info_container.visibility = View.INVISIBLE
        tv_error.visibility = View.VISIBLE
    }

    @ProvidePresenter
    fun providePresenter(): RepositoryPresenter {
        val repo = arguments?.getSerializable(REPOSITORY_KEY) as? GithubRepository

        return RepositoryPresenter(repo, App.instance.router)

    }

    companion object {
        private const val REPOSITORY_KEY = "repository"

        fun newInstance(repository: GithubRepository): RepositoryFragment {
            val bundle = Bundle()
            bundle.putSerializable(REPOSITORY_KEY, repository)

            val fragment = RepositoryFragment()
            fragment.arguments = bundle

            return fragment
        }
    }

}