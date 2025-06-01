package com.example.mist.pages

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavHostController
import com.example.mist.AuthState
import com.example.mist.AuthViewModel
import com.example.mist.R
import com.example.mist.components.CustomBottomBar
import com.example.mist.components.CustomTopBar
import android.util.Log
import com.example.mist.components.DefaultTopBar
import com.example.mist.models.User
import com.example.mist.ui.theme.DutchWhite
import com.example.mist.ui.theme.EerieBlack
import com.example.mist.ui.theme.Night
import com.example.mist.ui.theme.backgroundColor
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.math.Stats
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun ProfilePage(modifier: Modifier = Modifier, navController: NavHostController, authViewModel: AuthViewModel) {

    val authState = authViewModel.authState.observeAsState()

    var showLogoutPopup by remember { mutableStateOf(false) }
    if(showLogoutPopup) com.example.mist.popup.SignOutPopUp(
        onDismiss = { showLogoutPopup = false },
        onConfirm = {
            authViewModel.signout()
            showLogoutPopup = false
        },
        navController,
        authViewModel
    )

    LaunchedEffect(authState.value) {
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
            CustomTopBar(
                title = "Perfil",
                showBackButton = true,
                onClick = { showLogoutPopup = true },
                navController = navController
            )
        }
    ) { innerPadding ->
        ProfileContent(modifier.padding(innerPadding), authViewModel, navController)
    }
}

@Composable
fun ProfileContent(modifier: Modifier = Modifier, authViewModel: AuthViewModel, navController: NavHostController){
    val userData by authViewModel.userData.collectAsState()

    LaunchedEffect(userData) {
        Log.d("Profile Content", "Información del usuario: $userData")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 20.dp)
    ){
        Column(
            modifier = modifier.fillMaxSize(),
            //verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.25f))

            Image(
                painter = painterResource(R.drawable.ic_user_astronaut),
                contentDescription = "avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .border(14.dp, DutchWhite, CircleShape)
            )

            //Spacer(modifier = Modifier.weight(1f))

            userData?.let {
                Text(
                    text = it.nickname,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_regular)),
                        fontWeight = FontWeight(700),
                        color = DutchWhite,
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier
                        .padding(top = 14.dp)
                )
            }

            userData?.email?.let {
                Text(
                    text = it,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_regular)),
                        fontWeight = FontWeight(300),
                        color = DutchWhite,
                        textAlign = TextAlign.Center,
                        textDecoration = TextDecoration.Underline
                    ),
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                StatsCard("26", "Lecciones aprobadas", navController = navController)

                StatsCard("Editar perfil", " ", editProfile = true, navController = navController)

                userData?.let { StatsCard(it.hobby, "Repetir quiz", navController = navController) }
            }
            Spacer(modifier = Modifier.weight(1f))

//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 20.dp),
//                    //horizontalArrangement = Arrangement.SpaceBetween,
//            ){
//                Row(
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                ){
//                    OutlinedButton(
//                        onClick = { TODO() },
//                        border = BorderStroke(2.dp, DutchWhite),
//                        shape = RoundedCornerShape(20.dp),
//                        colors = ButtonDefaults.outlinedButtonColors(
//                            containerColor = Night,
//                            contentColor = DutchWhite
//                        ),
//                        contentPadding = PaddingValues(horizontal = 12.dp),
//                        modifier = Modifier
//                            .wrapContentWidth()
//                    ){
//                        Text(text = "Lecciones tomadas",
//                            style = TextStyle(
//                                fontSize = 10.sp,
//                                fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_regular)),
//                                fontWeight = FontWeight(300)
//                            )
//                        )
//                    }
//
//                    OutlinedButton(
//                        onClick = { TODO() },
//                        border = BorderStroke(2.dp, DutchWhite),
//                        shape = RoundedCornerShape(20.dp),
//                        colors = ButtonDefaults.outlinedButtonColors(
//                            containerColor = Night,
//                            contentColor = DutchWhite
//                        ),
//                        contentPadding = PaddingValues(horizontal = 12.dp),
//                        modifier = Modifier
//                            .wrapContentWidth()
//                    ){
//                        Text(text = "About",
//                            style = TextStyle(
//                                fontSize = 10.sp,
//                                fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_regular)),
//                                fontWeight = FontWeight(300)
//                            )
//                        )
//                    }
//                }
//
//                IconButton(
//                    onClick = {},
//                    modifier = Modifier
//
//                ) { }
//
//
//            }
        }
    }
}

@Composable
fun StatsCard(
    quantity: String,
    category: String,
    hobby: Boolean = false,
    editProfile: Boolean = false,
    navController: NavHostController
){
    var showTakeQuizPopUp by remember { mutableStateOf(false) }
    if(showTakeQuizPopUp) com.example.mist.popup.TakeQuizPopUp(
        onDismiss = { showTakeQuizPopUp = false },
        onConfirm = {
            navController.navigate("quiz")
            showTakeQuizPopUp = false
        },
        navController
    )

    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = Night
        ),
        border = BorderStroke(2.dp, DutchWhite),
        modifier = Modifier
            .width(110.dp)
            .height(80.dp)
            .then(
                if(hobby || editProfile){
                Modifier.clickable {
                    when {
                        hobby -> { showTakeQuizPopUp = true }
                        editProfile -> navController.navigate("editProfile")
                    }
                }
            } else {Modifier}
            )
            //.fillMaxWidth()
    ) {
        Column (
            modifier = Modifier
                .padding(4.dp)
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = quantity,
                style = TextStyle(
                    fontSize = /*if(hobby)*/ 16.sp /*else 20.sp*/,
                    fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_regular)),
                    fontWeight = FontWeight(800),
                    color = DutchWhite,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth(),
            )

            if(!hobby) {
                Text(
                    text = category,
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_regular)),
                        fontWeight = FontWeight(400),
                        color = DutchWhite,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .fillMaxWidth()

                )
            }
        }
    }
}

@Composable
fun LessonCard(image: Int, lesson: String, progress: Int){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = EerieBlack
        ),
        modifier = Modifier
            .height(65.dp)
            .fillMaxWidth()
    ){
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 15.dp, vertical = 2.dp)
                ,
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .width(40.dp)
                    ,
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        //.padding(10.dp)
                        ,
                    onDraw = {
                        drawCircle(DutchWhite)
                    }
                )

                Icon(painter = painterResource(id = image),
                    contentDescription = "",
                    tint = EerieBlack,
                    modifier = Modifier
                        .padding(5.dp),
                )
            }

            Spacer(modifier = Modifier.width(15.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = lesson,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_regular)),
                        fontWeight = FontWeight(500),
                        color = DutchWhite,
                        textAlign = TextAlign.Start,
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier
                        .width(210.dp)
                )

                Text(
                    text = "$progress %",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_regular)),
                        fontWeight = FontWeight(200),
                        color = DutchWhite
                    ),
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                )

            }
        }
    }

    Spacer(modifier = Modifier.height(10.dp))

}