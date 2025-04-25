package com.example.mist.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import androidx.navigation.NavHostController
import com.example.mist.AuthState
import com.example.mist.AuthViewModel
import com.example.mist.R
import com.example.mist.components.CustomBottomBar
import com.example.mist.components.HomeTopBar
import com.example.mist.models.LessonViewModel
import com.example.mist.ui.theme.DutchWhite
import com.example.mist.ui.theme.EerieBlack
import com.example.mist.ui.theme.ForestGreen
import com.example.mist.ui.theme.backgroundColor
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.collectAsState

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    lessonViewModel: LessonViewModel
) {
    val progress = 50f / 100f

    val authState by authViewModel.authState.observeAsState()

    LaunchedEffect(authState) {
        when(val state = authState) {
            is AuthState.Unauthenticated -> navController.navigate("start")
            is AuthState.Authenticated -> {
                Log.d("HomePage", "uid: ${state.uid}")
                lessonViewModel.loadUserLesson(state.uid)
                Log.d("HomePage", "Lecciones del usuario: ${lessonViewModel.allUserLessons.value}")
            }
            else -> Unit
        }
    }



    Scaffold(
        //modifier = Modifier.padding(bottom = 40.dp),
        contentWindowInsets = WindowInsets.systemBars,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("explore") },
                containerColor = ForestGreen,
                contentColor = DutchWhite
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Agregar lección"
                )
            }
        },
        bottomBar = { CustomBottomBar(navController) },
        topBar = {
            HomeTopBar(
                userName = "Whispersoul",
                progress = progress
            )
        }
    ) { innerPadding ->
        HomeContent(
            modifier = modifier.padding(innerPadding),
            navController = navController,
            lessonsViewModel = lessonViewModel
        )
    }

}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    lessonsViewModel: LessonViewModel
) {
    val userlessons by lessonsViewModel.userLessons.collectAsState() //?: remember { mutableStateOf(emptyList()) }

    /*LaunchedEffect(lessons) {
        println("Lessons received: ${lessons.size}")
    }*/

    LaunchedEffect(userlessons) {
        println("Lecciones desde Firestore: $userlessons")
        Log.d("HomeContent", "Lecciones desde Firestore: $userlessons")
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            //.padding(top = 80.dp, start = 40.dp, end = 40.dp)
            //.padding(top = 100.dp, bottom = 120.dp)
            .background(backgroundColor),
    ) {
        if(userlessons.isEmpty()){
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.Center
            ) {
                AddLessonButton(
                    onClick = {
                        navController.navigate("explore")
                    },
                    modifier = Modifier.padding(vertical = 20.dp)
                )
            }

        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 50.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                //contentPadding = PaddingValues(top = 100.dp, bottom = 120.dp)
            ) {
                itemsIndexed(userlessons.chunked(2)) { _, pair ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        //.height(IntrinsicSize.Max),
                        horizontalArrangement = if (pair.size == 1) Arrangement.Center else Arrangement.SpaceEvenly
                    ) {
                        pair.forEach { lesson ->
                            Lesson(
                                icon = lesson.icon,
                                title = lesson.title,
                                description = lesson.goal,
                                modifier = Modifier
                                    .weight(1f),
                                //.clickable {}
                                onClick = {
                                    Log.d("HomeContent", "Lesson clicked: $lesson")
                                    lessonsViewModel.selectUserLesson(lesson)
                                    Log.d("HomeContent", "Lesson selected: ${lessonsViewModel.selectedUserLesson}")
                                    navController.navigate("playground")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun AddLessonButton(onClick: () -> Unit, modifier: Modifier) {
    Box(
        modifier = Modifier
            .drawWithCache {
                val roundedPolygon = RoundedPolygon(
                    numVertices = 6,
                    radius = size.minDimension / 1.1f,
                    centerX = size.width / 2,
                    centerY = size.height / 2,
                    rounding = CornerRounding(
                        radius = size.minDimension / 5f,
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
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .drawWithCache {
                    val roundedPolygon = RoundedPolygon(
                        numVertices = 6,
                        radius = size.minDimension / 1.3f,
                        centerX = size.width / 2,
                        centerY = size.height / 2,
                        rounding = CornerRounding(
                            radius = size.minDimension / 5f,
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
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = "Agregar lección",
                tint = DutchWhite,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
fun Lesson(
    icon: String,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val iconResId = when (icon.lowercase()) {
        "ic_flag_solid" -> R.drawable.ic_flag_solid
        "ic_database" -> R.drawable.ic_database
        "ic_java" -> R.drawable.ic_java
        "ic_git" -> R.drawable.ic_git
        else -> R.drawable.ic_user_astronaut // default
    }

    Box(
        modifier = Modifier
            .drawWithCache {
                val roundedPolygon = RoundedPolygon(
                    numVertices = 6,
                    radius = size.minDimension / 1.1f,
                    centerX = size.width / 2,
                    centerY = size.height / 2,
                    rounding = CornerRounding(
                        radius = size.minDimension / 5f,
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
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .drawWithCache {
                    val roundedPolygon = RoundedPolygon(
                        numVertices = 6,
                        radius = size.minDimension / 1.3f,
                        centerX = size.width / 2,
                        centerY = size.height / 2,
                        rounding = CornerRounding(
                            radius = size.minDimension / 5f,
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
                painter = painterResource(id = iconResId),
                contentDescription = description,
                modifier = Modifier.size(35.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_semibold)),
                    textAlign = TextAlign.Center,
                    color = DutchWhite
                ),
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
            )
        }
    }
}