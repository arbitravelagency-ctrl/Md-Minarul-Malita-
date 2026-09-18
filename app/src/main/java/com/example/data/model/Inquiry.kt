package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inquiries")
data class Inquiry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val phone: String,
    val email: String,
    val countryInterest: String,
    val preferredTrade: String,
    val message: String,
    val submittedAt: Long = System.currentTimeMillis()
)
