package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "job_vacancies")
data class JobVacancy(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val country: String,
    val countryFlag: String,
    val city: String,
    val salaryEur: String,
    val salaryInr: String,
    val category: String,
    val vacanciesCount: Int,
    val contractDuration: String,
    val processingTime: String,
    val requirements: String,
    val benefits: String,
    val description: String,
    val isHotVacancy: Boolean = false
)
