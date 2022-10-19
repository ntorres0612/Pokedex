package com.torres.pokedex.navigation

import android.Manifest
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.torres.pokedex.views.HomeScreen
import com.torres.pokedex.views.PokemonDetailScreen
import com.torres.pokedex.views.PokemonListScreen
import com.torres.pokedex.navigation.Destinations.*
import com.torres.pokedex.utils.Constant


@Composable
fun NavigationHost() {
    val navController = rememberNavController()



    NavHost(
        navController = navController,
        startDestination = HomeRoute.route
    ) {
        composable(HomeRoute.route) {
            HomeScreen(navController = navController)
        }
        composable(ListPokemonRoute.route) {
            PokemonListScreen(
                navController = navController,
                onDetailPokemonScreen = { name, color, url ->
                    navController.navigate(DetailPokemonRoute.createRoute(name, color, url))
                }
            )
        }
        composable(
            DetailPokemonRoute.route,
            arguments = listOf(
                navArgument("name") { defaultValue = "Pikachu" },
                navArgument("color") { defaultValue = "F42C04" },
                navArgument("url") { defaultValue ="https://pokeapi.co/api/v2/pokemon/25/" }
            )
        ) {
            val name = it.arguments?.getString("name")
            val color = it.arguments?.getString("color")
            val url = it.arguments?.getString("url")

            requireNotNull(name)
            requireNotNull(color)
            PokemonDetailScreen(
                name = name!!,
                color = color!!,
                url = url!!,
                onListScreen = {
                    navController.navigate(ListPokemonRoute.route)
                }
            )
        }
    }
}