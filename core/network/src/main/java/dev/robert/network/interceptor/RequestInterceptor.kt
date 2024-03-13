package dev.robert.network.interceptor

import dev.robert.network.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.newBuilder().addQueryParameter("key", BuildConfig.API_KEY).build()
        return chain.proceed(originalRequest.newBuilder().url(url).build())
    }
}