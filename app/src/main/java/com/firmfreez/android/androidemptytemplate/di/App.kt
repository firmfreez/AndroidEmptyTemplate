package com.firmfreez.android.androidemptytemplate.di

import android.app.Application

class App: Application() {

    var component: AppComponent? = null
        get() {
            if (field == null) {
                field = DaggerAppComponent.builder()
                    .androidModule(AndroidModule(this))
                    .build()
            }
            return field
        }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: App
    }
}