package com.firmfreez.android.androidemptytemplate.view.viewModels

import androidx.lifecycle.ViewModel
import com.firmfreez.android.androidemptytemplate.di.App

class FragmentMainViewModel : ViewModel() {
    init {
        App.instance.component?.inject(this)
    }
}