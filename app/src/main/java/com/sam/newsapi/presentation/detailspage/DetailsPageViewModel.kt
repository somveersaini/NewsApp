package com.sam.newsapi.presentation.detailspage


import androidx.lifecycle.ViewModel
import com.sam.newsapi.interactor.detailspage.DetailsPageAction
import com.sam.newsapi.interactor.detailspage.DetailsPageAction.*
import com.sam.newsapi.interactor.detailspage.DetailsPageActionProcessor
import com.sam.newsapi.interactor.detailspage.DetailsPageResult
import com.sam.newsapi.interactor.detailspage.DetailsPageResult.*
import com.sam.newsapi.presentation.base.MviViewModel
import com.sam.newsapi.presentation.detailspage.DetailsPageIntent.*
import com.sam.newsapi.util.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class DetailsPageViewModel @Inject constructor(
    private val actionProcessorHolder: DetailsPageActionProcessor
) : ViewModel(), MviViewModel<DetailsPageIntent, DetailsPageViewState, DetailsPageViewEffect> {

    private val intentsSubject: PublishSubject<DetailsPageIntent> = PublishSubject.create()
    private val effect: PublishSubject<DetailsPageViewEffect> = PublishSubject.create()
    private val disposables = CompositeDisposable()

    private val statesObservable by lazy { compose() }
    private val reducer by lazy { reducer() }


    override fun processIntents(intents: Observable<DetailsPageIntent>) {
        disposables.add(intents.subscribe(intentsSubject::onNext))
    }

    override fun states(): Observable<DetailsPageViewState> = statesObservable

    override fun effects(): Observable<DetailsPageViewEffect> = effect

    private fun compose(): Observable<DetailsPageViewState> {
        return intentsSubject.compose<DetailsPageIntent>(intentFilter)
            .map<DetailsPageAction>(this::actionFromIntent)
            .compose(actionProcessorHolder.actionProcessor)
            .doOnNext(::provisionEffect)
            .scan(DetailsPageViewState.idle(), reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private fun provisionEffect(result: DetailsPageResult) {
        effectFromResult(result)?.let(effect::onNext)
    }

    private fun effectFromResult(result: DetailsPageResult): DetailsPageViewEffect? {
        return when (result) {
            is OnNewsUrlClickResult.Success -> DetailsPageViewEffect.OpenChromeUrlEffect(result.url)
            else -> null
        }
    }

    private fun reducer() =
        BiFunction { previousState: DetailsPageViewState, result: DetailsPageResult ->
            when (result) {
                is DialogCancelResult -> previousState.copy(
                    error = null
                )
                is OnNewsUrlClickResult.Failure -> previousState.copy(
                    isLoading = false,
                    error = result.error
                )
                is LoadDetailsPageResult.Success -> previousState.copy(
                    isLoading = false,
                    article = result.article
                )
                is LoadDetailsPageResult.InFlight -> previousState.copy(
                    isLoading = true
                )
                else -> previousState
            }
        }

    private val intentFilter: ObservableTransformer<DetailsPageIntent, DetailsPageIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge<DetailsPageIntent>(
                    shared.ofType(InitialIntent::class.java).take(1),
                    shared.notOfType(InitialIntent::class.java)
                )
            }
        }


    private fun actionFromIntent(intent: DetailsPageIntent): DetailsPageAction {
        return when (intent) {
            is InitialIntent -> LoadDetailsPageAction(intent.article)
            is OnNewsUrlClickIntent -> OnNewsUrlClickAction(intent.url)
            ErrorDialogCancelIntent -> DialogCancelAction
        }
    }

    override fun onCleared() {
        disposables.dispose()
    }

}