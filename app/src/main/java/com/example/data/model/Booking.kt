package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey
    val bookingId: String, // e.g. BK-APTA-1082
    val clientName: String,
    val phone: String,
    val email: String,
    val bookingDate: String,
    val timeSlot: String,
    val consultationType: String, // "Ranaghat Office Visit", "WhatsApp Video Call", "Direct Phone Call"
    val countryInterest: String,
    val purpose: String, // "Work Permit Consultation", "Document Submission", "Visa Interview Prep"
    val status: String = "Confirmed",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
