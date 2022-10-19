package com.torres.pokedex.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.torres.pokedex.data.model.PokemonList

@Entity(tableName = "pokemon_list_table")
data class PokemonListEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "pokemon") val pokemon: String
)
fun PokemonList.toDatabase() = PokemonListEntity(url = url, name = name, pokemon = pokemon)