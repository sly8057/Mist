package com.example.mist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.sp
import com.example.mist.R
import com.example.mist.pages.questions
import com.example.mist.ui.theme.DutchWhite
import com.example.mist.ui.theme.EerieBlack
import com.example.mist.ui.theme.ForestGreen
import com.example.mist.ui.theme.HunterGreen
import com.example.mist.ui.theme.Night
import com.example.mist.ui.theme.getImageVectorFromDrawable

data class BottomNavItem(
    val route: String,
    val icon: Int,
    val label: String
)

@Composable
fun CustomBottomBar(navController: NavHostController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val items = listOf(
        BottomNavItem("camera", R.drawable.ic_camera, "Camera"),
        BottomNavItem("home", R.drawable.ic_house, "Home"),
        BottomNavItem("profile", R.drawable.ic_user_astronaut, "Profile"),
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Night)
            .navigationBarsPadding(),
        //.padding(horizontal = 24.dp, vertical = 8.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        // fondo base
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(ForestGreen)
            //.shadow(elevation = 8.dp, shape = RoundedCornerShape(32.dp))
        )
        // iconos
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = currentRoute == item.route

                // val iconModifier = if (index == 1) {
                if (index == 1) {
                    // icono central
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .offset(y = (-24).dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(ForestGreen)
                                .clickable {
                                    if (!isSelected) {
                                        navController.navigate(item.route) {
                                            popUpTo("home") { inclusive = false }
                                            launchSingleTop = true
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) HunterGreen else ForestGreen),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = getImageVectorFromDrawable(item.icon),
                                    contentDescription = item.label,
                                    tint = DutchWhite,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) HunterGreen else ForestGreen)
                            .clickable {
                                if (!isSelected) {
                                    navController.navigate(item.route) {
                                        popUpTo("home") { inclusive = false }
                                        launchSingleTop = true
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getImageVectorFromDrawable(item.icon),
                            contentDescription = item.label,
                            tint = DutchWhite,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopBar(
    title: String,
    navController: NavHostController,
    mostrarBorde: Boolean = false,
    nivelUsuario: Int? = null,
    onClick: () -> Unit = {}
) {
    val isNivel = if (nivelUsuario != null) (-4).dp else 0.dp
    // "login", "signup", "quiz", "explore"
    CenterAlignedTopAppBar(
        /*modifier = Modifier
            .height(IntrinsicSize.Max)
            .background(EerieBlack)
            .padding(vertical = 16.dp)
        ,*/
        title = {
            Text(
                text = title,
                color = DutchWhite,
                fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_bold))
            )
        },
        navigationIcon = {
            if (navController.previousBackStackEntry != null) {
                IconButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = getImageVectorFromDrawable(R.drawable.ic_back_arrow),
                        contentDescription = "Back",
                    )
                }
            }
        },
        actions = {
            Box(
                modifier = Modifier
                    .padding(end = 8.dp, bottom = 8.dp)
                    .size(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clip(CircleShape)
                        .background(if (mostrarBorde) DutchWhite else Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = { onClick() },
                        modifier = Modifier
                            //.offset(y = (-2).dp)
                            //.offset(if (nivelUsuario != null) y = (-2).dp else y = 0.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(if (mostrarBorde) EerieBlack else Color.Transparent)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_robot_white),
                            modifier = Modifier
                                .offset(y = isNivel)
                                //.offset(if (nivelUsuario != null) y = (-2).dp else y = 0.dp)
                                .size(if (mostrarBorde) 28.dp else 44.dp),
                            contentDescription = "mist"
                        )
                    }
                }

                // nivel
                if (nivelUsuario != null) {
                    Box(
                        modifier = Modifier
                            .offset(y = 20.dp)
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(DutchWhite),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            //modifier = Modifier.offset(y = (-1.5).dp),
                            text = nivelUsuario.toString(),
                            fontSize = 12.sp,
                            color = EerieBlack,
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = EerieBlack,
            titleContentColor = DutchWhite,
            actionIconContentColor = DutchWhite,
            navigationIconContentColor = DutchWhite
        )
    )
}

@Composable
fun CustomTopBar(
    title: String,
    showBackButton: Boolean = false,
    onClick: () -> Unit = {},
    variant: Boolean = false,
    progress: Float = 0f,
    currentProgress: String = "",
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .shadow(
                elevation = 4.dp,
                ambientColor = Color(0x40000000),
                spotColor = Color(0x40000000),
            )
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .background(
                color = if(variant) DutchWhite else EerieBlack,
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
            )
            .padding(
                start = 30.dp,
                end = 30.dp,
                top = 50.dp,
                bottom = 15.dp
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (showBackButton && navController.previousBackStackEntry != null) {
                IconButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back_arrow),
                        contentDescription = "Back",
                        tint = if(variant) EerieBlack else DutchWhite
                    )
                }
            } else
                Spacer(modifier = Modifier.width(40.dp))
            // Icono de la izquierda

            Text(
                text = title.uppercase(),
                style = TextStyle(
                    fontSize = 24.sp,
                    color = if(variant) EerieBlack else DutchWhite,
                    fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_bold)),
                )
            )
            IconButton(
                onClick = { onClick() },
                modifier = Modifier.size(if(variant) 25.dp else 40.dp)
            ) {
                Icon(
                    painter = painterResource(id = if(variant) R.drawable.ic_poo_storm_solid else R.drawable.ic_robot_white),
                    contentDescription = "Hints",
                    tint = EerieBlack
                )
            }
        }

        when(title.uppercase()) {
            "QUIZ" -> {
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier.fillMaxWidth().height(30.dp)
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
                        painter = painterResource(id = R.drawable.ic_rocket),
                        contentDescription = "Progress Indicator",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .fillMaxHeight()
                            .align(Alignment.CenterStart)
                            .offset(x = 340.dp * progress - 10.dp, y = 0.dp)
                    )
                }

                Text(
                    text = currentProgress,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_bold)),
                        color = DutchWhite,
                    ),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
fun HomeTopBar(
    userName: String,
    progress: Float,
    level: Int = 1,
    nextLevel: Int = 80
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
                end = 30.dp,
                top = 55.dp,
                bottom = 20.dp
            ),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Hola, $userName 👋",
            style = TextStyle(
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_bold)),
                color = DutchWhite
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Progreso de aprendizaje:",
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_semibold_italic)),
                color = DutchWhite
            )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
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
                painter = painterResource(id = R.drawable.ic_rocket),
                contentDescription = "Progress Indicator",
                tint = Color.Unspecified,
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterStart)
                    .offset(x = progress.dp * 320)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Nivel actual: $level",
                style = TextStyle(
                    fontSize = 10.sp,
                    fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_extrabold_italic)),
                    color = DutchWhite
                )
            )

            Text(
                text = "$nextLevel / 150 XP",
                style = TextStyle(
                    fontSize = 10.sp,
                    fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_extrabold_italic)),
                    color = DutchWhite
                )
            )
        }
    }
}