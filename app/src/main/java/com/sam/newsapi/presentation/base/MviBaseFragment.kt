package com.sam.newsapi.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class MviBaseFragment<I : MviIntent, S : MviViewState, VM : ViewModel>
    : Fragment(), MviView<I, S> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    abstract val clazz: Class<VM>


    val compositeBag = CompositeDisposable()

    val viewModel: VM by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(clazz)
    }

    var hasInitializedRootView = false
    private var rootView: View? = null

    fun getPersistentView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
        layout: Int
    ): View? {
        if (rootView == null) {
            rootView = inflater?.inflate(layout, container, false)
        } else {
            (rootView?.parent as? ViewGroup)?.removeView(rootView)
        }

        return rootView
    }

    override fun onDestroy() {
        compositeBag.dispose()
        super.onDestroy()
    }
}
