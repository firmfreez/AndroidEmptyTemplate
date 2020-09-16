package com.firmfreez.android.androidemptytemplate.utils

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.firmfreez.android.androidemptytemplate.di.App

/**
 * По большому счету необходим для вызовов getString
 */
object AppContext {
    val instance: Context get() = App.instance.applicationContext

    fun getString(@StringRes id: Int?): String {
        return id?.let { instance.getString(it)?: "" }?: ""
    }

    fun getText(@StringRes id: Int?): CharSequence {
        return id?.let { instance.getText(it)?: "" }?: ""
    }

    fun getColor(@ColorRes id: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            instance.getColor(id)
        } else {
            ContextCompat.getColor(instance, id)
        }
    }

    fun getColorStateList(@ColorRes id: Int): ColorStateList? {
        return try {
            ContextCompat.getColorStateList(instance, id)
        } catch (ex: Resources.NotFoundException) {
            null
        }
    }
}