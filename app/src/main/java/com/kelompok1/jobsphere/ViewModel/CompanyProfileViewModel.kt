package com.kelompok1.jobsphere.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CompanyProfileViewModel : ViewModel() {

    // Inisialisasi FirebaseAuth dan FirebaseFirestore
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // StateFlow untuk menyimpan state profil perusahaan
    private val _companyProfileState = MutableStateFlow(CompanyProfile())
    val companyProfileState: StateFlow<CompanyProfile> = _companyProfileState

    // StateFlow untuk username dan email
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

    fun fetchCompanyProfile() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("company").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    _companyProfileState.value = CompanyProfile(
                        username = document.getString("username") ?: "",
                        email = document.getString("email") ?: "",
                        telephone = document.getString("telephone") ?: "",
                        address = document.getString("address") ?: "",
                        description = document.getString("description") ?: "",
                        industrialSector = document.getString("industrialSector") ?: "",
                        visionAndMission = document.get("visionAndMission") as? List<String> ?: listOf(),
                        benefitsAndFacilities = document.get("benefitsAndFacilities") as? List<String> ?: listOf(),
                        socialMedia = document.get("socialMedia") as? List<String> ?: listOf()
                    )
                }
            }
            .addOnFailureListener { exception ->
                println("Error fetching profile: $exception")
            }
    }

    fun saveProfile(profile: CompanyProfile) {
        val userId = auth.currentUser?.uid ?: return
        val profileData = mapOf(
            "username" to profile.username,
            "email" to profile.email,
            "telephone" to profile.telephone,
            "address" to profile.address,
            "description" to profile.description,
            "industrialSector" to profile.industrialSector,
            "visionAndMission" to profile.visionAndMission,
            "benefitsAndFacilities" to profile.benefitsAndFacilities,
            "socialMedia" to profile.socialMedia
        )
        db.collection("company").document(userId)
            .set(profileData)
            .addOnSuccessListener {
                _companyProfileState.value = profile
            }
            .addOnFailureListener { exception ->
                println("Error saving profile: $exception")
            }
    }
}

data class CompanyProfile(
    val username: String = "",
    val email: String = "",
    val telephone: String = "",
    val address: String = "",
    val description: String = "",
    val industrialSector: String = "", // Ganti menjadi List<String>
    val visionAndMission: List<String> = listOf(),     // Ganti menjadi List<String>
    val benefitsAndFacilities: List<String> = listOf(),         // Ganti menjadi List<String>
    val socialMedia: List<String> = listOf()       // Ganti menjadi List<String>
)


