package com.example.mist

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mist.pages.HomePage
import com.example.mist.pages.LoginPage
import com.example.mist.pages.SignupPage
import com.example.mist.pages.StartPage

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "start", builder = {
        composable("start") {
            StartPage(modifier, navController, authViewModel)
        }
        composable("login") {
            LoginPage(modifier, navController, authViewModel)
        }
        composable("signup") {
            SignupPage(modifier = modifier, navController = navController, authViewModel = authViewModel)
        }
        composable("home") {
            HomePage(modifier = modifier, navController = navController, authViewModel = authViewModel)
        }
    })
}