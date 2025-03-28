package com.example.mist.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.mist.R

// Degradados
val colorStopsGradient = arrayOf(0.0f to HunterGreen, 0.75f to Night)
val mainColorGradient = listOf(ForestGreen, HunterGreen, DarkGreen, EerieBlack)

val backgroundColor = Brush.verticalGradient(colorStops = colorStopsGradient)

// Iconos
@Composable
fun getImageVectorFromDrawable(resourceId: Int): ImageVector {
    return ImageVector.vectorResource(id = resourceId)
}

@Composable
fun LockOpenIconFun() {
    Image(
        painter = painterResource(id = R.drawable.ic_lock_open),
        contentDescription = "Lock Open Icon",
        modifier = Modifier.size(48.dp)
    )
}