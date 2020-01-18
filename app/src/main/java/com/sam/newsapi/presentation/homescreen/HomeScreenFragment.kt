package com.sam.newsapi.presentation.homescreen

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.sam.newsapi.R
import com.sam.newsapi.data.newsapi.model.Article
import com.sam.newsapi.data.newsapi.model.Category
import com.sam.newsapi.presentation.base.MviBaseFragment
import com.sam.newsapi.presentation.homescreen.HomeScreenIntent.*
import com.sam.newsapi.presentation.homescreen.HomeScreenViewEffect.OpenDetailsPageEffect
import com.sam.newsapi.presentation.homescreen.HomeScreenViewEffect.RefreshEffect
import com.sam.newsapi.presentation.homescreen.adapter.ArticleListAdapter
import com.sam.newsapi.presentation.homescreen.adapter.CategoryListAdapter
import com.sam.newsapi.util.doOn
import com.sam.newsapi.util.safeWith
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_home_screen.*
import javax.inject.Inject


class HomeScreenFragment :
    MviBaseFragment<HomeScreenIntent, HomeScreenViewState, HomeScreenViewModel>() {

    override val clazz: Class<HomeScreenViewModel> = HomeScreenViewModel::class.java
    private val intents = PublishSubject.create<HomeScreenIntent>()

    @Inject
    lateinit var articleLisAdapter: ArticleListAdapter

    @Inject
    lateinit var categoryListAdapter: CategoryListAdapter

    private lateinit var viewState: HomeScreenViewState

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getPersistentView(
            inflater,
            container,
            savedInstanceState,
            R.layout.fragment_home_screen
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!hasInitializedRootView) {
            hasInitializedRootView = true
            setupView()
            bind()
        }
        RxJavaPlugins.setErrorHandler {}
    }

    private fun setupView() {
        swiperefresh.setOnRefreshListener { refresh() }

        articleList.layoutManager = LinearLayoutManager(this.requireContext())
        (articleList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        articleList.adapter = articleLisAdapter

        categoryList.layoutManager =
            LinearLayoutManager(this.requireContext(), LinearLayoutManager.HORIZONTAL, false)
        (categoryList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        categoryList.adapter = categoryListAdapter
    }

    private fun refresh() {
        viewState.categoryList?.let { intents.onNext(RefreshIntent(it)) }
    }

    private fun bind() {
        compositeBag.add(viewModel.states().subscribe(this::render))
        compositeBag.add(viewModel.effects().subscribe(this::handleViewEffects))
        viewModel.processIntents(intents())
    }

    override fun intents(): Observable<HomeScreenIntent> {
        return Observable.merge(
            Observable.just(InitialIntent),
            categoryListAdapter.categoryClickEvent,
            articleLisAdapter.articleClickEvent,
            intents
        )
    }


    override fun render(state: HomeScreenViewState) {
        viewState = state
        state.isRefreshing.doOn(
            { showLoader() },
            { hideLoader() }
        )

        safeWith(state.articleList) {
            updateNewsListList(it)
        }

        safeWith(state.categoryList) {
            updateSectionList(it)
        }
        safeWith(state.error) {
            showError(it)
        }
    }

    private fun updateNewsListList(data: List<Article>) = articleLisAdapter.setData(data)
    private fun updateSectionList(data: List<Category>) = categoryListAdapter.setData(data)

    private fun handleViewEffects(effect: HomeScreenViewEffect) {
        when (effect) {
            is RefreshEffect -> intents.onNext(RefreshIntent(effect.categoryList))
            is OpenDetailsPageEffect -> openDetailsPage(effect.article)
        }
    }

    private fun openDetailsPage(article: Article) {
        val action = HomeScreenFragmentDirections.actionHomeScreenToDetailsPage(article)
        findNavController(this).navigate(action)
    }

    private fun showError(error: Throwable) {
        val errorMessage = error.message ?: getString(R.string.generic_error_message)
        val alertDialog = AlertDialog.Builder(this.requireContext())
            .setTitle(R.string.error_title)
            .setMessage(errorMessage)
            .setNegativeButton(R.string.cancel) { _, _ -> intents.onNext(ErrorDialogCancelIntent) }
            .setPositiveButton(R.string.retry) { _, _ ->
                viewState.categoryList?.let {
                    intents.onNext(RefreshIntent(it))
                }
            }
            .create()
        alertDialog.show()
    }

    private fun hideLoader() {
        swiperefresh.isRefreshing = false
    }

    private fun showLoader() {
        if (!swiperefresh.isRefreshing) swiperefresh.isRefreshing = true
    }

}