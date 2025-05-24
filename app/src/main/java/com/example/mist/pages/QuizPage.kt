package com.example.mist.pages

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mist.AuthState
import com.example.mist.AuthViewModel
import com.example.mist.R
import com.example.mist.components.QuizTopBar
import com.example.mist.ui.theme.*

@Composable
fun QuizPage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val authState = authViewModel.authState.observeAsState()

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableIntStateOf(-1) }
    val scores by remember { mutableStateOf(mutableMapOf<String, Int>()) }
    val selectedOptions = remember { mutableStateMapOf<Int, Int>() }
    val isClicked by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val currentQuestion = questions[currentQuestionIndex]
    val progress = (currentQuestionIndex + 1).toFloat() / questions.size
    var showDialog by remember { mutableStateOf(false) }
    var showConfirmation by remember { mutableStateOf(false) }
    if (showDialog) com.example.mist.popup.QuizPopUp(
        onDismiss = { showDialog = false },
        scores,
        navController,
        authViewModel
    )

    if (showConfirmation) com.example.mist.popup.LeaveQuizPopUp(
        onDismiss = { showConfirmation = false },
        navController,
        authViewModel
    )

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("start")
            else -> Unit
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            QuizTopBar(
                title = "QUIZ",
                progress = progress,
                currentProgress = "${(progress * questions.size).toInt()}/${questions.size}",
                onClick = { authViewModel.signout() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(top = innerPadding.calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(
                modifier = Modifier
                    .height(40.dp)
                    .background(EerieBlack)
            )

            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .height(IntrinsicSize.Max)
                    .background(
                        color = EerieBlack,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(30.dp),
            ) {
                Text(
                    text = "${currentQuestionIndex + 1}. ${currentQuestion.text}",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_bold)),
                        color = DutchWhite,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(10.dp))

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                ) {
                    drawLine(
                        color = DutchWhite,
                        start = Offset(x = 0f, y = 0f),
                        end = Offset(x = size.width, y = 0f),
                        strokeWidth = 5f
                    )
                }

                Spacer(modifier = Modifier.height(25.dp))

                currentQuestion.options.forEachIndexed { index, option ->
                    OutlinedCard(
                        colors = CardDefaults.cardColors(containerColor = if (isClicked) Asparagus else DutchWhite),
                        border = BorderStroke(3.dp, if (isClicked) ForestGreen else Asparagus),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .clickable { selectedOption = index },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            IconToggleButton(
                                checked = selectedOption == index,
                                onCheckedChange = { selectedOption = index }
                            ) {
                                Icon(
                                    painter = painterResource(id = if (selectedOption == index) R.drawable.ic_circle_selected else R.drawable.ic_circle_unselected),
                                    contentDescription = "Radio Button Icon",
                                    tint = Color.Unspecified
                                )
                            }

                            Text(
                                text = option,
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    color = if (isClicked) DutchWhite else EerieBlack,
                                    fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_regular))
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            if (currentQuestionIndex > 0) {
                                currentQuestionIndex--
                                selectedOption = selectedOptions[currentQuestionIndex] ?: -1
                                println(scores)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                        modifier = Modifier
                            .width(90.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_backward_step),
                            contentDescription = "Backward step",
                            tint = DutchWhite,
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                        shape = RoundedCornerShape(10.dp),
                        onClick = {
                            if (selectedOption != -1) {
                                val category = categories[selectedOption]
                                val previousSelection = selectedOptions[currentQuestionIndex]

                                if (previousSelection != selectedOption) {
                                    previousSelection?.let { oldSelection ->
                                        val oldCategory = categories[oldSelection]
                                        scores[oldCategory] = (scores[oldCategory] ?: 0) - 1
                                    }
                                }

                                if (previousSelection == null || previousSelection != selectedOption)
                                    scores[category] = (scores[category] ?: 0) + 1
                                selectedOptions[currentQuestionIndex] = selectedOption

                                if (currentQuestionIndex < questions.lastIndex) {
                                    currentQuestionIndex++
                                    selectedOption = selectedOptions[currentQuestionIndex] ?: -1
                                    println(scores)
                                } else {
                                    println("Puntaje: $scores")
                                    showDialog = true
                                }
                            }
                        }
                    ) {
                        Text(
                            text = "Siguiente",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = DutchWhite,
                                fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_bold))
                            ),
                            modifier = Modifier.padding(end = 10.dp)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_forward_step),
                            contentDescription = "Forward step",
                            tint = DutchWhite,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .padding(5.dp),
                    onClick = { showConfirmation = true },
                ) {
                    Text(
                        text = "No quiero una categoría fija, prefiero ejercicios aleatorios.",
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = DutchWhite,
                            textAlign = TextAlign.Center,
                            textDecoration = TextDecoration.Underline,
                            fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_regular))
                        )
                    )
                }
            }
        }
    }
}

data class Question(
    val text: String,
    val options: List<String>
)

val categories = listOf(
    "Deportes",
    "Artes Visuales",
    "Entretenimiento",
    "Artes Literarias",
    "Miscelaneo"
)

val questions = listOf(
    Question(
        "¿Cómo prefieres pasar tu tiempo libre?",
        listOf(
            "Haciendo ejercicio o practicando deportes.",
            "Dibujando, pintando o creando ilustraciones.",
            "Jugando, viendo películas o resolviendo acertijos.",
            "Escribiendo historias o leyendo libros."
        )
    ),
    Question(
        "¿Qué tipo de contenido te interesa más en redes sociales o internet?",
        listOf(
            "Videos de deportes, rutinas de entrenamiento o competencias.",
            "Ilustraciones digitales, tutoriales de dibujo o arte visual.",
            "Streams de videojuegos, análisis de películas o juegos de mesa.",
            "Citas de libros, análisis literarios o técnicas de escritura."
        )
    ),
    Question(
        "¿Si pudieras dedicarte a una de estas actividades, cuál elegirías?",
        listOf(
            "Ser atleta, entrenador o profesional en deportes.",
            "Ser artista visual, diseñador gráfico o ilustrador.",
            "Ser creador de videojuegos, crítico de cine o desarrollador de entretenimiento.",
            "Ser escritor, editor o periodista."
        )
    ),
    Question(
        "¿Cuál de estas frases te representa mejor?",
        listOf(
            "Me encanta el movimiento, la competencia y la actividad física.",
            "Expreso mis ideas a través del arte visual.",
            "Me encanta sumergirme en mundos de fantasía y entretenimiento.",
            "Las palabras y las historias son mi pasión."
        )
    ),
    Question(
        "¿Cómo prefieres aprender algo nuevo?",
        listOf(
            "Con experiencias prácticas y en movimiento.",
            "A través de la observación y la creatividad.",
            "Jugando, experimentando o interactuando con tecnología.",
            "Leyendo, escribiendo o analizando textos."
        )
    ),
)
