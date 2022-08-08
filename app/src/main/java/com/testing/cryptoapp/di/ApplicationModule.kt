package com.testing.cryptoapp.di

import android.content.Context
import com.testing.cryptoapp.api.ApiInterface
import com.testing.cryptoapp.api.CoroutineDispatcherProvider
import com.testing.cryptoapp.data.local.database.CoinDatabase
import com.testing.cryptoapp.data.local.preferences.DataPreferenceStorage
import com.testing.cryptoapp.data.local.preferences.PreferenceStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val requestInterceptor = Interceptor { chain ->
            val url = chain.request()
                .url
                .newBuilder()
                .build()

            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()

            return@Interceptor chain.proceed(request)
        }

        return OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.coingecko.com/api/v3/")
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiClient(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CoinDatabase = CoinDatabase.buildDatabase(context)

    @Provides
    @Singleton
    fun providePreferenceStorage(@ApplicationContext context: Context): PreferenceStorage = DataPreferenceStorage(context)

    @Provides
    @Singleton
    fun provideCoroutineDispatcherProvider(): CoroutineDispatcherProvider = CoroutineDispatcherProvider()
}