package com.example.g46_kotlin.core.di

import com.example.g46_kotlin.BuildConfig
import com.example.g46_kotlin.features.auth.data.remote.AuthApiService
import com.example.g46_kotlin.features.house.data.remote.HouseApiService
import com.example.g46_kotlin.features.map.data.remote.MapApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import com.example.g46_kotlin.core.network.AuthHeaderInterceptor
import com.example.g46_kotlin.core.network.TokenAuthenticator
import com.example.g46_kotlin.features.auth.data.remote.AuthRefreshApiService
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun provideBaseUrl(): String = BuildConfig.API_BASE_URL

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authHeaderInterceptor: AuthHeaderInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authHeaderInterceptor)
        .authenticator(tokenAuthenticator)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json,
        baseUrl: String
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    fun provideMapApiService(retrofit: Retrofit): MapApiService =
        retrofit.create(MapApiService::class.java)

    @Provides
    @Singleton
    fun provideHouseApiService(retrofit: Retrofit): HouseApiService =
        retrofit.create(HouseApiService::class.java)

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService =
        retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    @Named("noAuthClient")
    fun provideNoAuthOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    @Named("noAuthRetrofit")
    fun provideNoAuthRetrofit(
        @Named("noAuthClient") okHttpClient: OkHttpClient,
        json: Json,
        baseUrl: String
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    fun provideAuthRefreshApiService(
        @Named("noAuthRetrofit") retrofit: Retrofit
    ): AuthRefreshApiService = retrofit.create(AuthRefreshApiService::class.java)

}
