package com.kelompok1.jobsphere.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // State untuk menyimpan data profil
    private val _profileState = MutableStateFlow(Profile("", "", "", ""))
    val profileState: StateFlow<Profile> = _profileState

    // Fungsi untuk mengambil data profil dari Firestore
    fun fetchProfile() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("profile").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val username = document.getString("username") ?: ""
                    val email = document.getString("email") ?: ""
                    val address = document.getString("address") ?: ""
                    val personalSummary = document.getString("personalSummary") ?: ""
                    _profileState.value = Profile(username, email, address, personalSummary)
                }
            }
            .addOnFailureListener { exception ->
                println("Error fetching profile: $exception")
            }
    }

    // Fungsi untuk menyimpan data profil ke Firestore
    fun saveProfile(profile: Profile) {
        val userId = auth.currentUser?.uid ?: return
        val profileData = mapOf(
            "username" to profile.username,
            "email" to profile.email,
            "address" to profile.address,
            "personalSummary" to profile.personalSummary
        )
        db.collection("profile").document(userId)
            .set(profileData)
            .addOnSuccessListener {
                println("Profile successfully saved!")
                _profileState.value = profile // Update state setelah menyimpan
            }
            .addOnFailureListener { exception ->
                println("Error saving profile: $exception")
            }
    }
}

// Data class untuk menyimpan informasi profil
data class Profile(
    val username: String,
    val email: String,
    val address: String,
    val personalSummary: String
)
