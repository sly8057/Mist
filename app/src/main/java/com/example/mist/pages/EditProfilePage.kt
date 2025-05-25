package com.example.mist.pages

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.mist.AuthState
import com.example.mist.AuthViewModel
import com.example.mist.components.CustomBottomBar
import com.example.mist.components.CustomTopBar
import com.example.mist.ui.theme.backgroundColor
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.mist.components.PrimaryButton
import com.example.mist.components.PrimaryInputField
import com.example.mist.ui.theme.EerieBlack

@Composable
fun EditProfilePage(modifier: Modifier, navController: NavHostController, authViewModel: AuthViewModel){
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState) {
        when(authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("start")
            is AuthState.Authenticated -> authViewModel.loadUserData()
            else -> Unit
        }
    }

    Scaffold (
        contentWindowInsets = WindowInsets.systemBars,
        bottomBar = { CustomBottomBar(navController) },
        topBar = {
            /*DefaultTopBar(
                title = "Perfil",
                navController = navController,
                mostrarBorde = true,
                nivelUsuario = 10,
                onClick = { authViewModel.signout() }
            )*/
            CustomTopBar(title = "Editar perfil", onClick = {  }, navController = navController)
        }
    ) { innerPadding ->
        EditProfileContent(modifier.padding(innerPadding), authViewModel, navController)
    }
}

@Composable
fun EditProfileContent(modifier: Modifier = Modifier, authViewModel: AuthViewModel, navController: NavHostController){
    val context = LocalContext.current
    val userData by authViewModel.userData.collectAsState()

    var username by remember { mutableStateOf(userData?.nickname ?: "") }
    var profilePicture by remember { mutableStateOf(userData?.profilePicture ?: "") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 20.dp)
    ){
        Column(modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(EerieBlack),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,)
        {
            PrimaryInputField(
                value = username,
                onValueChange = { username = it },
                label = "Nombre de usuario",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            PrimaryInputField(
                value = profilePicture,
                onValueChange = { profilePicture = it },
                label = "URL de foto de perfil",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            PrimaryButton(
                text = "OK",
                onClick = {
                    authViewModel.editUser(username, profilePicture){ success ->
                        if(success) navController.navigate("profile")
                        else {
                            Toast.makeText(context, "Error al actualizar hobby", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }
    }
}