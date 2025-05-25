package com.example.mist.pages

import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mist.AuthState
import com.example.mist.AuthViewModel
import com.example.mist.components.PrimaryButton
import com.example.mist.components.PrimaryInputField
import com.example.mist.components.PrimaryTextButton
import com.example.mist.components.SecondaryTextButton
import com.example.mist.ui.theme.*

@Composable
fun LoginPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> navController.navigate("home")
            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT
            ).show()

            else -> Unit
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "loginBanner")
    val targetOffset = with(LocalDensity.current) { 1000.dp.toPx() }
    val bannerOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = targetOffset,
        animationSpec = infiniteRepeatable(
            tween(50000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bannerOffset"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            //.blur(40.dp)
            .drawWithCache {
                val brushSize = 400f
                val brush = Brush.linearGradient(
                    colors = mainColorGradient,
                    start = Offset(bannerOffset, bannerOffset),
                    end = Offset(bannerOffset + brushSize, bannerOffset + brushSize),
                    tileMode = TileMode.Mirror
                )
                onDrawBehind { drawRect(brush) }
            },
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier
                .height(250.dp)
                .blur(40.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(EerieBlack),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {

            Spacer(modifier = Modifier.height(50.dp))

            PrimaryInputField(
                value = email,
                onValueChange = { email = it },
                label = "Correo electrónico",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )


            Spacer(modifier = Modifier.height(20.dp))

            PrimaryInputField(
                value = password,
                onValueChange = { password = it },
                label = "Contraseña",
                isPasswordField = true,
                passwordVisible = passwordVisible,
                onVisibilityToggle = { passwordVisible = !passwordVisible },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            PrimaryTextButton(
                text = "¿Olvidaste la contraseña?",
                onClick = {
                    authViewModel.sendPasswordResetEmail(email) { success, errorMessage ->
                        if (success) {
                            Toast.makeText(context, "Correo de recuperación enviado", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 20.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            PrimaryButton(
                text = "Inicia sesión",
                onClick = { authViewModel.login(email, password) },
                enabled = authState.value != AuthState.Loading
            )

            Spacer(modifier = Modifier.height(50.dp))

            SecondaryTextButton(
                text = "No tienes cuenta? Registrate",
                onClick = { navController.navigate("signup") })

        }

    }
}