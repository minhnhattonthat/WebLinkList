package com.nathan.app.weblinklist.utils;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public final class BindingHelper {
    @BindingAdapter("avatarUrl")
    public static void avatarUrl(ImageView imageView, String url) {
        Glide.with(imageView)
                .load(url)
                .apply(new RequestOptions()
                        .centerCrop().circleCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(imageView);
    }
}
