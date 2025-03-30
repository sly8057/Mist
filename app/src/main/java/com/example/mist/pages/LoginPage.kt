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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mist.AuthState
import com.example.mist.AuthViewModel
import com.example.mist.R
import com.example.mist.ui.theme.*

@Composable
fun LoginPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Authenticated -> navController.navigate("home")
            is AuthState.Error -> Toast.makeText(context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "loginBanner")
    val targetOffset = with(LocalDensity.current) { 1000.dp.toPx() }
    val bannerOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = targetOffset,
        animationSpec = infiniteRepeatable(tween(50000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse),
        label = "bannerOffset"
    )

    Column(modifier = Modifier
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
            onDrawBehind {drawRect(brush)}
        },
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(250.dp).blur(40.dp))

        Column(modifier =  Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(EerieBlack),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            /*
            Row (
                modifier = Modifier.fillMaxWidth()
                    .background(backgroundColor)
                    .height(200.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                //Text(text = "Login Page", fontSize = 32.sp)
            }

            Row (
                modifier = Modifier.fillMaxWidth()
                    .height(50.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Login Page", fontSize = 24.sp)
            }

             */

            Spacer(modifier = Modifier.height(50.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Night.copy(alpha = 0.1f),
                    unfocusedContainerColor = Night.copy(alpha = 0.1f),
                    focusedTextColor = DutchWhite,
                    unfocusedTextColor = DutchWhite.copy(alpha = 0.5f)
                ),
                value = email,
                onValueChange = { newText ->
                    val filteredText = newText.filter{ it != ' ' && it != '\n' && it != '\t' }
                    email = filteredText },
                label = { Text(text = "Correo electrónico") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Night.copy(alpha = 0.1f),
                    unfocusedContainerColor = Night.copy(alpha = 0.1f),
                    focusedTextColor = DutchWhite,
                    unfocusedTextColor = DutchWhite.copy(alpha = 0.5f)
                ),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisible) getImageVectorFromDrawable(R.drawable.ic_lock) else getImageVectorFromDrawable(R.drawable.ic_lock_open)
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            //imageVector = if (passwordVisible) Icons.Outlined.Lock else Icons.Outlined.LockOpen,
                            imageVector = icon,
                            contentDescription = if (passwordVisible) "Contraseña visible" else "Contraseña oculta",
                            tint = DutchWhite.copy(alpha = 0.5f)
                    )}
                },
                value = password,
                onValueChange = { newText ->
                    val filteredText = newText.filter{ it != ' ' && it != '\n' && it != '\t' }
                    password = filteredText },
                label = { Text(text = "Contraseña") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(4.dp))

            TextButton(onClick = { }, modifier = Modifier.align(Alignment.End).padding(end = 20.dp)) {
                Text(text = "Olvidaste la contraseña?",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_bold))))
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button (
                modifier = Modifier
                    .width(170.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(size = 20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
                onClick = {
                    authViewModel.login(email,password)
                },
                    enabled = authState.value != AuthState.Loading
            ) {
                Text(
                    text = "Inicia sesión",
                    // MonoText
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_bold)),
                    )
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            TextButton(onClick = { navController.navigate("signup") }) {
                Text(text = "No tienes cuenta? Registrate",
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_bold))))
            }

        }

    }
}