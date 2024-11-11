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

    // Fungsi validasi password minimal 8 karakter
    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 8
    }

    // Fungsi untuk cek apakah email sudah terdaftar
    private fun checkIfEmailExists(email: String, onComplete: (Boolean) -> Unit) {
        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val isEmailUsed = task.result?.signInMethods?.isNotEmpty() == true
                    onComplete(isEmailUsed)
                } else {
                    // Jika terjadi kesalahan saat pengecekan
                    onComplete(false)
                }
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

    // fungsi login
    fun signup(email: String, password: String, role: String, username: String) {
        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            _authState.value = AuthState.Error("Email, Password, and Username can't be empty")
            return
        }

        // Validasi password
        if (!isPasswordValid(password)) {
            _authState.value = AuthState.Error("Password must be at least 8 characters long")
            return
        }

        // Cek apakah email sudah terdaftar
        checkIfEmailExists(email) { isEmailUsed ->
            if (isEmailUsed) {
                _authState.value = AuthState.Error("Email is already in use")
            } else {
                _authState.value = AuthState.Loading
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                            val userMap = hashMapOf(
                                "role" to role,
                                "email" to email,
                                "username" to username
                            )

                            // menambhkan data ke tabel users
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
        }
    }

    // Fungsi Logout
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
