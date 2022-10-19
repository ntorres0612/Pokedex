package com.torres.pokedex.repository

import com.plcoding.jetpackcomposepokedex.data.remote.responses.Pokemon
import com.torres.pokedex.provider.PokemonDao
import com.torres.pokedex.data.database.entity.PokemonListEntity
import com.torres.pokedex.data.model.PokemonList
import com.torres.pokedex.data.model.toDomain
import com.torres.pokedex.provider.PokemonApiProvider
import javax.inject.Inject


interface PokemonRepository {
    suspend fun getPokemonListFromApi(offset: Int, limit: Int): List<PokemonList>
    suspend fun getPokemonListFromDb(): List<PokemonList>
    suspend fun savePokemonsList(pokemonListsEntity: List<PokemonListEntity>)
    suspend fun savePokemonList(pokemonListEntity: PokemonListEntity)

    suspend fun deleteAllPokemonList()
    suspend fun getListPokemonByName(name: String): List<PokemonList>
    suspend fun getDbPokemonByName(name: String): PokemonList


    suspend fun getPokemon(name: String): Pokemon

    suspend fun updatePokemon(pokemon: String, name: String )
}

class PokemonRepositoryImp @Inject constructor(
    private val pokemonDao: PokemonDao,
    private val pokemonApiProvider: PokemonApiProvider
) : PokemonRepository {

    override suspend fun getPokemonListFromApi(offset: Int, limit: Int): List<PokemonList> {
        val response = pokemonApiProvider.getPokemonList(offset, limit).body()
        return response!!.results.map { it.toDomain() }
    }


    override suspend fun getPokemonListFromDb(): List<PokemonList> {
        val response: List<PokemonListEntity> = pokemonDao.getListPokemon()
        return response.map { it.toDomain() }
    }

    override suspend fun savePokemonsList(pokemonListsEntity: List<PokemonListEntity>) {
        pokemonDao.insertAllPokemonList(pokemonListEntity = pokemonListsEntity)
    }

    override suspend fun savePokemonList(pokemonListEntity: PokemonListEntity) {
        pokemonDao.insertPokemonList(pokemonListEntity = pokemonListEntity)
    }

    override suspend fun deleteAllPokemonList() {
        pokemonDao.deleteAllPokemonsList()
    }

    override suspend fun getListPokemonByName(name: String): List<PokemonList> {
        val response: List<PokemonListEntity> = pokemonDao.getListPokemonByName(name)
        return response.map { it.toDomain() }
    }

    override suspend fun getDbPokemonByName(name: String): PokemonList {
        return pokemonDao.getPokemonByName(name).toDomain()
    }

    override suspend fun getPokemon(name: String): Pokemon {
        return pokemonApiProvider.getPokemon(name)
    }

    override suspend fun updatePokemon(pokemon: String, name: String) {
        println("::::::::::::::::Update Repository::::::::::::::")
        println(pokemon)
        println(name)
        println("::::::::::::::::Update Repository::::::::::::::")
        pokemonDao.updatePokemonList(pokemon, name)
    }
}