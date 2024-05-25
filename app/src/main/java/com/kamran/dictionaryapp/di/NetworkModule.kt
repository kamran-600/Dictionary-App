package com.kamran.dictionaryapp.di

import com.kamran.dictionaryapp.data.api.DictionaryApi
import com.kamran.dictionaryapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofitBuilder() : Retrofit.Builder{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideOkHttpClient() : OkHttpClient{
        return OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build()
    }

    @Singleton
    @Provides
    fun provideDictionaryApi(retrofitBuilder : Retrofit.Builder, okHttpClient: OkHttpClient) : DictionaryApi {
        return retrofitBuilder.client(okHttpClient).build().create(DictionaryApi::class.java)
    }



}