package com.sam.newsapi.presentation.detailspage

import com.sam.newsapi.presentation.base.MviViewEffect

sealed class DetailsPageViewEffect : MviViewEffect {
    data class OpenChromeUrlEffect(
        val url: String
    ) : DetailsPageViewEffect()
}