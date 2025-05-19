package com.example.mist.popup

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mist.AuthViewModel
import com.example.mist.R

@Composable
fun TakeQuizPopUp(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    navController: NavController,
) {

    AlertDialog(onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(
                    "Sí",
                    style = TextStyle(fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_extrabold)))
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    "No",
                    style = TextStyle(fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_extrabold)))
                )
            }
        },

        modifier = Modifier
        //.height(IntrinsicSize.Min)
        ,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Info, contentDescription = null)
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Quiz de aficiones",
                    style = TextStyle(fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_bold))),
                    fontSize = 20.sp,
                )
            }
        },
        text = {
            Row {
                Text(
                    text = "¿Quieres tomar el quiz de aficiones de nuevo?",
                    style = TextStyle(fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_regular)))
                )
            }
        })

}