package com.torres.pokedex.provider

import com.plcoding.jetpackcomposepokedex.data.remote.responses.Pokemon
import com.torres.pokedex.data.network.PokemonListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApiProvider {
    @GET("pokemon/")
    suspend fun getPokemonList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<PokemonListResponse>

    @GET("pokemon/{name}")
    suspend fun getPokemon(
        @Path("name") name: String
    ): Pokemon
}