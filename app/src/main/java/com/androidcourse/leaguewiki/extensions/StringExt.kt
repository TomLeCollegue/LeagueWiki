package com.androidcourse.leaguewiki.extensions

fun String.clearTags(): String {
    this.replace("<br>", "\n")
    val regex = Regex("<.*>")
    return regex.replace(this, "")
}