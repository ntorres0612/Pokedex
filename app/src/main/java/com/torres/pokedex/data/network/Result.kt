package com.torres.pokedex.data.network

import com.torres.pokedex.data.database.entity.PokemonListEntity
import com.torres.pokedex.data.model.PokemonList

data class Result(
    val name: String,
    val url: String,
    val pokemon: String
)
