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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mist.AuthViewModel
import com.example.mist.AuthState
import com.example.mist.R
import com.example.mist.ui.theme.*

@Composable
fun QuizPage(/*modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel*/){
    /*val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when(authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("start")
            else -> Unit
        }
    }*/

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableIntStateOf(-1) }
    var scores by remember { mutableStateOf(mutableMapOf<String, Int>()) }
    val selectedOptions = remember { mutableStateMapOf<Int, Int>()}

    val currentQuestion = questions[currentQuestionIndex]

    Column(modifier = Modifier
        .fillMaxSize()
        .background(backgroundColor)
        .padding(bottom = 50.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,){

        Spacer(modifier = Modifier.height(36.dp))

        Column(modifier = Modifier
            .clip(shape = RoundedCornerShape(10.dp))
            .background(EerieBlack)
            .width(300.dp)
            .height(580.dp),
            //verticalArrangement = Arrangement.SpaceBetween
            ){

            Text(
                text = "${currentQuestionIndex + 1}. ${currentQuestion.text}",
                //color = DutchWhite,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_bold)),
                    color = DutchWhite,
                ),
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 29.dp, bottom = 0.dp, start=34.dp, end=34.dp),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            Canvas(modifier = Modifier.width(275.dp).
            height(1.dp)) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                drawLine(
                    color = DutchWhite,
                    start = Offset(x = 75f, y = 0f),
                    end = Offset(x = canvasWidth, y = 0f),
                    strokeWidth = 6f
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            currentQuestion.options.forEachIndexed{ index, option ->
                OutlinedCard(
                    colors = CardDefaults.cardColors(
                        containerColor = DutchWhite
                    ),
                    border = BorderStroke(2.dp, Asparagus),
                    modifier = Modifier.
                        padding(horizontal=25.dp,).
                        fillMaxWidth().
                        width(250.dp).
                        height(50.dp).
                        align(Alignment.CenterHorizontally).
                        clickable { selectedOption = index },

                    shape = RoundedCornerShape(10.dp),

                ){
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(0.dp)
                    ){
                        RadioButton(
                            selected = selectedOption == index,
                            onClick = { selectedOption = index
                                      println(selectedOption)},
                        )
                        Text(text = option, modifier = Modifier.padding(0.dp),
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.relay_jetbrains_mono_regular)),
                                color = EerieBlack),
                        )
                    }

                }

                Spacer(modifier = Modifier.height(30.dp))
            }

            Row{
                Button(
                    onClick = {
                        if(currentQuestionIndex > 0){
                            selectedOptions[currentQuestionIndex-1]?.let {
                                val previousCategory = categories[it]
                                scores[previousCategory] = (scores[previousCategory] ?: 1) - 1
                            }
                            currentQuestionIndex--
                            selectedOption = selectedOptions[currentQuestionIndex] ?: -1

                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                    modifier = Modifier
                        .padding(start = 25.dp)
                        .height(50.dp)
                        .width(80.dp),
                    shape = RoundedCornerShape(10.dp)
                ){
                    Icon(painter = painterResource(id=R.drawable.backward_step),
                        contentDescription = null)
                }

                Button(
                    onClick = {
                        if(selectedOption != -1){
                            val category = categories[selectedOption]
                            val wasAlreadyAnswered = selectedOptions.containsKey(currentQuestionIndex)


                            scores[category] = scores.getOrDefault(category,0) + 1


                            selectedOptions[currentQuestionIndex] = selectedOption

                            if(currentQuestionIndex < questions.lastIndex){
                                currentQuestionIndex++
                                selectedOption = selectedOptions[currentQuestionIndex] ?: -1
                            }
                            else{
                                println("Puntaje: $scores")
                                //overlay
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                    modifier = Modifier
                        .padding(start = 10.dp, end = 25.dp)
                        .height(50.dp)
                        .width(175.dp),
                    shape = RoundedCornerShape(10.dp)
                ){
                    Text(text="Siguiente",
                        modifier = Modifier
                            .padding(end=10.dp),
                        style = TextStyle(
                            fontFamily= FontFamily(Font(R.font.relay_jetbrains_mono_bold))
                        )
                    )
                    Icon(painter = painterResource(id=R.drawable.forward_step),
                        contentDescription = null)
                }
            }

        }

    }

    
}

data class Question(val text: String,
    val options: List<String>)

val categories = listOf(
    "Deportes",
    "ArtesVisuales",
    "Entretenimiento",
    "ArtesLiterarias",
    "Miscelaneo")

val questions = listOf(
    Question(
        "¿Cómo prefieres pasar tu tiempo libre?",
        listOf(
            "Haciendo ejercicio o practicando deportes.",
            "Dibujando, pintando o creando ilustraciones.",
            "Jugando, viendo películas o resolviendo acertijos.",
            "Escribiendo historias o leyendo libros.",
            "No quiero una categoría fija, prefiero ejercicios aleatorios."
        )
    ),
    Question(
        "¿Qué tipo de contenido te interesa más en redes sociales o internet?",
        listOf(
            "Videos de deportes, rutinas de entrenamiento o competencias.",
            "Ilustraciones digitales, tutoriales de dibujo o arte visual.",
            "Streams de videojuegos, análisis de películas o juegos de mesa.",
            "Citas de libros, análisis literarios o técnicas de escritura.",
            "No quiero una categoría fija, prefiero ejercicios aleatorios."
        )
    ),
    Question(
        "¿Si pudieras dedicarte a una de estas actividades, cuál elegirías?",
        listOf(
            "Ser atleta, entrenador o profesional en deportes.",
            "Ser artista visual, diseñador gráfico o ilustrador.",
            "Ser creador de videojuegos, crítico de cine o desarrollador de entretenimiento.",
            "Ser escritor, editor o periodista.",
            "No quiero una categoría fija, prefiero ejercicios aleatorios."
        )
    ),
    Question(
        "¿Cuál de estas frases te representa mejor?",
        listOf(
            "Me encanta el movimiento, la competencia y la actividad física.",
            "Expreso mis ideas a través del arte visual.",
            "Me encanta sumergirme en mundos de fantasía y entretenimiento.",
            "Las palabras y las historias son mi pasión.",
            "No quiero una categoría fija, prefiero ejercicios aleatorios."
        )
    ),
    Question(
        "¿Cómo prefieres aprender algo nuevo?",
        listOf(
            "Con experiencias prácticas y en movimiento.",
            "A través de la observación y la creatividad.",
            "Jugando, experimentando o interactuando con tecnología.",
            "Leyendo, escribiendo o analizando textos.",
            "No quiero una categoría fija, prefiero ejercicios aleatorios."
        )
    ),

)

@Preview
@Composable
fun QuizPreview(){
    QuizPage()
}