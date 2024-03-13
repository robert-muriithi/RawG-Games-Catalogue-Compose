package dev.robert.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.robert.network.apiservice.GamesApi
import dev.robert.network.interceptor.RequestInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
    ): OkHttpClient {
        val chuckInterceptor = ChuckerInterceptor.Builder(context)
            .apply {
                collector(
                    ChuckerCollector(
                        context = context,
                        showNotification = BuildConfig.DEBUG,
                        retentionPeriod = RetentionManager.Period.ONE_DAY
                    )
                )
                maxContentLength(250_000L)
                alwaysReadResponseBody(false)
                if (!BuildConfig.DEBUG) {
                    redactHeaders("Authorization", "Bearer")
                    redactHeaders("Authorization", "Basic")
                }
            }
            .build()
        val loggingInterceptor = HttpLoggingInterceptor()
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                },
            )
            .addInterceptor(RequestInterceptor())
            .addInterceptor(chuckInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }



    @Provides
    @Singleton
    fun provideProductApi(
        retrofit: Retrofit
    ): GamesApi {
        return retrofit.create(GamesApi::class.java)
    }
}