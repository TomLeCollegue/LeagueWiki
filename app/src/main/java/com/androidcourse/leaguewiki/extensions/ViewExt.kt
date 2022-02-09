package com.androidcourse.leaguewiki.extensions

import android.util.TypedValue
import android.view.View
import com.androidcourse.leaguewiki.R

fun View.addRipple(): Unit = with(TypedValue()) {
    context.theme.resolveAttribute(R.attr.selectableItemBackground, this, true)
    setBackgroundResource(resourceId)
}

fun View.setOnClickListenerOrHideRipple(onClickListener: View.OnClickListener?) {
    if (onClickListener != null) {
        addRipple()
    } else {
        background = null
    }
    setOnClickListener(onClickListener)
}