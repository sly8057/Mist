package com.example.mist.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mist.AuthViewModel
import com.example.mist.R
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
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(EerieBlack),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
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
            onValueChange = { email = it },
            label = { Text(text = "Correo electrónico") }
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
                val icon =
                    if (passwordVisible) getImageVectorFromDrawable(R.drawable.ic_lock) else getImageVectorFromDrawable(
                        R.drawable.ic_lock_open
                    )
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = if (passwordVisible) "Contraseña visible" else "Contraseña oculta",
                        tint = DutchWhite.copy(alpha = 0.5f)
                    )
                }
            },
            value = password,
            onValueChange = { newText ->
                val filteredText = newText.filter{ it != ' ' && it != '\n' && it != '\t' }
                password = filteredText },
            label = { Text(text = "Contraseña") },

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
            visualTransformation = if (passwordConfirmationVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon =
                    if (passwordConfirmationVisible) getImageVectorFromDrawable(R.drawable.ic_lock) else getImageVectorFromDrawable(
                        R.drawable.ic_lock_open
                    )
                IconButton(onClick = {
                    passwordConfirmationVisible = !passwordConfirmationVisible
                }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = if (passwordConfirmationVisible) "Contraseña visible" else "Contraseña oculta",
                        tint = DutchWhite.copy(alpha = 0.5f)
                    )
                }
            },
            value = passwordConfirmation,
            onValueChange = { newText ->
                val filteredText = newText.filter{ it != ' ' && it != '\n' && it != '\t' }
                passwordConfirmation = filteredText },
            label = { Text(text = "Confirma la contraseña") },
        )

        Row(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
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

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Realizar quiz de afición",
                style = TextStyle(
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_regular))
                )
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            modifier = Modifier
                .width(170.dp)
                .height(50.dp),
            shape = RoundedCornerShape(size = 20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onBackground
            ),
            onClick = { navController.navigate("home") }
        ) {
            Text(
                text = "Regístrate",
                // MonoText
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_bold)),
                )
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

        TextButton(onClick = { navController.navigate("login") }) {
            Text(
                text = "Ya tienes cuenta? Inicia sesión",
                style = TextStyle(
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_bold))
                )
            )
        }

    }
}

fun isValidPassword(password: String): Boolean{
    return !password.contains(Regex("\\s"))
}