package com.torres.pokedex.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Palette.Swatch
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.gson.Gson
import com.torres.pokedex.data.database.entity.PokemonListEntity
import com.torres.pokedex.data.database.entity.toDatabase
import com.torres.pokedex.data.model.PokemonList
import com.torres.pokedex.repository.PokemonRepository
import com.torres.pokedex.utils.Constant
import com.torres.pokedex.utils.ImageUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.Comparator
import kotlin.collections.ArrayList


@HiltViewModel
class ListPokemonViewModel @Inject constructor(
    private val pokemonRepositoryImp: PokemonRepository,


    ) : ViewModel() {


    private val _finishLoadData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val finishLoadData: LiveData<Boolean> get() = _finishLoadData

    private val _pokemonListCounter: MutableLiveData<Int> by lazy { MutableLiveData<Int>(0) }
    val pokemonListCounter: LiveData<Int> get() = _pokemonListCounter

    private val _pokemonDetailCounter: MutableLiveData<Int> by lazy { MutableLiveData<Int>(0) }
    val pokemonDetailCounter: LiveData<Int> get() = _pokemonDetailCounter


    // private var pokemonList: List<PokemonList> = emptyList()

    private val _isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val isLoading: LiveData<Boolean> get() = _isLoading


    private val _isFirstTime: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }
    val isFirstTime: LiveData<Boolean> get() = _isFirstTime


    val text: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    private val _searchText: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }
    val searchText: LiveData<String> get() = _searchText

    val pokemons: MutableLiveData<List<PokemonList>> by lazy {
        MutableLiveData<List<PokemonList>>()
    }

    fun downloadImage(pokemonId: Int): String {

        var base64: String = ""
        viewModelScope.launch(Dispatchers.IO) {


            /*
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(getA)
                .data("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/dream-world/$pokemonId.svg")
                .allowHardware(false)
                .decoderFactory(SvgDecoder.Factory())// Disable hardware bitmaps.
                .build()

            val result = (loader.execute(request) as SuccessResult).drawable

            val bitmap = (result as BitmapDrawable).bitmap
            println("::::::::::::::Bitmap::::::::::::::::::")
            println(bitmap)
            base64 = ImageUtil.convert(bitmap)
            println("::::::::::::::Bitmap::::::::::::::::::")

             */

        }
        return base64
    }

    fun getListPokemonByName(name: String) {

        if (!_isFirstTime.value!!) {
            _isFirstTime.postValue(true)
        }
        _searchText.postValue(name)
        viewModelScope.launch(Dispatchers.IO) {

            val pokemonList = pokemonRepositoryImp.getListPokemonByName(name)
            if (pokemonList.isNotEmpty()) {
                pokemonList.map {
                    println("Name: ${it.name}")
                }
            }
            pokemons.postValue(pokemonList)
        }
    }

    fun clearSearchText() {
        _searchText.postValue("")
    }

    fun pokemonListFromApi() {

        viewModelScope.launch(Dispatchers.IO) {

            _isLoading.postValue(true)
            pokemonRepositoryImp.deleteAllPokemonList()
            val pokemonList = pokemonRepositoryImp.getPokemonListFromApi(0, Constant.POKEMON_LIMIT)

            var base64: String
            var counter = 0
            pokemonList.map {
                _pokemonListCounter.postValue(counter)
                val pokemonInfo = pokemonRepositoryImp.getPokemon(it.name)
                val pokemonString = Gson().toJson(pokemonInfo)
                it.pokemon = pokemonString

                base64 = downloadImage(pokemonInfo.id)

                pokemonRepositoryImp.savePokemonList(it.toDatabase())
                counter++
            }

            counter = 0
            pokemonList.map {
                _pokemonDetailCounter.postValue(counter)
                /*
                val pokemon = pokemonRepositoryImp.getPokemon(it.name)
                val pokemonString = Gson().toJson(pokemon)
                pokemonRepositoryImp.updatePokemon("Torres", it.name)

                 */
                counter++
            }

            _finishLoadData.postValue(true)

            pokemons.postValue(pokemonList)
            _isLoading.postValue(false)

        }
    }


}