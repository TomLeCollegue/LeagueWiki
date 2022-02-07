package com.androidcourse.leaguewiki.extensions

import android.view.View
import android.widget.TextView


fun TextView.setTextOrHide(
    value: CharSequence?,
    ifVisibleBlock: (TextView.() -> Unit)? = null
) {
    if (value.isNullOrEmpty()) {
        visibility = View.GONE
    } else {
        text = value
        ifVisibleBlock?.let { apply(it) }
        visibility = View.VISIBLE
    }
}