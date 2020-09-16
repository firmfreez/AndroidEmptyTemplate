package com.firmfreez.android.androidemptytemplate.exceptions

import okhttp3.Response

class WrongResponseCodeException(message: String, displayedMessage: String? = null, response: Response? = null): NetworkResponseException(message, displayedMessage, response)