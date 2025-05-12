package com.example.mist

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mist.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val _userData = MutableStateFlow<User?>(null)
    val userData: StateFlow<User?> = _userData.asStateFlow()

    init {
        checkAuthStatus()
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun checkAuthStatus(){
        val uid = auth.currentUser?.uid
        if(uid == null){
            _authState.value = AuthState.Unauthenticated
        }else{
            _authState.value = AuthState.Authenticated(uid)
        }
    }

    fun login(email : String,password : String){

        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("La contraseña o el correo no pueden estar vacíos")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{task->
            if (task.isSuccessful){
                val uid = auth.currentUser?.uid
                if(uid != null) {
                    _authState.value = AuthState.Authenticated(uid)
                } else {
                    _authState.value = AuthState.Error("No se pudo obtener el UID del usuario")
                }
            }else{
                _authState.value = AuthState.Error(task.exception?.message?:"Algo salió mal")
            }
        }
    }

    fun signup(email : String,password : String){

        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    if(uid != null) {
                        createUser(email.substringBefore("@"))
                        _authState.value = AuthState.Authenticated(uid)
                    } else {
                        _authState.value = AuthState.Error("No se pudo obtener el UID del usuario")
                    }
                }else{
                    _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong")
                }
            }
    }

    private fun createUser(displayName: String) {
        val userId = auth.currentUser?.uid ?: return

        val user = User(
            userId = userId,
            nickname = displayName,
            email = auth.currentUser?.email.toString(),
            hobby = "Todos",
            bookmarks = emptyList(),
            completedLessons = emptyList(),
            points = 0,
            level = "0",
            profilePicture = ""
        ).toMap()

        FirebaseFirestore.getInstance().collection("users")
            .document(userId)
            .set(user)
            .addOnSuccessListener {
                Log.d("AuthViewModel", "User $userId created successfully")
            } .addOnFailureListener {
                Log.d("AuthViewModel", "Error creating user: ${it.message}")
            }
    }

    fun saveUserHobby(hobby: String, onSuccess: (Boolean) -> Unit){
        auth.currentUser?.uid?.let { uid ->
            viewModelScope.launch {
                try {
                    val userData = mapOf(
                        "hobby" to hobby
                    )


                    Firebase.firestore.collection("users").document(uid)
                        .update(userData)
                        .addOnSuccessListener {
                            Log.d("AuthViewModel", "Hobby actualizado correctamente")
                            onSuccess(true)
                        }.addOnFailureListener {
                            Log.d("AuthViewModel", "Error al actualizar el hobby")
                            onSuccess(false)
                        }
                } catch(e: Exception){
                    Log.e("AuthViewModel", "Excepción al guardar el hobby", e)
                    onSuccess(false)
                }
            }
        }
    }

    fun editUser(name:String, profilePicture:String, onSuccess: (Boolean) -> Unit){
        auth.currentUser?.uid?.let { uid ->
            viewModelScope.launch {
                try {
                    val userData = mapOf(
                        "nickname" to name,
                        "profilePicture" to profilePicture
                    )


                    Firebase.firestore.collection("users").document(uid)
                        .update(userData)
                        .addOnSuccessListener {
                            Log.d("AuthViewModel", "Perfil actualizado correctamente")
                            onSuccess(true)
                            loadUserData()
                        }.addOnFailureListener {
                            Log.d("AuthViewModel", "Error al actualizar el perfil")
                            onSuccess(false)
                        }
                } catch(e: Exception){
                    Log.e("AuthViewModel", "Excepción al editar el perfil", e)
                    onSuccess(false)
                }
            }
        }
    }

    fun loadUserData(){
        auth.currentUser?.uid?.let { uid ->
            viewModelScope.launch {
                try {
                    Firebase.firestore.collection("users").document(uid)
                        .get()
                        .addOnSuccessListener { document ->
                            if(document.exists()){
                                val user = document.toObject(User::class.java)?.copy(userId = uid)
                                _userData.value = user
                            }
                        }.addOnFailureListener{ e ->
                            Log.e("AuthViewModel", "Error al cargar datos del usuario", e)
                        }
                }catch (e: Exception){
                    Log.e("AuthViewModel", "Excepcion al cargar los datos", e)
                }                }
            }
    }

    fun signout(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

}


sealed class AuthState{
    data class Authenticated(val uid: String) : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message : String) : AuthState()
}

sealed class UserState{
    object Loading: UserState()
    data class Success(val user: User) : UserState()
    data class Error(val message: String) : UserState()
}