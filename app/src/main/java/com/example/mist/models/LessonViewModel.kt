package com.example.mist.models

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LessonViewModel(
    private val repository: LessonRepository,
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _userLessons = MutableStateFlow<List<UserLesson>>(emptyList())
    val allUserLessons: StateFlow<List<UserLesson>> = _userLessons

    val allLessons = repository.getAllLessons()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userLessons = auth.currentUser?.uid?.let { uid ->
        repository.getUserLessons(uid)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    } ?: MutableStateFlow(emptyList())

    var showLessonDialog by mutableStateOf(false)
        private set

    var selectedLesson: Lesson? by mutableStateOf(null)
        private set

    var selectedUserLesson: UserLesson? by mutableStateOf(null)
        private set

    fun selectLesson(lesson: Lesson) {
        selectedLesson = lesson
        showLessonDialog = true
    }

    fun dismissLessonDialog() {
        showLessonDialog = false
        selectedLesson = null
    }

    fun selectUserLesson(lesson: UserLesson) {
        Log.d("LessonViewModel", "Lesson selected: $lesson")
        selectedUserLesson = lesson
        showLessonDialog = true
    }

    fun dismissUserLessonDialog() {
        showLessonDialog = false
        selectedUserLesson = null
    }

    fun addLessonToUser(context: Context, lesson: Lesson) {
        auth.currentUser?.uid?.let { uid ->
            viewModelScope.launch {
                val userLesson = repository.addUserLesson(context, uid, lesson)
                if(userLesson == null) {
                    Log.e("LessonViewModel", "No se pudo agregar la lección al usuario.")
                    print("Error al añadir la lección al usuario")
                } else {
                    Log.d("LessonViewModel", "Lección añadida correctamente: ${userLesson.title}")
                    // (opcional) Actualizar _userLessons si es necesario
                }
            }
        } ?: Log.e("LessonViewModel", "Usuario no autenticado.")
    }

    fun loadUserLesson(uid: String) {
        viewModelScope.launch {
            repository.getUserLessons(uid).collect { lessons ->
                _userLessons.value = lessons
            }
        }
    }
}