package com.torres.pokedex.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.torres.pokedex.provider.PokemonDao
import com.torres.pokedex.data.database.entity.PokemonListEntity

@Database(entities = [PokemonListEntity::class], version = 1)
abstract class PokemonDatabase: RoomDatabase() {

    abstract fun getPokemonDao(): PokemonDao
}