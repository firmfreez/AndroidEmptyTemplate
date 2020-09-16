package com.firmfreez.android.androidemptytemplate.exceptions

import okhttp3.Request
import okhttp3.Response

/**
 * Сетевые исключения, которых нет в Retrofit
 * - Неверные коды ответа
 * - и подобные
 *
 * @param message - стандартный для Exception
 * @param displayedMessage - сообщение, которое можно вывести пользователю
 *
 * **/
open class NetworkResponseException(message: String, displayedMessage: String? = null, response: Response? = null): AppException(message, displayedMessage) {
    private val request:    Request?    = response?.request()
    val httpResponseCode:   Int?        = response?.code()
    val url:                String?     = request?.url()?.url()?.path
}