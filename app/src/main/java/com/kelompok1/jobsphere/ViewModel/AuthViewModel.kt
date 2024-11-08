package com.kelompok1.jobsphere.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        _authState.value = if (auth.currentUser == null) {
            AuthState.Unauthenticated
        } else {
            AuthState.Authenticated
        }
    }

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or Password can't be empty")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "An error occurred. Please try again.")
                }
            }
    }

    // Update signup function to accept username parameter
    fun signup(email: String, password: String, role: String, username: String) {
        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            _authState.value = AuthState.Error("Email, Password, and Username can't be empty")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // After successful signup, store the role, username, and email in Firestore
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val userMap = hashMapOf(
                        "role" to role,
                        "email" to email,
                        "username" to username  // Save username
                    )

                    // Save user data (including username and role) to Firestore
                    FirebaseFirestore.getInstance().collection("users")
                        .document(userId)
                        .set(userMap)
                        .addOnCompleteListener { firestoreTask ->
                            if (firestoreTask.isSuccessful) {
                                _authState.value = AuthState.Authenticated
                            } else {
                                _authState.value = AuthState.Error("Failed to store user data.")
                            }
                        }

                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "An error occurred. Please try again.")
                }
            }
    }

    fun signout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }
}

sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}
