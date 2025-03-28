package com.example.mist.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mist.AuthViewModel
import com.example.mist.R
import com.example.mist.ui.theme.*

@Composable
fun StartPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(backgroundColor)){
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

            Button (
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground,
                        shape = RoundedCornerShape(size = 20.dp)
                    )
                    .width(170.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(size = 20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
                onClick = { navController.navigate("login") }
            ) {
                Text(
                    text = "Inicia Sesión",
                    // MonoText
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_bold)),
                        // color = MaterialTheme.colorScheme.onBackground,
                    )
                )
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
                onClick = { navController.navigate("signup") }
            ) {
                Text(
                    text = "Registrate",
                    // MonoText
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_bold)),
                        // color = MaterialTheme.colorScheme.onBackground,
                    )
                )
            }
        }
    }
}