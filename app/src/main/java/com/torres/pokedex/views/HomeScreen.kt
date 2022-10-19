package com.torres.pokedex.views


import android.Manifest
import android.annotation.SuppressLint
import android.graphics.drawable.BitmapDrawable
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.airbnb.lottie.compose.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.torres.pokedex.R
import com.torres.pokedex.navigation.Destinations.ListPokemonRoute
import com.torres.pokedex.utils.Constant
import com.torres.pokedex.utils.PokemonUtil
import com.torres.pokedex.viewmodels.ListPokemonViewModel
import com.torres.pokedex.viewmodels.PokemonDetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("PermissionLaunchedDuringComposition")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    listPokemonViewModel: ListPokemonViewModel = hiltViewModel(),
    pokemonDetailViewModel: PokemonDetailViewModel = hiltViewModel(),
    navController: NavHostController,
) {

    val context = LocalContext.current

    val multiplePermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    ) { permissionStateMap ->
        if (!permissionStateMap.containsValue(false)) {
            Toast.makeText(context, "Permissions Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permissions Denied", Toast.LENGTH_SHORT).show()
        }
    }

    var isPlaying by rememberSaveable { mutableStateOf(true) }
    val isLoading by listPokemonViewModel.isLoading.observeAsState(false)
    val finishLoadData by listPokemonViewModel.finishLoadData.observeAsState(false)

    val pokemonListCounter by listPokemonViewModel.pokemonListCounter.observeAsState(0)

    val pokemonDetailCounter by listPokemonViewModel.pokemonDetailCounter.observeAsState(0)

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.pokemon_animation)
    )
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isPlaying,
        speed = .5f,
        restartOnPlay = false,
    )

    if (finishLoadData && isPlaying) {
        isPlaying = false
        navController.navigate(ListPokemonRoute.route)
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            composition,
            progress,
            modifier = Modifier.size(100.dp)
        )

        Box(
            modifier = Modifier
                .height(400.dp)
                .padding(vertical = 20.dp)
        ) {

            if (isLoading) {
                ProgressUI()
            }

        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "List Of Pokemons : $pokemonListCounter of ${Constant.POKEMON_LIMIT}",
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.ExtraLight
            )

        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Detail Of Pokemons : $pokemonDetailCounter of ${Constant.POKEMON_LIMIT}",
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.ExtraLight
            )

        }
        Button(
            onClick = {
                listPokemonViewModel.pokemonListFromApi()
                // PokemonUtil.StorageChecker.checkStorageAvailability(context = context)
                // multiplePermissionsState.launchMultiplePermissionRequest()

            },
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                Icons.Filled.Bolt,
                contentDescription = "Start",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(text = "Start")
        }
    }
}

@Composable
@Preview
fun ProgressUI() {

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.downloading)
    )
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
        speed = .5f,
        restartOnPlay = false
    )

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            composition,
            progress,
            modifier = Modifier.size(400.dp)
        )
    }
}