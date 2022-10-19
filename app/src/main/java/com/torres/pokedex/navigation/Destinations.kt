package com.torres.pokedex.navigation

import androidx.compose.ui.graphics.Color

sealed class Destinations(
    var route: String
) {
    object HomeRoute: Destinations("homeScreen")
    object ListPokemonRoute: Destinations("listPokemonScreen")
    object DetailPokemonRoute: Destinations("detailPokemonScreen/?name={name}&color={color}&url={url}") {
        fun createRoute(name: String, color: String, url: String ) = "detailPokemonScreen/?name=$name&color=$color&url=$url"
    }
}
