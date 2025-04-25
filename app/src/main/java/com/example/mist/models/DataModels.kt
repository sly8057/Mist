package com.example.mist.models

data class Lesson(
    var id: String = "",
    var icon: String = "",
    val hobby: String = "",
    val level: String = "",
    val hints: List<String> = emptyList(),
    val brief: List<String> = emptyList(),
    val goal: String = "",
    val title: String = "",
    val test: String = ""
)

data class UserLesson(
    val lessonId: String = "",
    val icon: String = "",
    val hobby: String = "",
    val level: String = "",
    val hints: List<String> = emptyList(),
    val brief: List<String> = emptyList(),
    val goal: String = "",
    val title: String = "",
    val state: String = "",
    val test: String = "",
    val storagePath: String = ""
)