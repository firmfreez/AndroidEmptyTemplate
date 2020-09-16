package com.firmfreez.android.androidemptytemplate.di

import com.firmfreez.android.androidemptytemplate.view.viewModels.FragmentMainViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AndroidModule::class])
@Singleton
interface AppComponent {

    //ViewModels
    fun inject(fragmentMainViewModel: FragmentMainViewModel)
}