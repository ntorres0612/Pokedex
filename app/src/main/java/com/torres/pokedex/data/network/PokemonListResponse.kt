package com.torres.pokedex.data.network

data class PokemonListResponse(
    val count: Int,
    val next: String,
    val previous: String?,
    val results: List<Result>
)