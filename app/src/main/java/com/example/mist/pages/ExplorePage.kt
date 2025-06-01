package com.example.mist.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mist.AuthViewModel
import com.example.mist.components.CustomBottomBar
import com.example.mist.components.DefaultTopBar
import com.example.mist.ui.theme.backgroundColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.example.mist.R
import com.example.mist.components.CustomTopBar
import com.example.mist.components.SecondaryInputField
import com.example.mist.models.Lesson
import com.example.mist.models.LessonViewModel
import com.example.mist.ui.theme.DutchWhite
import com.example.mist.ui.theme.EerieBlack
import com.example.mist.ui.theme.ForestGreen

@Composable
fun ExplorePage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    lessonViewModel: LessonViewModel
) {
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

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        bottomBar = { CustomBottomBar(navController) },
        topBar = {
            CustomTopBar(
                title = "Explora Lecciones",
                showBackButton = true,
                onClick = { showLogoutPopup = true },
                navController = navController
            )
        }
    ) { innerPadding ->
        ExploreContent(
            modifier = modifier.padding(innerPadding),
            navController = navController,
            authViewModel = authViewModel,
            lessonViewModel = lessonViewModel
        )
    }
}

@Composable
fun ExploreContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    lessonViewModel: LessonViewModel,
) {
    val context = LocalContext.current
    val lessons by lessonViewModel.allLessons.collectAsState()

    // searchBar variables
    var searchQuery by remember { mutableStateOf("") }
    var selectedLevel by remember { mutableStateOf("Todos") }
    var selectedHobby by remember { mutableStateOf("Todos") }

    val levels = listOf("Todos", "Básico", "Intermedio", "Difícil")
    val hobbies = listOf("Todos", "Deportes", "Artes", "Juegos", "Literatura")

    val filteredLessons = lessons.filter { lesson ->
        (searchQuery.isBlank() || lesson.title.contains(searchQuery, ignoreCase = true)) &&
                (selectedLevel == "Todos" || lesson.level.equals(selectedLevel, ignoreCase = true)) &&
                (selectedHobby == "Todos" || lesson.hobby.equals(selectedHobby, ignoreCase = true))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(modifier = modifier.padding(16.dp)) {
            // Search Field
            SecondaryInputField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = "Buscar lección...",
                trailingIcon = R.drawable.ic_search,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Filtros
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DropdownFilter(
                    label = "Dificultad",
                    options = levels,
                    selectedOption = selectedLevel,
                    onOptionSelected = { selectedLevel = it }
                )
                DropdownFilter(
                    label = "Afición",
                    options = hobbies,
                    selectedOption = selectedHobby,
                    onOptionSelected = { selectedHobby = it }
                )
            }

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredLessons) { lesson ->
                    LessonCard(
                        lesson = lesson,
                        onClick = {
                            lessonViewModel.selectLesson(lesson)
                        }
                    )
                }
            }
        }
    }

    if(lessonViewModel.showLessonDialog) {
        val selected = lessonViewModel.selectedLesson
        selected?.let { lesson ->
            AlertDialog(
                onDismissRequest = { lessonViewModel.dismissLessonDialog() },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AddCircle, contentDescription = null)
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = lesson.title,
                            style = TextStyle(fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_bold))),
                            fontSize = 20.sp,
                        )
                    }
                },
                text = {
                    Column {
                        //Row {
                            Text(text = "Dificultad: ${lesson.level}",
                                style = TextStyle(fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_regular))),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Afición: ${lesson.hobby}",
                                style = TextStyle(fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_regular))),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                        //}
                        Text(text = "Objetivo: ",
                            style = TextStyle(fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_regular))),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = lesson.goal,
                            style = TextStyle(fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_regular))),
                            fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Instrucciones: ",
                            style = TextStyle(fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_regular))),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold)
                        lesson.brief.forEachIndexed { index, instruction ->
                            Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(start = 8.dp)) {
                                /*Text(
                                    text = "${index + 1}. ",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    style = TextStyle(fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_regular)))
                                )*/
                                Text(
                                    text = instruction,
                                    fontSize = 14.sp,
                                    style = TextStyle(fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_regular)))
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        val userId = authViewModel.getCurrentUserId()
                        if (userId != null) {
                            lessonViewModel.addLessonToUser(context, lesson)
                            Toast.makeText(context, "Lección añadida al Home", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(context, "Usuario no autenticado", Toast.LENGTH_SHORT)
                                .show()
                        }
                        lessonViewModel.dismissLessonDialog()
                    }) {
                        Text(text = "Agregar al Home",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = ForestGreen,
                                textDecoration = TextDecoration.Underline,
                                fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_extrabold))
                            ))
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        lessonViewModel.dismissLessonDialog()
                    }) {
                        Text(text = "Cancelar",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = EerieBlack,
                                textDecoration = TextDecoration.Underline,
                                fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_extrabold))
                            ))
                    }
                }
            )
        }
    }
}

@Composable
fun LessonCard(lesson: Lesson, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = EerieBlack),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Usa icono si lo tienes en recursos. Puedes mapear strings a recursos aquí.
            val iconResId = when (lesson.icon.lowercase()) {
                "ic_flag_solid" -> R.drawable.ic_flag_solid
                "ic_database" -> R.drawable.ic_database
                "ic_java" -> R.drawable.ic_java
                "ic_git" -> R.drawable.ic_git
                else -> R.drawable.ic_user_astronaut // default
            }

            if(lesson.icon.lowercase().isEmpty()){
                lesson.icon = "ic_user_astronaut"
            }

            lesson.id = lesson.title.replace(" ", "_")

            Image(
                painter = painterResource(id = iconResId),
                contentDescription = lesson.title,
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 16.dp),
                colorFilter = ColorFilter.tint(DutchWhite)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lesson.title,
                    color = DutchWhite,
                    fontSize = 18.sp,
                    fontFamily = MaterialTheme.typography.titleMedium.fontFamily
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = lesson.goal,
                    color = DutchWhite.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
fun DropdownFilter(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart)
            .padding(end = 8.dp)
    ) {
        Button(
            onClick = { expanded = true },
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = EerieBlack,
                contentColor = DutchWhite
            ),
            modifier = Modifier
                .height(36.dp)
        ) {
            Text(
                text = if (selectedOption == "Todos") "$label: $selectedOption" else selectedOption,
                color = DutchWhite,
                fontSize = 12.sp,
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(EerieBlack.copy(alpha = 0.98f))
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(
                        text = option,
                        color = DutchWhite,
                        fontSize = 12.sp
                    ) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
