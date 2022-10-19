package com.torres.pokedex.di

import android.content.Context
import androidx.room.Room
import com.torres.pokedex.data.database.PokemonDatabase
import com.torres.pokedex.provider.PokemonApiProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PokemonModule {

    companion object {
        private const val DB_NAME = "pokemon_database"
    }


    @Provides
    @Named("BaseUrl")
    fun provideBaseUrl() = "https://pokeapi.co/api/v2/".toHttpUrl()

    @Singleton
    @Provides
    fun provideRetrofit(@Named("BaseUrl") baseUrl: HttpUrl): Retrofit {
        println("Base url :::::. $baseUrl")
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providePokemonApi(retrofit: Retrofit): PokemonApiProvider =
        retrofit.create(PokemonApiProvider::class.java)

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context): PokemonDatabase {
        return Room.databaseBuilder(
            context,
            PokemonDatabase::class.java,
            Companion.DB_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun providePokemonDao(db: PokemonDatabase) = db.getPokemonDao()
}