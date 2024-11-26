

package com.bnyro.translate.util

import com.bnyro.translate.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitHelper {
    val contentType = "application/json".toMediaTypeOrNull()!!

    inline fun <reified T> createInstance(baseUrl: String): T {
        val httpClient = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            httpClient.addInterceptor(logging) // <-- this is the important line!
        }

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(JsonHelper.json.asConverterFactory(contentType))
            .client(httpClient.build())
            .build()
            .create(T::class.java)
    }

    inline fun <reified T> createApi(engine: TranslationEngine) = createInstance<T>(engine.getUrl())
}
