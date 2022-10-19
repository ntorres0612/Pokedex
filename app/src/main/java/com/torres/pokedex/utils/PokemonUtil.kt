package com.torres.pokedex.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.palette.graphics.Palette
import com.plcoding.jetpackcomposepokedex.data.remote.responses.Stat
import com.plcoding.jetpackcomposepokedex.data.remote.responses.Type
import com.torres.pokedex.ui.theme.*
import java.util.*


object PokemonUtil{

    fun toHexCode(color: Color): String {
        val red = color.red * 255
        val green = color.green * 255
        val blue = color.blue * 255
        return String.format("%02x%02x%02x", red.toInt(), green.toInt(), blue.toInt())
    }

    fun getPokemonNumberFromUrl(url: String): String {

        return if(url.endsWith("/")) {
            url.dropLast(1).takeLastWhile { it.isDigit() }
        } else {
            url.takeLastWhile { it.isDigit() }
        }
    }


    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }

    fun parseStatToAbbr(stat: Stat): String {
        return when (stat.stat.name.lowercase(Locale.ROOT)) {
            "hp" -> "HP"
            "attack" -> "Atk"
            "defense" -> "Def"
            "special-attack" -> "SpAtk"
            "special-defense" -> "SpDef"
            "speed" -> "Spd"
            else -> ""
        }
    }
    fun parseStatToColor(stat: Stat): Color {
        return when (stat.stat.name.lowercase(Locale.getDefault())) {
            "hp" -> HPColor
            "attack" -> AtkColor
            "defense" -> DefColor
            "special-attack" -> SpAtkColor
            "special-defense" -> SpDefColor
            "speed" -> SpdColor
            else -> Color.White
        }
    }

    fun parseTypeToColor(type: Type): Color {
        return when (type.type.name.lowercase(Locale.ROOT)) {
            "normal" -> TypeNormal
            "fire" -> TypeFire
            "water" -> TypeWater
            "electric" -> TypeElectric
            "grass" -> TypeGrass
            "ice" -> TypeIce
            "fighting" -> TypeFighting
            "poison" -> TypePoison
            "ground" -> TypeGround
            "flying" -> TypeFlying
            "psychic" -> TypePsychic
            "bug" -> TypeBug
            "rock" -> TypeRock
            "ghost" -> TypeGhost
            "dragon" -> TypeDragon
            "dark" -> TypeDark
            "steel" -> TypeSteel
            "fairy" -> TypeFairy
            else -> Color.Black
        }
    }


    object StorageChecker {
        fun checkStorageAvailability(context: Context?) {
            var isStorageExist = false
            var isStorageWritable = false
            val state = Environment.getExternalStorageState()
            if (Environment.MEDIA_MOUNTED == state) {
                isStorageWritable = true
                isStorageExist = isStorageWritable
            } else if (Environment.MEDIA_MOUNTED_READ_ONLY == state) {
                isStorageExist = true
                isStorageWritable = false
                Toast.makeText(context, "Storage is read only", Toast.LENGTH_SHORT).show()
            } else {
                isStorageWritable = false
                isStorageExist = isStorageWritable
                Toast.makeText(context, "Storage is not exist", Toast.LENGTH_SHORT).show()
            }
        }
    }
}