package com.example.mist.pages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mist.AuthViewModel
import com.example.mist.AuthState
import com.example.mist.R
import com.example.mist.components.PrimaryButton
import com.example.mist.components.SecondaryButton
import com.example.mist.ui.theme.*

@Composable
fun StartPage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> navController.navigate("home")
            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message,
                Toast.LENGTH_SHORT
            ).show()

            else -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 55.dp, top = 88.dp, end = 55.dp, bottom = 248.dp),
            //.background(color = MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .width(250.dp)
                    .height(250.dp),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "logo de la aplicación",
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(100.dp))

            SecondaryButton(text = "Inicia Sesión") { navController.navigate("login") }

            Spacer(modifier = Modifier.height(30.dp))

            PrimaryButton(text = "Registrate", onClick = { navController.navigate("signup") })
        }
    }
}