package com.tcall.tcall_test.di

import android.content.Context
import com.tcall.tcall_test.BuildConfig
import com.tcall.tcall_test.repository.api.NetworkService
import com.tcall.tcall_test.util.Constant.BASE_URL
import com.tcall.tcall_test.util.ExceptionNoInternet
import com.tcall.tcall_test.util.isNetworkConnected
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideLogInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = when {
                BuildConfig.DEBUG -> HttpLoggingInterceptor.Level.BODY
                else -> HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Singleton
    @Provides
    fun provideErrorInterceptor(@ApplicationContext context: Context): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            if (!isNetworkConnected(context)) {
                throw ExceptionNoInternet(
                    "",
                    Throwable(ExceptionNoInternet::class.java.toString())
                )
            }
            val request = chain.request()
            chain.proceed(request)
        }
    }

    @Singleton
    @Provides
    fun provideOkHttp(error: Interceptor, logging: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder().run {
            addInterceptor(logging)
            addInterceptor(error)
            build()
        }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder().run {
        addConverterFactory(ScalarsConverterFactory.create())
        baseUrl(BASE_URL)
        client(client)
        build()
    }

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): NetworkService =
        retrofit.create(NetworkService::class.java)
}