package com.example.mist.popup

import android.widget.Toast
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mist.AuthViewModel
import com.example.mist.R
import com.example.mist.ui.theme.*

@Composable
fun LeaveQuizPopUp(
    onDismiss: () -> Unit,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current

    AlertDialog( onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = DarkGreen)
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "¿Deseas salir del quiz?",
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = DarkGreen,
                        fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_bold)),
                    )
                )
            }
        },
        text = {
            Text(
                text = "Si continuas con esta operacion, no recibirás ejercicios relacionados con una aficion en especifico. Por el contrario, prefieres practicar con ejercicios aleatorios (de cualquier aficion).",
                style = TextStyle(
                    fontSize = 15.sp,
                    color = EerieBlack,
                    fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_regular))
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            ) {
                Text(
                    text = "Continuar",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = ForestGreen,
                        textDecoration = TextDecoration.Underline,
                        fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_extrabold))
                    )
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() }
            ) {
                Text(
                    text = "Cancelar",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = EerieBlack,
                        textDecoration = TextDecoration.Underline,
                        fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_extrabold))
                    )
                )
            }
        }
    )
}