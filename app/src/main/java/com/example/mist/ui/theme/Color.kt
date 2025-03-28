package com.example.mist.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val DutchWhite = Color(0xFFF5F5F5)
val Asparagus = Color(0xFF85A947)
val ForestGreen = Color(0xFF4C842D)
val HunterGreen = Color(0xFF285826)
val DarkGreen = Color(0xFF123524)
val EeireBlack = Color(0xFF1A1A19)
val Night = Color(0xFF0D0D0D)

val colorStopsGradient = arrayOf(0.0f to HunterGreen, 0.75f to Night)
val mainColorGradient = listOf(ForestGreen, HunterGreen, DarkGreen, EeireBlack)

val backgroundColor = Brush.verticalGradient(colorStops = colorStopsGradient)