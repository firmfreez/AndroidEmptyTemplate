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

/**
 * Главный модуль DI, обеспечивает инициализацию всех необходимых нам объектов
 * В будущем можно будет разбить на несколько модулей
 */
@Module
class AndroidModule(private val application: Application) {

    //Необходим для того, чтобы был доступен Application
    @Provides
    @Singleton
    @ForApplication
    fun provideApplicationContext() = application

    //Создает экземпляр класса Gson
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    //Создаёт экземпляр класса OkHttpClient
    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return UnsafeOkHttpClient.getUnsafeOkHttpClient()
    }

    //Создаёт экземпляр класса Retrofit, настраивает его на работу с Data-классами Gson,
    //Так же подключает к интерфейсу Api.kt
    @Provides
    @Singleton
    fun provideApi(gson: Gson, httpClient: OkHttpClient): Api {
        val retrofit = Retrofit.Builder()
            .baseUrl(Api.BACK_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(DownloadCallAdapterFactory.create())

        return retrofit.build().create(Api::class.java)
    }

    //Возвращает экземпляр класса PrefsManager
    @Provides
    @Singleton
    fun providePrefsManager(): PrefsManager {
        return PrefsManager(application.getSharedPreferences("prefs", Context.MODE_PRIVATE))
    }
}