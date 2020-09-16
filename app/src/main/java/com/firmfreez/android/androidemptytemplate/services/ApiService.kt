package com.firmfreez.android.androidemptytemplate.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.firmfreez.android.androidemptytemplate.exceptions.NetworkResponseException
import com.firmfreez.android.androidemptytemplate.exceptions.WrongResponseCodeException
import com.firmfreez.android.androidemptytemplate.network.Api
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

open class ApiService @Inject constructor() {
    @Inject
    lateinit var api: Api

    /** Сосотяния загрузки.
     * При исполнении запроса можно передать ключ для HasMap.
     * По этому получаем LiveData<Boolean> и так мониторим сосотояние загрузки
     *  **/
    private val loadingStates: HashMap<String, MutableLiveData<Boolean>> = HashMap()

    suspend fun <ReturnType> executeOrNull(loadingStateKey: String? = null,  request: suspend () -> retrofit2.Response<ReturnType>): ReturnType? {
        return try {
            execute(loadingStateKey, request)
        } catch (ex: Exception) {
            null
        }
    }

    /***
     * Выполняет запрос в корутине
     * Проверяет код ответа (успешный код - 200) и тело ответа
     *
     * @param loadingStateKey - ключ состояния загрузки. По нему из loadingStates можно получить LiveData<Boolean> и следить за загрузкой
     * @param request - suspend функция из интерфейса Api, которую нужно выполнить
     *
     * @return Возвращает тело ответа <ReturnType> только если код 200 и тело ответа != null
     * @throws NetworkResponseException если тело ответа пустое.
     * @throws WrongResponseCodeException если код ответа не 200
     * @throws IOException выброшенные из Retrofit
     * Не перехватывает исключения выброшенные из Retrofit (и из MainInterceptorNew ErrorHandlerNew). Они отображаются и прокидываются дальше из MainInterceptorNew и ErrorHandlerNew
     *
     * **/
    @Throws(NetworkResponseException::class)
    suspend fun <ReturnType> execute(loadingStateKey: String? = null,  request: suspend () -> retrofit2.Response<ReturnType>): ReturnType {
        val result = executeRaw(loadingStateKey, request)
        if (result.isSuccessful) {
            result.body()?.let { return it }?: throw NetworkResponseException("Unable to get data from ${result.raw().request().url()}. Response body is null", null, result.raw())
        } else {
            throw WrongResponseCodeException("Unable to get data from ${result.raw().request().url()}. Response response code ${result.code()}", null, result.raw())
        }
    }

    suspend fun <ReturnType> executeRawOrNull(loadingStateKey: String? = null,  request: suspend () -> retrofit2.Response<ReturnType>): retrofit2.Response<ReturnType>? {
        return try {
            executeRaw(loadingStateKey, request)
        } catch (ex: Exception) {
            null
        }
    }

    /***
     * Выполняет запрос в корутине
     *
     * @param loadingStateKey - ключ состояния загрузки. По нему из loadingStates можно получить LiveData<Boolean> и следить за загрузкой
     * @param request - suspend функция из интерфейса Api, которую нужно выполнить
     *
     * @return Возвращает полный ответ от Retrofit Response<ReturnType?>
     * @throws Exception выброшенные из Retrofit
     *
     * **/
    @Throws(Exception::class)
    suspend fun <ReturnType> executeRaw(loadingStateKey: String? = null,  request: suspend () -> retrofit2.Response<ReturnType>): retrofit2.Response<ReturnType> {
        val loadingState = getLoadingState(loadingStateKey)
        loadingState?.postValue(true)
        val response = try {
            request.invoke()
        } catch (ex: Exception) {
            loadingState?.postValue(false)
            throw ex
        }
        loadingState?.postValue(false)
        return response
    }


    fun createLoadingState(loadingStateKey: String) {
        val currentLoadingState = loadingStates.getOrElse(loadingStateKey) { null }

        if (currentLoadingState == null ) {
            loadingStates.put(loadingStateKey, MutableLiveData<Boolean>().apply { value = false })
        }
    }

    fun createAndGetLoadingState(loadingStateKey: String): LiveData<Boolean>? {
        val currentLoadingState = loadingStates.getOrElse(loadingStateKey) { null }

        return if (currentLoadingState == null ) {
            loadingStates.put(loadingStateKey, MutableLiveData<Boolean>().apply { value = false })
            getLoadingState(loadingStateKey)
        } else {
            currentLoadingState
        }
    }

    fun getLoadingState(loadingStateKey: String?): MutableLiveData<Boolean>? {
        return if (loadingStateKey != null) { loadingStates.getOrElse(loadingStateKey) { null } } else { null }
    }

    interface ApiDataCallback <ReturnType> {
        fun onError(error: Error)
        fun onDataReceived(data: ReturnType)
    }

    interface ApiRawCallback <ReturnType> {
        fun onError(error: Error)
        fun onResponseReceived(response: Response<ReturnType>, code: Int)
    }
}