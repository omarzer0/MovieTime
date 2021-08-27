package az.zero.movietime.di

import az.zero.movietime.api.ShowApi
import az.zero.movietime.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

object RetrofitModule {

    @Module
    @InstallIn(SingletonComponent::class)
    object AppModule {

        @Singleton
        @Provides
        fun provideRetrofit(): Retrofit =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        @Singleton
        @Provides
        fun provideMoveApi(retrofit: Retrofit): ShowApi =
            retrofit.create(ShowApi::class.java)
    }


}