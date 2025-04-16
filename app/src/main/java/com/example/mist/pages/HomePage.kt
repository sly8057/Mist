package com.example.mist.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import androidx.navigation.NavController
import com.example.mist.AuthState
import com.example.mist.AuthViewModel
import com.example.mist.R
import com.example.mist.ui.theme.DutchWhite
import com.example.mist.ui.theme.EerieBlack
import com.example.mist.ui.theme.backgroundColor
import com.example.mist.ui.theme.mainColorGradient

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val progress = 50f / 100f

    /*val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when(authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("start")
            else -> Unit
        }
    }*/

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .shadow(
                    elevation = 4.dp,
                    ambientColor = Color(0x40000000),
                    spotColor = Color(0x40000000),
                )
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .background(
                    color = EerieBlack,
                    shape = RoundedCornerShape(
                        topStart = 0.dp, topEnd = 0.dp,
                        bottomStart = 20.dp, bottomEnd = 20.dp
                    )
                )
                .padding(
                    start = 30.dp,
                    end = 20.dp,
                    top = 50.dp,
                    bottom = 20.dp
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Hola, Whispersoul!" + "👋",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_regular)),
                    color = DutchWhite
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Progreso de aprendizaje:",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_medium)),
                    color = DutchWhite
                )
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
            ) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .align(Alignment.CenterStart),
                    color = DutchWhite,
                    trackColor = DutchWhite.copy(alpha = 0.5f)
                )

                Icon(
                    imageVector = Icons.Default.Star,
                    // painter = painterResource(id = R.drawable.rocket),
                    contentDescription = "Progress Indicator",
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterStart)
                        .offset(
                            x = (
                                    progress * LocalDensity.current.run {
                                        (LocalConfiguration.current.screenWidthDp.dp.toPx())
                                    }).dp
                        )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Nivel actual: 3",
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_italic)),
                        color = DutchWhite
                    )
                )

                Text(
                    text = "80 / 150 XP",
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_italic)),
                        color = DutchWhite
                    )
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 80.dp,
                    bottom = 20.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Lesson(
                icon = R.drawable.flag_solid,
                description = "Icono de Leccion",
                title = "Conceptos Basicos",
                multiple = false
            )
            Lesson(
                icon = R.drawable.java,
                description = "Icono de Java",
                title = "Java SE",
                multiple = true,
                icon2 = R.drawable.database_solid,
                description2 = "Icono de Base de Datos",
                title2 = "Bases de Datos",
            )
            Lesson(
                icon = R.drawable.git,
                description = "Icono de Git",
                title = "Sistemas de Versionamiento",
                multiple = false
            )
            Lesson(
                icon = R.drawable.android,
                description = "Icono de Android",
                title = "Android Studio",
                multiple = true,
                icon2 = R.drawable.ios,
                description2 = "Icono de iOS",
                title2 = "iOS (Swift)",
            )

        }

    }
}


@Composable
fun LessonsInRow(i1: Int, i2: Int, d1: String, d2: String, t1: String, t2: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 34.dp,
                end = 34.dp,
                top = 0.dp,
                bottom = 0.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Box(
            modifier = Modifier
                .drawWithCache {
                    val roundedPolygon = RoundedPolygon(
                        numVertices = 6,
                        radius = size.minDimension,
                        centerX = size.width / 2,
                        centerY = size.height / 2,
                        rounding = CornerRounding(
                            size.minDimension / 5f,
                            smoothing = 0.1f
                        )
                    )
                    val roundedPolygonPath = roundedPolygon.toPath().asComposePath()
                    onDrawBehind {
                        rotate(-90f) {
                            drawPath(roundedPolygonPath, color = DutchWhite)
                        }
                    }
                }
                .size(90.dp)
        ) {
            Column(
                modifier = Modifier
                    .drawWithCache {
                        val roundedPolygon = RoundedPolygon(
                            numVertices = 6,
                            radius = size.minDimension / 1.2f,
                            centerX = size.width / 2,
                            centerY = size.height / 2,
                            rounding = CornerRounding(
                                size.minDimension / 5f,
                                smoothing = 0.1f
                            )
                        )
                        val roundedPolygonPath = roundedPolygon.toPath().asComposePath()
                        onDrawBehind {
                            rotate(-90f) {
                                drawPath(roundedPolygonPath, color = EerieBlack)
                            }
                        }
                    }
                    .fillMaxSize()
                    .padding(
                        start = 0.dp,
                        end = 0.dp,
                        top = 10.dp,
                        bottom = 10.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(
                    painter = painterResource(id = i1),
                    contentDescription = d1,
                    modifier = Modifier
                        .size(35.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = t1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_regular)),
                        color = DutchWhite
                    ),
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                )
            }
        }

        Box(
            modifier = Modifier
                .drawWithCache {
                    val roundedPolygon = RoundedPolygon(
                        numVertices = 6,
                        radius = size.minDimension,
                        centerX = size.width / 2,
                        centerY = size.height / 2,
                        rounding = CornerRounding(
                            size.minDimension / 5f,
                            smoothing = 0.1f
                        )
                    )
                    val roundedPolygonPath = roundedPolygon.toPath().asComposePath()
                    onDrawBehind {
                        rotate(-90f) {
                            drawPath(roundedPolygonPath, color = DutchWhite)
                        }
                    }
                }
                .size(90.dp)
        ) {
            Column(
                modifier = Modifier
                    .drawWithCache {
                        val roundedPolygon = RoundedPolygon(
                            numVertices = 6,
                            radius = size.minDimension / 1.2f,
                            centerX = size.width / 2,
                            centerY = size.height / 2,
                            rounding = CornerRounding(
                                size.minDimension / 5f,
                                smoothing = 0.1f
                            )
                        )
                        val roundedPolygonPath = roundedPolygon.toPath().asComposePath()
                        onDrawBehind {
                            rotate(-90f) {
                                drawPath(roundedPolygonPath, color = EerieBlack)
                            }
                        }
                    }
                    .fillMaxSize()
                    .padding(
                        start = 0.dp,
                        end = 0.dp,
                        top = 10.dp,
                        bottom = 10.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(
                    painter = painterResource(id = i2),
                    contentDescription = d2,
                    modifier = Modifier
                        .size(35.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = t2,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_regular)),
                        color = DutchWhite
                    ),
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                )
            }
        }
    }
}

@Composable
fun Lesson(icon: Int, description: String, title: String, multiple: Boolean = false, icon2: Int = -1, description2: String = "", title2: String = "") {
    if (multiple)
        LessonsInRow(icon, icon2, description, description2, title, title2)
    else
        Box(
            modifier = Modifier
                .drawWithCache {
                    val roundedPolygon = RoundedPolygon(
                        numVertices = 6,
                        radius = size.minDimension,
                        centerX = size.width / 2,
                        centerY = size.height / 2,
                        rounding = CornerRounding(
                            size.minDimension / 5f,
                            smoothing = 0.1f
                        )
                    )
                    val roundedPolygonPath = roundedPolygon.toPath().asComposePath()
                    onDrawBehind {
                        rotate(-90f) {
                            drawPath(roundedPolygonPath, color = DutchWhite)
                        }
                    }
                }
                .size(90.dp)
        ) {
            Column(
                modifier = Modifier
                    .drawWithCache {
                        val roundedPolygon = RoundedPolygon(
                            numVertices = 6,
                            radius = size.minDimension / 1.2f,
                            centerX = size.width / 2,
                            centerY = size.height / 2,
                            rounding = CornerRounding(
                                size.minDimension / 5f,
                                smoothing = 0.1f
                            )
                        )
                        val roundedPolygonPath = roundedPolygon.toPath().asComposePath()
                        onDrawBehind {
                            rotate(-90f) {
                                drawPath(roundedPolygonPath, color = EerieBlack)
                            }
                        }
                    }
                    .fillMaxSize()
                    .padding(
                        start = 0.dp,
                        end = 0.dp,
                        top = 10.dp,
                        bottom = 10.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = description,
                    modifier = Modifier
                        .size(35.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_regular)),
                        color = DutchWhite
                    ),
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                )
            }
        }

    Spacer(modifier = Modifier.height(90.dp))
}