package com.torres.pokedex.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.torres.pokedex.R
import com.torres.pokedex.navigation.Destinations
import com.torres.pokedex.viewmodels.ListPokemonViewModel

@Composable
fun PokemonListScreen(
    viewModel: ListPokemonViewModel = hiltViewModel(),
    onDetailPokemonScreen: (String, String, String) -> Unit,
    navController: NavHostController,

    ) {
    val searchText by viewModel.searchText.observeAsState("")
    val pokemons by viewModel.pokemons.observeAsState(arrayListOf())

    val isFirstTime by viewModel.isFirstTime.observeAsState(false)


    if (!isFirstTime)
        viewModel.getListPokemonByName("")


    Column {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color(0xFFd30a40))
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.pokeapi),
                contentDescription = "PokeApi"
            )
        }
        Box(
            modifier =
            Modifier
                .background(Color(0xFF000000))
                .weight(.1f)
        ) {
            Column(
                modifier =
                Modifier
                    .background(Color(0xFFd30a40))
                    .padding(10.dp)
            ) {
                TextField(
                    value = searchText,
                    onValueChange = {
                        viewModel.getListPokemonByName(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF0F0F0), CircleShape),
                    textStyle = TextStyle(color = Color.Black, fontSize = 12.sp),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(15.dp)
                                .size(24.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchText != "") {
                            IconButton(
                                onClick = {
                                    viewModel.clearSearchText()
                                }
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .padding(15.dp)
                                        .size(24.dp)

                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RectangleShape,
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        cursorColor = Color.Black,
                        leadingIconColor = Color.Black,
                        trailingIconColor = Color.Black,
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(.9f)
                .fillMaxSize()
                .background(Color(0xFFFFC107))
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                //state = state,
                content = {
                    items(pokemons) { pokemon ->
                        PokemonItemScreen(
                            pokemonList = pokemon,
                            onDetailPokemonScreen = { name, color, url ->
                                navController.navigate(
                                    Destinations.DetailPokemonRoute.createRoute(
                                        name,
                                        color,
                                        url
                                    )
                                )
                            })
                    }
                }
            )
        }
    }
}