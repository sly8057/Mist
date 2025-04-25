package com.example.mist.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.mist.AuthState
import com.example.mist.AuthViewModel
import com.example.mist.components.CustomBottomBar
import com.example.mist.components.DefaultTopBar
import com.example.mist.models.LessonViewModel
import com.example.mist.models.UserLesson
import com.example.mist.ui.theme.backgroundColor

@Composable
fun PlaygroundPage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    lessonViewModel: LessonViewModel
) {
    val lesson = lessonViewModel.selectedUserLesson

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
                onClick = { navController.navigate("explore") }
            )
        }
    ) { innerPadding ->
        if(lesson != null) {
            PlaygroundContent(
                modifier.padding(innerPadding),
                lesson = lesson
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
fun PlaygroundContent(modifier: Modifier = Modifier, lesson: UserLesson) {
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
            Text(text = "Lección seleccionada: ${lesson.title}", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Objetivo: ${lesson.goal}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Instrucciones: ${lesson.brief}")


            Spacer(modifier = Modifier.height(16.dp))
            EjecutarSaludo()
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

@Composable
fun EjecutarSaludo() {
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        TextField(
            value = nombre,
            onValueChange = {nombre = it},
            label = {
                Text("Nombre")
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                try {
                    if(!Python.isStarted()) {
                        Python.start(AndroidPlatform(context))
                        //Python.start(new AndroidPlatform(context))
                    }

                    val py = Python.getInstance()
                    val module = py.getModule("mist_ejemplo")
                    resultado = module.callAttr("saluda", nombre).toString()
                } catch (e: Exception) {
                    resultado = "Error: ${e.message}"
                }

            }
        ) {
            Text("Ejecutar")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Resultado: $resultado")
    }
}