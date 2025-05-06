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
fun QuizPopUp(
    onDismiss: () -> Unit,
    scores: MutableMap<String, Int>,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val authState = authViewModel.authState.observeAsState()
    val maxValue = scores.values.maxOrNull()
    val winnerCategories = scores.filter { it.value == maxValue }
    var winner = ""
    val context = LocalContext.current

    AlertDialog(onDismissRequest = onDismiss,
        confirmButton = {
            if (winnerCategories.size == 1) {
                TextButton(
                    onClick = {
                        val hobby = winnerCategories.keys.first()
                        authViewModel.updateUserHobby(hobby) { success ->
                            if (success) {
                                navController.navigate("home")
                            } else {
                                Toast.makeText(context, "Error al actualizar hobby", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                ) {
                    Text("Excelente!!")
                }
            }
           /* if (winnerCategories.size == 1) {
                TextButton(
                    onClick = {
                        navController.navigate("home")
                    }
                ) {
                    Text(
                        "Excelente!!",
                        style = TextStyle(fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_extrabold)))
                    )
                }
            }*/
        },

        modifier = Modifier
            .height(IntrinsicSize.Min),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Info, contentDescription = null)
                Spacer(Modifier.width(10.dp))
                Text(
                    text = if(winnerCategories.size > 1) "Oooops!" else "Enhorabuena!",
                    style = TextStyle(fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_bold))),
                    fontSize = 20.sp,
                )
            }
        },
        text = {
            if (winnerCategories.size > 1) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    //horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(text = "Pareces ser multifacético. Elige una aficion:",
                        style = TextStyle(fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_regular))),
                        fontSize = 15.sp
                    )
                    Spacer(Modifier.height(20.dp))

                    Column (modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally){
                        Button(
                            onClick = {
                                authViewModel.updateUserHobby(winnerCategories.keys.elementAt(0)) { success ->
                                    if (success) {
                                        navController.navigate("home") {
                                            popUpTo("home") { inclusive = true }
                                        }
                                    } else {
                                        Toast.makeText(context, "Error al actualizar hobby", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        ) {
                            Text(text = winnerCategories.keys.elementAt(0))
                        }
                        /*Button(
                            onClick = {
                                winner = winnerCategories.keys.elementAt(0)
                                //Toast.makeText(context,"Elegiste: $winner", Toast.LENGTH_SHORT).show()
                                navController.navigate("home")
                            },
                            modifier = Modifier
                                .width(180.dp)
                        ) {
                            Text(text = winnerCategories.keys.elementAt(0))
                        }*/

                        Spacer(Modifier.height(10.dp))

                        Button(
                            onClick = {
                                authViewModel.updateUserHobby(winnerCategories.keys.elementAt(1)) { success ->
                                    if (success) {
                                        navController.navigate("home") {
                                            popUpTo("home") { inclusive = true }
                                        }
                                    } else {
                                        Toast.makeText(context, "Error al actualizar hobby", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        ) {
                            Text(text = winnerCategories.keys.elementAt(1))
                        }
                    }
                }
            } else {
                Row {
                    Text(
                        text = "Tu afición es: ",
                        style = TextStyle(fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_regular)))
                    )
                    Text(
                        text = winnerCategories.keys.joinToString(),
                        style = TextStyle(fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_bold)))
                    )
                }
                winner = winnerCategories.keys.joinToString()
            }
        })

}