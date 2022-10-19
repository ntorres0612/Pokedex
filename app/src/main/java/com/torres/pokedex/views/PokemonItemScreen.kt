package com.torres.pokedex.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.torres.pokedex.data.model.PokemonList
import com.torres.pokedex.utils.Constant
import com.torres.pokedex.utils.PokemonUtil
import com.torres.pokedex.viewmodels.ListPokemonViewModel
import java.util.*

@Composable
fun PokemonItemScreen(
    onDetailPokemonScreen: (String, String, String) -> Unit,
    viewModel: ListPokemonViewModel = hiltViewModel(),
    pokemonList: PokemonList
) {
    val defaultDominantColor = MaterialTheme.colors.surface

    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    Box(
        contentAlignment = Center,
        modifier = Modifier
            .clickable {
                onDetailPokemonScreen(
                    pokemonList.name,
                    PokemonUtil.toHexCode(dominantColor),
                    pokemonList.url
                )
            }
            .padding(10.dp)
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        dominantColor,
                        defaultDominantColor
                    )
                )
            )
    ) {
        Column() {

            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(
                        "${Constant.POKEMON_API_SVG}${
                            PokemonUtil.getPokemonNumberFromUrl(
                                pokemonList.url
                            )
                        }.svg"
                    )
                    .decoderFactory(SvgDecoder.Factory())
                    .crossfade(true)
                    .build(),

                onSuccess = { success ->
                    val drawable = success.result.drawable
                    PokemonUtil.calcDominantColor(drawable) { color ->
                        dominantColor = color
                    }
                },
                contentDescription = "",
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .size(80.dp)
                    .align(CenterHorizontally),

                loading = {
                    CircularProgressIndicator()
                },
            )
            Text(
                text = pokemonList.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(PaddingValues(bottom = 20.dp))
            )
        }
    }
}