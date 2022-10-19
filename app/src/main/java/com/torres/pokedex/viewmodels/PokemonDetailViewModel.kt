package com.torres.pokedex.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.plcoding.jetpackcomposepokedex.data.remote.responses.Pokemon
import com.torres.pokedex.data.model.PokemonList
import com.torres.pokedex.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val pokemonRepositoryImp: PokemonRepository,

    ) : ViewModel() {
    private val _pokemon: MutableLiveData<Pokemon> by lazy {
        MutableLiveData<Pokemon>(null)
    }
    val pokemon: LiveData<Pokemon> get() = _pokemon

    private val _loading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(true)
    }
    val loading: LiveData<Boolean> get() = _loading

    fun getPokemon(name: String) {
        viewModelScope.launch(Dispatchers.IO) {

            val tmpPokemon: PokemonList = pokemonRepositoryImp.getDbPokemonByName(name)
            if (tmpPokemon != null) {
                _loading.postValue(false)
                val gson = Gson()
                val pokemon: Pokemon = gson.fromJson(tmpPokemon.pokemon, Pokemon::class.java)
                _pokemon.postValue(pokemon)

            }
        }

    }

}