package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posted_photos")
data class PostedPhoto(
    @PrimaryKey
    val id: String, // e.g. PHO-2026-104
    val candidateName: String,
    val trackingNumber: String = "",
    val photoType: String, // "Candidate White Background Photo", "Passport Front Page", "Passport Back Page", "Police Clearance Certificate (PCC)", "Experience Certificate", "Payment Receipt / Proof", "Other"
    val photoUri: String,
    val notes: String = "",
    val datePosted: String,
    val timestamp: Long = System.currentTimeMillis()
)
