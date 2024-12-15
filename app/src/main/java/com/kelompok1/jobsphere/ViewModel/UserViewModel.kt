package com.kelompok1.jobsphere.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kelompok1.jobsphere.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class UserViewModel : ViewModel() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _isGuest = MutableStateFlow(false) // Status apakah pengguna adalah guest
    val isGuest: StateFlow<Boolean> = _isGuest

    private val _currentUser = MutableStateFlow<User?>(null) // StateFlow untuk data user
    val currentUser: StateFlow<User?> = _currentUser

    init {
        checkUserStatus()
    }


    private fun checkUserStatus() {
        val userId = firebaseAuth.currentUser?.uid
        if (userId == null) {
            _isGuest.value = true
            _currentUser.value = null
        } else {
            _isGuest.value = false
            fetchUserInfo(userId)
        }
    }

    // Fetch informasi user dari Firestore
    private fun fetchUserInfo(userId: String) {
        liveData {
            try {
                val userDoc = firestore.collection("users").document(userId).get().await()
                val user = userDoc.toObject(User::class.java)
                _currentUser.value = user
                emit(user)
            } catch (e: Exception) {
                _currentUser.value = null
                emit(null)
            }
        }
    }

    // Mendapatkan ID user saat ini
    fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }

    // Logout user dan reset state
    fun logoutUser(vararg resetStateCallbacks: () -> Unit) {
        firebaseAuth.signOut()
        _isGuest.value = true
        _currentUser.value = null
        resetStateCallbacks.forEach { resetState -> resetState() }
    }
}
