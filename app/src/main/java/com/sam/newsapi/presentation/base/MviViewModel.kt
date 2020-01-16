package com.sam.newsapi.presentation.base

import io.reactivex.Observable

interface MviViewModel<I : MviIntent, S : MviViewState, E : MviViewEffect> {
    fun processIntents(intents: Observable<I>)

    fun states(): Observable<S>

    fun effects(): Observable<E>
}
