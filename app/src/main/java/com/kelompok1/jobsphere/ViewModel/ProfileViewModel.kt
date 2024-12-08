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

    private val _profileState = MutableStateFlow(Profile())
    val profileState: StateFlow<Profile> = _profileState

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _useremail = MutableStateFlow("")
    val useremail: StateFlow<String> = _useremail

    fun fetchUserProfile() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    _username.value = document.getString("username") ?: ""
                    _useremail.value = document.getString("email") ?: ""
                }
            }
            .addOnFailureListener { exception ->
                println("Error fetching user profile: $exception")
            }
    }

    fun fetchProfile() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("profile").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    _profileState.value = Profile(
                        username = document.getString("username") ?: "",
                        email = document.getString("email") ?: "",
                        address = document.getString("address") ?: "",
                        personalSummary = document.getString("personalSummary") ?: "",
                        careerHistory = document.get("careerHistory") as? List<String> ?: listOf(),
                        education = document.get("education") as? List<String> ?: listOf(),
                        skill = document.get("skill") as? List<String> ?: listOf(),
                        language = document.get("language") as? List<String> ?: listOf()
                    )
                }
            }
            .addOnFailureListener { exception ->
                println("Error fetching profile: $exception")
            }
    }

    fun saveProfile(profile: Profile) {
        val userId = auth.currentUser?.uid ?: return
        val profileData = mapOf(
            "username" to profile.username,
            "email" to profile.email,
            "address" to profile.address,
            "personalSummary" to profile.personalSummary,
            "careerHistory" to profile.careerHistory,
            "education" to profile.education,
            "skill" to profile.skill,
            "language" to profile.language
        )
        db.collection("profile").document(userId)
            .set(profileData)
            .addOnSuccessListener {
                _profileState.value = profile
            }
            .addOnFailureListener { exception ->
                println("Error saving profile: $exception")
            }
    }
}

data class Profile(
    val username: String = "",
    val email: String = "",
    val address: String = "",
    val personalSummary: String = "",
    val careerHistory: List<String> = listOf(), // Ganti menjadi List<String>
    val education: List<String> = listOf(),     // Ganti menjadi List<String>
    val skill: List<String> = listOf(),         // Ganti menjadi List<String>
    val language: List<String> = listOf()       // Ganti menjadi List<String>
)

