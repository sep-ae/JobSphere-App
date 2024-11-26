package com.kelompok1.jobsphere.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CompanyProfileViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _companyProfileState = MutableStateFlow(
        CompanyProfile(
            name = "", email = "", address = "", phone = "",
            description = "", industrialSector = "",
            visionAndMission = "", benefitsAndFacilities = "",
            socialMedia = ""
        )
    )
    val companyProfileState: StateFlow<CompanyProfile> = _companyProfileState

    fun fetchCompanyProfile(companyId: String) {
        viewModelScope.launch {
            db.collection("companyProfiles")
                .document(companyId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val name = document.getString("name") ?: ""
                        val email = document.getString("email") ?: ""
                        val address = document.getString("address") ?: ""
                        val phone = document.getString("phone") ?: ""
                        val description = document.getString("description") ?: ""
                        val industrialSector = document.getString("industrialSector") ?: ""
                        val visionAndMission = document.getString("visionAndMission") ?: ""
                        val benefitsAndFacilities = document.getString("benefitsAndFacilities") ?: ""
                        val socialMedia = document.getString("socialMedia") ?: ""
                        _companyProfileState.value = CompanyProfile(
                            name, email, address, phone, description,
                            industrialSector, visionAndMission,
                            benefitsAndFacilities, socialMedia
                        )
                    }
                }
        }
    }

    fun saveCompanyProfile(companyId: String, profile: CompanyProfile) {
        viewModelScope.launch {
            val profileData = mapOf(
                "name" to profile.name,
                "email" to profile.email,
                "address" to profile.address,
                "phone" to profile.phone,
                "description" to profile.description,
                "industrialSector" to profile.industrialSector,
                "visionAndMission" to profile.visionAndMission,
                "benefitsAndFacilities" to profile.benefitsAndFacilities,
                "socialMedia" to profile.socialMedia
            )
            db.collection("companyProfiles").document(companyId).set(profileData)
        }
    }
}

data class CompanyProfile(
    val name: String,
    val email: String,
    val address: String,
    val phone: String,
    val description: String,
    val industrialSector: String,
    val visionAndMission: String,
    val benefitsAndFacilities: String,
    val socialMedia: String
)
