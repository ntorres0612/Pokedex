package com.torres.pokedex.data.model

import com.torres.pokedex.data.database.entity.PokemonListEntity
import com.torres.pokedex.data.model.PokemonListModel
import com.torres.pokedex.data.network.Result

data class PokemonList(val url: String, val name: String, var pokemon: String)


fun PokemonListEntity.toDomain() = PokemonList(url, name, pokemon = pokemon)
fun Result.toDomain() = PokemonList(url, name, pokemon = "")