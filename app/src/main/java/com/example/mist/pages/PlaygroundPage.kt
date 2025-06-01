package com.example.mist.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mist.AuthState
import com.example.mist.AuthViewModel
import com.example.mist.R
import com.example.mist.components.CustomBottomBar
import com.example.mist.components.CustomTopBar
import com.example.mist.components.DefaultTopBar
import com.example.mist.models.LessonViewModel
import com.example.mist.models.UserLesson
import com.example.mist.ui.theme.DutchWhite
import com.example.mist.ui.theme.EerieBlack
import com.example.mist.ui.theme.Night
import com.example.mist.ui.theme.backgroundColor

@Composable
fun PlaygroundPage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    lessonViewModel: LessonViewModel
) {
    val lesson = lessonViewModel.selectedUserLesson
    var testResult by remember { mutableStateOf("") }

    val context = LocalContext.current

    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("start")
            else -> Unit
        }
    }

    Scaffold(
        //modifier = Modifier.padding(bottom = 40.dp),
        contentWindowInsets = WindowInsets.systemBars,
        bottomBar = { CustomBottomBar(navController) },
        topBar = {
            CustomTopBar(
                title = "Playground",
                showBackButton = true,
                onClick = {  },
                variant = true,
                navController = navController
            )
        }
    ) { innerPadding ->
        if(lesson != null) {
            PlaygroundContent(
                modifier.padding(innerPadding),
                lesson = lesson,
                onSave = { newCode ->
                    lessonViewModel.updateUserLessonCode(lesson.lessonId, newCode)
                },
                onRun = {
                    //lessonViewModel.runUserCodeTest(context, lesson.lessonId)
                    lessonViewModel.runCurrentLessonTest(context, lesson) { result ->
                        testResult = result
                    }
                },
                testResult = testResult
            )
        } else {
            PlaygroundContent(
                modifier.padding(innerPadding),
                authViewModel
            )
        }
    }
}

@Composable
fun PlaygroundContent(
    modifier: Modifier = Modifier,
    lesson: UserLesson,
    onSave: (String) -> Unit = {},
    onRun: () -> Unit = {},
    testResult: String = ""
) {
    var codeText by remember { mutableStateOf(lesson.pythonCode) }
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(EerieBlack)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Lección seleccionada:\n\t\t${lesson.title}", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Objetivo: ${lesson.goal}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Instrucciones: ${lesson.brief}")
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = codeText,
                onValueChange = { codeText = it },
                label = { Text("index.py") },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                maxLines = Int.MAX_VALUE,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Night.copy(alpha = 0.1f),
                    unfocusedContainerColor = Night.copy(alpha = 0.1f),
                    focusedTextColor = DutchWhite,
                    unfocusedTextColor = DutchWhite.copy(alpha = 0.5f)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                TextButton (
                    modifier = Modifier
                        .width(130.dp)
                        .height(40.dp)
                        .background(
                            color = DutchWhite,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    onClick = { onSave(codeText) },
                ) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_floppy_disk_solid),
                            contentDescription = "Guardar",
                            tint = EerieBlack
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text (
                            text = "Guardar",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = EerieBlack,
                                fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_semibold))
                            )
                        )
                    }
                }

                TextButton (
                    modifier = Modifier
                        .width(130.dp)
                        .height(40.dp)
                        .background(
                            color = DutchWhite,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    onClick = { onRun() },
                ) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_play_solid),
                            contentDescription = "Ejecutar",
                            tint = EerieBlack
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Ejecutar",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = EerieBlack,
                                fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_semibold))
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .width(350.dp)
                    .height(1.dp)
                    .drawBehind {
                        val strokeWidth = 1.dp.toPx()
                        drawLine(
                            color = DutchWhite.copy(alpha = 0.5f),
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = strokeWidth
                        )
                    }
            )

            //EjecutarSaludo()
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                value = testResult,
                onValueChange = {},
                label = { Text("Resultado") },
                readOnly = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = EerieBlack,
                    unfocusedContainerColor = EerieBlack,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = DutchWhite,
                    unfocusedTextColor = DutchWhite.copy(alpha = 0.5f)
                )
            )
        }
    }
}

@Composable
fun PlaygroundContent(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Playground Page", fontSize = 32.sp)
            Text(text = "No se seleccionó ninguna lección", fontSize = 16.sp)

            TextButton(onClick = {
                authViewModel.signout()
            }) {
                Text(text = "Sign out")
            }
            Spacer(modifier = Modifier.height(16.dp))
            //EjecutarSaludo()
        }
    }
}