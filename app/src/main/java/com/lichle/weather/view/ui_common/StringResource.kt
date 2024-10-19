package com.lichle.weather.view.ui_common

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class StringResource {
    data class Resource(@StringRes val resId: Int) : StringResource()
    class ResourceFormat(@StringRes val resId: Int, vararg val args: Any) : StringResource()
    data class Plain(val value: String) : StringResource()
}

fun StringResource.asString(context: Context): String {
    return when (this) {
        is StringResource.Resource -> context.getString(resId)
        is StringResource.ResourceFormat -> context.getString(resId, *args)
        is StringResource.Plain -> value
    }
}

@Composable
fun StringResource.asString(): String {
    return when (this) {
        is StringResource.Resource -> stringResource(resId)
        is StringResource.ResourceFormat -> stringResource(resId, *args)
        is StringResource.Plain -> value
    }
}