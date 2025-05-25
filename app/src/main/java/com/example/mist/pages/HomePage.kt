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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.collectAsState
import com.example.mist.models.UserLesson
import com.google.firebase.auth.FirebaseAuth

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
//                userName = "Whispersoul",
                userName = "${FirebaseAuth.getInstance().currentUser?.email}",
                progress = progress
            )
        }
    ) { innerPadding ->
        HomeContent(
            innerPadding = innerPadding,
            navController = navController,
            lessonsViewModel = lessonViewModel
        )
    }
}

@Composable
fun HomeContent(
    innerPadding: PaddingValues,
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

    val lessonRows = mutableListOf<List<UserLesson>>()
    var index = 0
    var rowCount = 0

    while (index < userlessons.size) {
        if (rowCount % 2 == 0) {
            lessonRows.add(listOf(userlessons[index]))
            index += 1
        } else {
            if (index + 1 < userlessons.size) {
                lessonRows.add(listOf(userlessons[index], userlessons[index + 1]))
            } else {
                lessonRows.add(listOf(userlessons[index]))
            }
            index += 2
        }
        rowCount += 1
    }

    Log.d("HomeContent", "lessonRows: ${lessonRows.size}")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(top = 50.dp),
        contentPadding = innerPadding
    ) {
        item { Spacer(modifier = Modifier.height(16.dp)) }

        items(lessonRows) { row ->
            if (row.size == 1) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    LessonCard(
                        lesson = row[0],
                        onClick = {
                            Log.d("HomeContent", "Lesson clicked: ${row[0]}")
                            lessonsViewModel.selectUserLesson(row[0])
                            Log.d("HomeContent", "Lesson selected: ${lessonsViewModel.selectedUserLesson}")
                            navController.navigate("playground")
                        }
                    )
                }

            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                    //horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    row.forEach { lesson ->
                        LessonCard(
                            lesson = lesson,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                Log.d("HomeContent", "Lesson clicked: ${lesson}")
                                lessonsViewModel.selectUserLesson(lesson)
                                Log.d("HomeContent", "Lesson selected: ${lessonsViewModel.selectedUserLesson}")
                                navController.navigate("playground")
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

@Composable
fun LessonCard(
    lesson: UserLesson,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
){
    val iconResId = when (lesson.icon.lowercase()) {
        "ic_flag_solid" -> R.drawable.ic_flag_solid
        "ic_database" -> R.drawable.ic_database
        "ic_java" -> R.drawable.ic_java
        "ic_git" -> R.drawable.ic_git
        else -> R.drawable.ic_user_astronaut // default
    }

    Box(
        modifier = modifier
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
                //.fillMaxWidth()
                //.height(90.dp)
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
                contentDescription = lesson.goal,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = lesson.title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_semibold)),
                    textAlign = TextAlign.Center,
                    color = DutchWhite
                ),
                modifier = Modifier
                    .width(90.dp)
                    //.fillMaxWidth()
                    //.padding(horizontal = 4.dp)
                    //.height(IntrinsicSize.Max),
            )
        }
    }
}