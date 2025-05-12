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
    val pythonCode: String = ""
)

data class User(
    val userId: String = "",
    val nickname: String = "",
    val email: String = "",
    val hobby: String = "Todos",
    val bookmarks: List<UserLesson> = emptyList(),
    val completedLessons: List<Lesson> = emptyList(),
    val points: Int = 0,
    val level: String = "",
    val profilePicture: String = ""
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "user_uid" to this.userId,
            "nickname" to this.nickname,
            "email" to this.email,
            "hobby" to this.hobby,
            "bookmarks" to this.bookmarks,
            "completedLessons" to this.completedLessons,
            "points" to this.points,
            "level" to this.level,
            "profilePicture" to this.profilePicture
        )
    }
}