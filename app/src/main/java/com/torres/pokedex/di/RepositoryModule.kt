package com.torres.pokedex.di

import com.torres.pokedex.repository.PokemonRepository
import com.torres.pokedex.repository.PokemonRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun pokemonRepository(repository: PokemonRepositoryImp) : PokemonRepository
}