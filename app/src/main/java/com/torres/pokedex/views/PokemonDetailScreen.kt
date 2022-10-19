package com.torres.pokedex.views

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.torres.pokedex.R
import com.torres.pokedex.utils.Constant
import com.torres.pokedex.utils.PokemonUtil
import com.torres.pokedex.viewmodels.PokemonDetailViewModel
import java.util.*

@Composable
fun PokemonDetailScreen(
    onListScreen: () -> Unit,
    name: String,
    color: String,
    url: String,
    viewModel: PokemonDetailViewModel = hiltViewModel(),

    ) {

    val loading by viewModel.loading.observeAsState(true)
    val pokemon by viewModel.pokemon.observeAsState(null)


    viewModel.getPokemon(name)

    var animDelayPerItem: Int = 100

    if (pokemon == null) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally
        ) {

            CircularProgressIndicator()
        }
    } else {

        val maxBaseStat = remember {
            pokemon!!.stats.maxOf { it.baseStat }
        }


        Column(

            Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Gray,
                            Color(android.graphics.Color.parseColor("#$color"))
                        )
                    )
                )
                .fillMaxSize()
        ) {
            Row(
                Modifier
                    .padding(30.dp)
                    .clickable { onListScreen() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Box(
                Modifier
                    .fillMaxSize()

            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .background(Color.White),
                    horizontalAlignment = Alignment.End
                ) {

                    Row(
                        horizontalArrangement = Arrangement.End
                    ) {
                        Column(
                            modifier = Modifier
                                .offset(y = (-70).dp),
                            horizontalAlignment = Alignment.End,
                        ) {

                            SubcomposeAsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(
                                        "${Constant.POKEMON_API_SVG}${
                                            PokemonUtil.getPokemonNumberFromUrl(
                                                url
                                            )
                                        }.svg"

                                    )
                                    .decoderFactory(SvgDecoder.Factory())
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "",
                                contentScale = ContentScale.Inside,
                                modifier = Modifier
                                    //.clip(CircleShape)
                                    .size(200.dp)
                                    .align(CenterHorizontally),
                                loading = {
                                    CircularProgressIndicator()
                                },
                            )
                        }
                        Box(
                            modifier = Modifier
                                //.size(100.dp)
                                .clip(RoundedCornerShape(bottomStart = 10.dp))
                                .background(Color.LightGray),
                            contentAlignment = CenterEnd

                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(10.dp),

                                text = "#${pokemon!!.id}",
                                fontSize = 20.sp,

                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Right
                                )
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            modifier = Modifier
                                .padding(10.dp),
                            text = name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.US) else it.toString() },
                            fontSize = 20.sp,

                            style = TextStyle(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {


                        Row() {
                            for (type in pokemon!!.types) {
                                Box(
                                    contentAlignment = Center,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 28.dp)
                                        .clip(CircleShape)
                                        .background(PokemonUtil.parseTypeToColor(type))

                                        .height(35.dp)
                                ) {

                                    Text(
                                        text = type.type.name.replaceFirstChar {
                                            if (it.isLowerCase()) it.titlecase(
                                                Locale.ROOT
                                            ) else it.toString()
                                        },
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                }
                            }


                        }

                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(modifier = Modifier.weight(.5f)) {
                            Column(
                                horizontalAlignment = CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()

                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.height),
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "${pokemon!!.weight} kg",
                                    color = MaterialTheme.colors.onSurface,
                                    fontSize = 12.sp,
                                )
                            }
                        }

                        Box(modifier = Modifier.weight(.5f)) {
                            Column(
                                horizontalAlignment = CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()

                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.height),
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "${pokemon!!.weight} m",
                                    color = MaterialTheme.colors.onSurface,
                                    fontSize = 12.sp

                                )
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Base stats:",
                            fontSize = 10.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))


                        for (i in pokemon!!.stats.indices) {
                            PokemonStat(
                                statName = PokemonUtil.parseStatToAbbr(pokemon!!.stats[i]),
                                statValue = pokemon!!.stats[i].baseStat,
                                statMaxValue = maxBaseStat,
                                statColor = PokemonUtil.parseStatToColor(pokemon!!.stats[i]),
                                animDelay = i * animDelayPerItem
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                    }
                }
            }

        }
    }

}


@Composable
fun PokemonStat(
    statName: String,
    statValue: Int,
    statMaxValue: Int,
    statColor: Color,
    height: Dp = 28.dp,
    animDuration: Int = 1000,
    animDelay: Int = 0
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val curPercent = animateFloatAsState(
        targetValue = if (animationPlayed) {
            statValue / statMaxValue.toFloat()
        } else 0f,
        animationSpec = tween(
            animDuration,
            animDelay
        )
    )
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(CircleShape)
            .background(
                if (isSystemInDarkTheme()) {
                    Color(0xFF505050)
                } else {
                    Color.LightGray
                }
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(curPercent.value)
                .clip(CircleShape)
                .background(statColor)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = statName,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
            Text(
                text = (curPercent.value * statMaxValue).toInt().toString(),
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp

            )
        }
    }
}