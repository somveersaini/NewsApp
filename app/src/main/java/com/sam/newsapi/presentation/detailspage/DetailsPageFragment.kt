package com.sam.newsapi.presentation.detailspage

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.sam.newsapi.R
import com.sam.newsapi.data.newsapi.model.Article
import com.sam.newsapi.databinding.FragmentDetailsPageBinding
import com.sam.newsapi.presentation.base.MviBaseFragment
import com.sam.newsapi.util.doOn
import com.sam.newsapi.util.safeDistinctWith
import com.sam.newsapi.util.safeWith
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class DetailsPageFragment :
    MviBaseFragment<DetailsPageIntent, DetailsPageViewState, DetailsPageViewModel>() {

    override val clazz: Class<DetailsPageViewModel> = DetailsPageViewModel::class.java
    private val intents = PublishSubject.create<DetailsPageIntent>()

    private val args by navArgs<DetailsPageFragmentArgs>()
    private var viewState: DetailsPageViewState? = null


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
            R.layout.fragment_details_page
        )
    }

    private var binding: FragmentDetailsPageBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)
        if (!hasInitializedRootView) {
            hasInitializedRootView = true
            bind()
        }
    }

    private fun bind() {
        compositeBag.add(viewModel.states().subscribe(this::render))
        compositeBag.add(viewModel.effects().subscribe(this::handleViewEffects))
        viewModel.processIntents(intents())
    }

    override fun intents(): Observable<DetailsPageIntent> {
        return Observable.merge(
            Observable.just(DetailsPageIntent.InitialIntent(args.article)),
            intents
        )
    }


    override fun render(state: DetailsPageViewState) {
        state.isLoading.doOn(
            { showLoader() },
            { hideLoader() }
        )

        safeDistinctWith(state.article, viewState?.article) {
            updateView(it)
        }

        safeWith(state.error) {
            showError(it)
        }
        viewState = state
    }

    private fun updateView(articleItem: Article) {
        binding?.article = articleItem
        binding?.link?.setOnClickListener {
            intents.onNext(DetailsPageIntent.OnNewsUrlClickIntent(articleItem.url))
        }
    }

    private fun handleViewEffects(effect: DetailsPageViewEffect) {
        when (effect) {
            is DetailsPageViewEffect.OpenChromeUrlEffect -> openChrome(effect.url)
        }
    }

    private fun openChrome(urlString: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setPackage("com.android.chrome")
        try {
            context?.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            // Chrome browser presumably not installed so allow user to choose instead
            intent.setPackage(null)
            context?.startActivity(intent)
        }
    }

    private fun showError(error: Throwable) {
        val errorMessage = error.message ?: getString(R.string.generic_error_message)
        val alertDialog = AlertDialog.Builder(this.requireContext())
            .setTitle(R.string.error_title)
            .setMessage(errorMessage)
            .setNegativeButton(R.string.cancel) { _, _ -> intents.onNext(DetailsPageIntent.ErrorDialogCancelIntent) }
            .create()
        alertDialog.show()
    }

    private fun hideLoader() {}
    private fun showLoader() {}
}