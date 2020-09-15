package com.firmfreez.android.androidemptytemplate.di

import android.app.Application
import android.content.Context
import com.firmfreez.android.androidemptytemplate.network.Api
import com.firmfreez.android.androidemptytemplate.network.UnsafeOkHttpClient
import com.firmfreez.android.androidemptytemplate.storage.PrefsManager
import com.github.samizerouta.retrofit2.adapter.download.DownloadCallAdapterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AndroidModule(private val application: Application) {

    @Provides
    @Singleton
    @ForApplication
    fun provideApplicationContext() = application

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return UnsafeOkHttpClient.getUnsafeOkHttpClient()
    }

    @Provides
    @Singleton
    fun provideApi(gson: Gson, httpClient: OkHttpClient): Api {
        val retrofit = Retrofit.Builder()
            .baseUrl("")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(DownloadCallAdapterFactory.create())

        return retrofit.build().create(Api::class.java)
    }

    @Provides
    @Singleton
    fun providePrefsManager(): PrefsManager {
        return PrefsManager(application.getSharedPreferences("prefs", Context.MODE_PRIVATE))
    }
}