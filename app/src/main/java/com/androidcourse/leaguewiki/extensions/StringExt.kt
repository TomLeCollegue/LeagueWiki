package com.androidcourse.leaguewiki.extensions

fun String.clearTags(): String {
    var result = this
    result = result.replace("<br>", "\n")
    val regex1 = Regex("<[^/]*>")
    val regex2 = Regex("</[^>]*>")
    result = regex1.replace(result, "")
    result = regex2.replace(result, "")
    return result
}