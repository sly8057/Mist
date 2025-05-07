package com.example.mist.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mist.AuthState
import com.example.mist.AuthViewModel
import com.example.mist.components.CustomBottomBar
import com.example.mist.components.DefaultTopBar
import com.example.mist.models.LessonViewModel
import com.example.mist.models.UserLesson
import com.example.mist.ui.theme.DutchWhite
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
            DefaultTopBar(
                title = "Playground",
                navController = navController,
                onClick = {}
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Lecci贸n seleccionada: ${lesson.title}", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Objetivo: ${lesson.goal}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Instrucciones: ${lesson.brief}")
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = codeText,
                onValueChange = { codeText = it },
                label = { Text("C贸digo de Python") },
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
                Button(onClick = { onSave(codeText) }) {
                    Text(text = "Guardar")
                }
                Button(onClick = { onRun() }) {
                    Text(text = "Ejecutar")
                }
            }

            //EjecutarSaludo()

            TextField(
                value = testResult,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                label = { Text("Resultado") },
                readOnly = true
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