package com.sam.newsapi.binding

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions


object BindingAdapters {

    @JvmStatic
    @BindingAdapter("goneUnless")
    fun goneUnless(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }


    @JvmStatic
    @BindingAdapter("urlToImage", "cornerRadius", requireAll = false)
    fun loadImage(
        imageView: ImageView,
        urlToImage: String?,
        cornerRadius: Float?
    ) {
        if (urlToImage != null) {
            val requestOptions = RequestOptions().centerCrop()
            if (cornerRadius != null) requestOptions.transform(
                RoundedCorners(cornerRadius.toInt())
            )
            Glide.with(imageView)
                .load(urlToImage)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
        }
    }

}