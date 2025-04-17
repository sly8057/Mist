package com.example.mist.pages


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.mist.AuthState
import com.example.mist.AuthViewModel
import com.example.mist.components.CustomBottomBar
import com.example.mist.components.DefaultTopBar
import com.example.mist.ui.theme.backgroundColor

@Composable
fun ProfilePage(modifier: Modifier = Modifier, navController: NavHostController, authViewModel: AuthViewModel) {

    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when(authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("start")
            else -> Unit
        }
    }

    Scaffold (
        modifier = Modifier.padding(bottom = 40.dp),
        bottomBar = { CustomBottomBar(navController) },
        topBar = { DefaultTopBar(
            title = "Perfil",
            navController = navController,
            mostrarBorde = true,
            nivelUsuario = 10,
            onClick = { navController.navigate("quiz") }) }
    ) { innerPadding ->
        ProfileContent(modifier.padding(innerPadding), authViewModel)
    }
}

@Composable
fun ProfileContent(modifier: Modifier = Modifier, authViewModel: AuthViewModel){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ){
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Profile Page", fontSize = 32.sp)

            TextButton(onClick = {
                authViewModel.signout()
            }) {
                Text(text = "Sign out")
            }
        }
    }
}
