package com.example.mist

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mist.models.LessonRepository
import com.example.mist.models.LessonViewModel
import com.example.mist.models.LessonViewModelFactory
import com.example.mist.pages.CameraPage
import com.example.mist.pages.ExplorePage
import com.example.mist.pages.HomePage
import com.example.mist.pages.LoginPage
import com.example.mist.pages.PlaygroundPage
import com.example.mist.pages.ProfilePage
import com.example.mist.pages.QuizPage
import com.example.mist.pages.SignupPage
import com.example.mist.pages.StartPage
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MyAppNavigation(
    authViewModel: AuthViewModel,
    modifier: Modifier
) {
    val navController = rememberNavController()

    val repository = LessonRepository()
    val lessonViewModel: LessonViewModel = viewModel(
        factory = LessonViewModelFactory(repository, FirebaseAuth.getInstance())
    )

    NavHost(navController = navController, startDestination = "start") {
        composable("start") {
            StartPage(
                modifier = modifier,
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable("login") {
            LoginPage(
                modifier = modifier,
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable("signup") {
            SignupPage(
                modifier = modifier,
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable("home") {
            HomePage(
                modifier = modifier,
                navController = navController,
                authViewModel = authViewModel,
                lessonViewModel = lessonViewModel
            )
        }
        composable("camera") {
            CameraPage(
                modifier = modifier,
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable("profile") {
            ProfilePage(
                modifier = modifier,
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable("explore") {
            ExplorePage(
                modifier = modifier,
                navController = navController,
                authViewModel = authViewModel,
                lessonViewModel = lessonViewModel
            )
        }
        composable("quiz") {
            QuizPage(
                modifier = modifier,
                navController = navController/*, authViewModel = authViewModel*/
            )
        }
        composable("playground") {
            PlaygroundPage(
                modifier = modifier,
                navController = navController,
                authViewModel = authViewModel,
                lessonViewModel = lessonViewModel
            )
        }
    }
}