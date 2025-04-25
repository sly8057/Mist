package com.example.mist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

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
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{task->
                if (task.isSuccessful){
                    val uid = auth.currentUser?.uid
                    if(uid != null) {
                        _authState.value = AuthState.Authenticated(uid)
                    } else {
                        _authState.value = AuthState.Error("No se pudo obtener el UID del usuario")
                    }
                }else{
                    _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong")
                }
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