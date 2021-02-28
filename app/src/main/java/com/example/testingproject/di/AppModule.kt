package com.example.testingproject.di

import android.content.Context
import androidx.room.Room
import com.example.testingproject.webauth.ApiResponse
import com.example.testingproject.room.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun getRetrofitInstance() : ApiResponse{
        val httplogginInterceptor = HttpLoggingInterceptor()
        httplogginInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttp = OkHttpClient.Builder()
            .addInterceptor(httplogginInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .client(okHttp)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiResponse::class.java)
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context)  =
        Room.databaseBuilder(context.applicationContext,NewsDatabase::class.java,
        "news.db").fallbackToDestructiveMigration().build()
//
//    @Singleton
//    @Provides
//    fun provideFavNewsDao(newsDatabase: NewsDatabase) = newsDatabase.favNewsDao()
//
//    @Singleton
//    @Provides
//    fun provideSuggestionsDao(newsDatabase: NewsDatabase) = newsDatabase.suggestionsDao()
//
//    @Singleton
//    @Provides
//    fun provideHeadLinesDao(newsDatabase: NewsDatabase) = newsDatabase.headlinesDao()
//
//    @Singleton
//    @Provides
//    fun provideNewsDao(newsDatabase: NewsDatabase) = newsDatabase.newsDao()
//
//    @Singleton
//    @Provides
//    fun provideHeadlinesRemoteKeyDao(newsDatabase: NewsDatabase)  = newsDatabase.headlinesRemoteKey()
//
//    @Singleton
//    @Provides
//    fun provideNewsRemoteKeyDao(newsDatabase: NewsDatabase)  = newsDatabase.newsRemoteKey()
//

}