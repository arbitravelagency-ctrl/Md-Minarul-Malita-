package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Booking
import com.example.data.model.Inquiry
import com.example.data.model.JobApplication
import com.example.data.model.JobVacancy
import com.example.data.model.PaymentRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface TravelAgencyDao {

    // Job Vacancies
    @Query("SELECT * FROM job_vacancies ORDER BY isHotVacancy DESC, id ASC")
    fun getAllVacancies(): Flow<List<JobVacancy>>

    @Query("SELECT * FROM job_vacancies WHERE country = :country")
    fun getVacanciesByCountry(country: String): Flow<List<JobVacancy>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancies(vacancies: List<JobVacancy>)

    @Query("SELECT COUNT(*) FROM job_vacancies")
    suspend fun getVacanciesCount(): Int

    // Applications & Tracking
    @Query("SELECT * FROM job_applications ORDER BY appliedDate DESC")
    fun getAllApplications(): Flow<List<JobApplication>>

    @Query("SELECT * FROM job_applications WHERE trackingNumber = :trackingNumber LIMIT 1")
    suspend fun getApplicationByTrackingNumber(trackingNumber: String): JobApplication?

    @Query("SELECT * FROM job_applications WHERE trackingNumber = :trackingNumber")
    fun observeApplication(trackingNumber: String): Flow<JobApplication?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(application: JobApplication)

    @Update
    suspend fun updateApplication(application: JobApplication)

    @Query("UPDATE job_applications SET photoUri = :photoUri WHERE trackingNumber = :trackingNumber")
    suspend fun updateApplicationPhoto(trackingNumber: String, photoUri: String)

    // Bookings
    @Query("SELECT * FROM bookings ORDER BY createdAt DESC")
    fun getAllBookings(): Flow<List<Booking>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: Booking)

    // Payments
    @Query("SELECT * FROM payment_records ORDER BY timestamp DESC")
    fun getAllPayments(): Flow<List<PaymentRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentRecord)

    // Inquiries
    @Query("SELECT * FROM inquiries ORDER BY submittedAt DESC")
    fun getAllInquiries(): Flow<List<Inquiry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInquiry(inquiry: Inquiry)

    // Posted Photos
    @Query("SELECT * FROM posted_photos ORDER BY timestamp DESC")
    fun getAllPostedPhotos(): Flow<List<com.example.data.model.PostedPhoto>>

    @Query("SELECT * FROM posted_photos WHERE trackingNumber = :trackingNumber ORDER BY timestamp DESC")
    fun getPhotosForTracking(trackingNumber: String): Flow<List<com.example.data.model.PostedPhoto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPostedPhoto(photo: com.example.data.model.PostedPhoto)
}
