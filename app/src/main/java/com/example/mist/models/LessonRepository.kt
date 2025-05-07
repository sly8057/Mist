package com.example.mist.models

import android.content.Context
import android.util.Log
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
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
            val fileContent = "# Código de python de ${lesson.title}\\n"

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
                //storagePath = file.absolutePath,
                pythonCode = fileContent
            )

            // guardar en Firestore
            Log.d("LessonRepository", "Guardando usuario en Firestore...")
            firestore.collection("users").document(uid)
                .set(mapOf("uid" to uid), SetOptions.merge())

            Log.d("LessonRepository", "antes del set ${userLesson}")
            Log.d("LessonRepository", "Guardando userLesson en subcolección ejercicios...")
            firestore.collection("users").document(uid).collection("exercises")
                .document(lesson.id).set(userLesson).await()

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

    fun saveUserLessonCode(uid: String, lessonId: String, newCode: String) {
        firestore.collection("users")
            .document(uid)
            .collection("exercises")
            .document(lessonId)
            .update("pythonCode", newCode)
    }

    fun runLessonTest(context: Context, userCode: String, testFileName: String): String {
        if(!Python.isStarted()) {
            Python.start(AndroidPlatform(context))
        }
        return Python.getInstance().getModule("test_runner").callAttr("run_test",
            writeUserCodeToFile(context, userCode),
            testFileName.trim()
        ).toString()
    }

    private fun writeUserCodeToFile(context: Context, code: String): String {
        val file = File(context.filesDir, "user_code.py")
        file.writeText(code)
        return file.absolutePath
    }
}