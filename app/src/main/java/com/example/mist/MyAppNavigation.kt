package com.example.mist

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mist.pages.*

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    val navController = rememberNavController()

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
                authViewModel = authViewModel
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
                authViewModel = authViewModel
            )
        }
        composable("quiz") {
            QuizPage(
                modifier = modifier,
                navController = navController/*, authViewModel = authViewModel*/
            )
        }
    }
}