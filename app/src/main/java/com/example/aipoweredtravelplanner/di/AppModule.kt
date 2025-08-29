package com.example.aipoweredtravelplanner.di

import android.content.Context
import com.example.aipoweredtravelplanner.data.datastore.FavoritesDataStore
import com.example.aipoweredtravelplanner.data.remote.GeminiApiService
import com.example.aipoweredtravelplanner.data.repository.DefaultTravelsRepository
import com.example.aipoweredtravelplanner.domain.interactor.TravelsInteractor
import com.example.aipoweredtravelplanner.domain.repository.TravelsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Module that provides application-level dependencies.
 *
 * This module is responsible for creating and providing instances of
 * `GeminiApiService`, `TravelsRepository`, `TravelsInteractor`, and `FavoritesDataStore`
 * which are used throughout the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    @Provides
    fun provideGeminiApi(): GeminiApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(GeminiApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(api: GeminiApiService): TravelsRepository {
        return DefaultTravelsRepository(api)
    }

    @Provides
    fun provideInteractor(repo: TravelsRepository): TravelsInteractor {
        return TravelsInteractor(repo)
    }

    @Provides
    @Singleton
    fun provideLikedTravelsDataStore(@ApplicationContext context: Context): FavoritesDataStore {
        return FavoritesDataStore(context)
    }
}
