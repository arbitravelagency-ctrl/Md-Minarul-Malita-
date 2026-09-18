package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "job_applications")
data class JobApplication(
    @PrimaryKey
    val trackingNumber: String,
    val applicantName: String,
    val passportNumber: String,
    val phone: String,
    val whatsappNumber: String,
    val email: String,
    val address: String,
    val jobTitle: String,
    val country: String,
    val experienceYears: String,
    val education: String,
    val status: String = "Application Submitted",
    val stageIndex: Int = 1, // 1 to 6
    val remarks: String = "Your application has been received and is being reviewed by Arbi Pori Travel Agency recruitment desk.",
    val appliedDate: String,
    val targetSubmissionDate: String = "Within 45 days",
    val assignedOfficer: String = "European Visa Desk - Arbi Pori Travel Agency",
    val photoUri: String? = null
)
