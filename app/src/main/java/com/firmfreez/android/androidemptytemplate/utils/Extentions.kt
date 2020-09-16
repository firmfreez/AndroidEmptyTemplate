package com.firmfreez.android.androidemptytemplate.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment

/**
 * Меняет фрагменты
 * @param resId - id действия замены из nav_graph.xml
 */
fun Fragment.navigate(resId: Int, bundle: Bundle? = null) {
    return NavHostFragment.findNavController(this).navigate(resId, bundle)
}