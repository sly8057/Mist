package com.example.mist.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mist.AuthState
import com.example.mist.AuthViewModel
import com.example.mist.R
import com.example.mist.components.PrimaryButton
import com.example.mist.components.PrimaryInputField
import com.example.mist.components.SecondaryTextButton
import com.example.mist.ui.theme.*

@Composable
fun SignupPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var passwordConfirmation by remember { mutableStateOf("") }
    var passwordConfirmationVisible by remember { mutableStateOf(false) }

    var isChecked by remember { mutableStateOf(false) }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> {
                if(isChecked)
                    navController.navigate("quiz")
                navController.navigate("home")
            }
            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT
            ).show()

            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(EerieBlack),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
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

        Spacer(modifier = Modifier.height(20.dp))

        PrimaryInputField(
            value = passwordConfirmation,
            onValueChange = { passwordConfirmation = it },
            label = "Confirma la contraseña",
            isPasswordField = true,
            passwordVisible = passwordConfirmationVisible,
            onVisibilityToggle = { passwordConfirmationVisible = !passwordConfirmationVisible },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Row(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .clickable { isChecked = !isChecked },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it },
                modifier = Modifier,
                colors = CheckboxDefaults.colors(
                    checkedColor = Asparagus,
                    uncheckedColor = DutchWhite,
                    checkmarkColor = DutchWhite,
                )
            )

            Text(
                text = if(isChecked) "Realizar quiz de afición" else "No realizar quiz de afición",
                style = TextStyle(
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_regular))
                )
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        PrimaryButton(
            text = "Regístrate",
            onClick = {
                if(password == passwordConfirmation)
                    authViewModel.signup(email, password)
                else
                    Toast.makeText(context, "La contraseña no coincide", Toast.LENGTH_SHORT).show()
            },
            enabled = authState.value != AuthState.Loading
        )

        Spacer(modifier = Modifier.height(50.dp))

        SecondaryTextButton(
            text = "Ya tienes cuenta? Inicia sesión",
            onClick = { navController.navigate("login") }
        )

    }
}