package com.torres.pokedex.provider

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.torres.pokedex.data.database.entity.PokemonListEntity

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemon_list_table")
    suspend fun getListPokemon(): List<PokemonListEntity>

    @Query("SELECT * FROM pokemon_list_table WHERE name  LIKE '%' || :name || '%' LIMIT 1, 20 ")
    suspend fun getListPokemonByName(name: String): List<PokemonListEntity>

    @Query("SELECT * FROM pokemon_list_table WHERE name = :name  LIMIT 1 ")
    suspend fun getPokemonByName(name: String): PokemonListEntity

    @Insert
    suspend fun insertAllPokemonList(pokemonListEntity: List<PokemonListEntity>)


    @Insert
    suspend fun insertPokemonList(pokemonListEntity: PokemonListEntity)


    @Query("DELETE FROM pokemon_list_table")
    suspend fun deleteAllPokemonsList()

    @Query("UPDATE pokemon_list_table SET pokemon =:pokemon WHERE name =:name")
    suspend fun updatePokemonList(pokemon: String, name: String)

}