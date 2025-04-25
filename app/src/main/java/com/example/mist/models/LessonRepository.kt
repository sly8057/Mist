package com.example.mist.models

import android.content.Context
import android.util.Log
import com.example.mist.models.Lesson
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayInputStream
import java.io.File

class LessonRepository {
    private val firestore = Firebase.firestore
    //private val storage = Firebase.storage

    fun getAllLessons(): Flow<List<Lesson>> = callbackFlow {
        val listener = firestore.collection("exercises")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    e.printStackTrace()
                    trySend(emptyList())
                    print("Error al obtener las lecciones")
                    return@addSnapshotListener
                }

                val lessons = try {
                    snapshot?.documents?.mapNotNull { it.toObject(Lesson::class.java) }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    emptyList()
                }

                trySend(lessons ?: emptyList())
            }
        awaitClose { listener.remove() }
    }

    suspend fun addUserLesson(context: Context, uid: String, lesson: Lesson): UserLesson? {
        return try {
            Log.d("LessonRepository", "Creando directorio local...")
            //val fileName = "${lesson.title}.py"
            val fileName = "${lesson.id}.py"
            val fileContent = "# Código de python de ${lesson.title}\\n"

            // guardar archivo localmente
            Log.d("LessonRepository", "Guardando archivo localmente...")
            val fileDir = File(context.filesDir, "exercises/$uid")
            if(!fileDir.exists()) {
                val created = fileDir.mkdirs()
                Log.d("LessonRepository", "Directorio local creado =  ${created}")
            }

            val file = File(fileDir, fileName)
            file.writeText(fileContent)
            Log.d("LessonRepository", "Archivo guardado en: ${file.absolutePath}")

            val userLesson = UserLesson(
                lessonId = lesson.id,
                icon = lesson.icon,
                hobby = lesson.hobby,
                level = lesson.level,
                hints = lesson.hints,
                brief = lesson.brief,
                goal = lesson.goal,
                title = lesson.title,
                state = "pendiente",
                test = lesson.test,
                storagePath = file.absolutePath
            )

            // guardar en Firestore
            Log.d("LessonRepository", "Guardando usuario en Firestore...")
            firestore.collection("users").document(uid)
                .set(mapOf("uid" to uid), SetOptions.merge())

            Log.d("LessonRepository", "antes del set ${userLesson}")
            Log.d("LessonRepository", "Guardando userLesson en subcolección ejercicios...")
            firestore.collection("users").document(uid).collection("exercises")
                //.document(lesson.title).set(userLesson).await()
                .document(lesson.id).set(userLesson).await()

            Log.d("LessonRepository", "Archivo guardado localmente en: ${file.absolutePath}")
            Log.d("LessonRepository", "Guardando userLesson en Firestore...")

            userLesson
        } catch (e: Exception) {
            Log.e("LessonRepository", "Error al guardar lección", e)
            e.printStackTrace()
            null
        }
    }

    fun getUserLessons(uid: String): Flow<List<UserLesson>> = callbackFlow {
        val listener = firestore.collection("users").document(uid).collection("exercises")
            .addSnapshotListener { snapshot, _ ->
                val userLessons = snapshot?.documents?.mapNotNull { it.toObject(UserLesson::class.java) } ?: emptyList()
                trySend(userLessons)
            }
        awaitClose { listener.remove() }
    }
}
