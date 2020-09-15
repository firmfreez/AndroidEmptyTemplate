package com.firmfreez.android.androidemptytemplate.di

import dagger.Component
import javax.inject.Singleton

@Component(modules = [AndroidModule::class])
@Singleton
interface AppComponent {
}