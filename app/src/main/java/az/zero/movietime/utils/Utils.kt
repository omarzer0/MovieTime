package az.zero.movietime.utils

import android.content.Context
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.MenuItem

/**
 * converts statement of type [Any] into expression of the same type
 * to get the benefit of compile-time safety
 * for ex: turns
 *
 *     fun foo():Boolean { return true } -> fun foo():Boolean = true
 * */
val <T> T.exhaustive: T
    get() = this

val tabsName = listOf(
    "POPULAR",
    "TOP RATED",
    "ON TV",
    "AIRING TODAY"
)

/**
 * changes drawer title style for group of items to specific style
 * */
fun changeTitleTextStyle(titleText: MenuItem, context: Context, style: Int): SpannableString {
    val titleStyleText = SpannableString(titleText.title)
    titleStyleText.setSpan(
        TextAppearanceSpan(context, style),
        0, titleStyleText.length, 0
    )
    return titleStyleText
}