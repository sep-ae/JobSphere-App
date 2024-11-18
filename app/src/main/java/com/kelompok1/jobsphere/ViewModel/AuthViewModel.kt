package com.kelompok1.jobsphere.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    // Memeriksa status autentikasi pengguna
    fun checkAuthStatus() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            fetchUserData(currentUser.uid)
        }
    }

    // Mengambil data pengguna setelah login
    private fun fetchUserData(userId: String) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                val username = document.getString("username") ?: "User"
                val role = document.getString("role") ?: "user"
                _authState.value = AuthState.Authenticated(username, role)
            }
            .addOnFailureListener {
                _authState.value = AuthState.Error("Failed to retrieve user data.")
            }
    }

    // Validasi password minimal 8 karakter
    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 8
    }

    // Memeriksa apakah email sudah digunakan
    private fun checkIfEmailExists(email: String, onComplete: (Boolean) -> Unit) {
        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val isEmailUsed = task.result?.signInMethods?.isNotEmpty() == true
                    onComplete(isEmailUsed)
                } else {
                    onComplete(false)
                }
            }
    }

    // Fungsi untuk login pengguna
    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or Password can't be empty")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.currentUser?.let { user ->
                        fetchUserData(user.uid)
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "An error occurred. Please try again.")
                }
            }
    }

    // Fungsi untuk signup pengguna
    fun signup(email: String, password: String, role: String, username: String) {
        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            _authState.value = AuthState.Error("Email, Password, and Username can't be empty")
            return
        }

        if (!isPasswordValid(password)) {
            _authState.value = AuthState.Error("Password must be at least 8 characters long")
            return
        }

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

                            db.collection("users").document(userId)
                                .set(userMap)
                                .addOnCompleteListener { firestoreTask ->
                                    if (firestoreTask.isSuccessful) {
                                        _authState.value = AuthState.Authenticated(username, role)
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

    // Fungsi untuk logout pengguna
    fun signout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }
}

// Status autentikasi pengguna
sealed class AuthState {
    data class Authenticated(val username: String, val role: String) : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}
