package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_records")
data class PaymentRecord(
    @PrimaryKey
    val receiptId: String, // e.g. RCP-8945-21
    val candidateName: String,
    val trackingOrBookingId: String,
    val amount: String,
    val paymentMethod: String, // "PhonePe", "Google Pay", "Paytm", "Bank Transfer (NEFT/IMPS)"
    val transactionUtr: String,
    val serviceType: String, // "Registration & File Opening", "Work Permit Processing", "Embassy Appointment", "Other"
    val status: String = "Submitted for Verification",
    val paymentDate: String,
    val timestamp: Long = System.currentTimeMillis(),
    val receiptPhotoUri: String? = null
)
