package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.Booking
import com.example.data.model.Inquiry
import com.example.data.model.JobApplication
import com.example.data.model.JobVacancy
import com.example.data.model.PaymentRecord
import com.example.data.model.PostedPhoto

@Database(
    entities = [
        JobVacancy::class,
        JobApplication::class,
        Booking::class,
        PaymentRecord::class,
        Inquiry::class,
        PostedPhoto::class
    ],
    version = 2,
    exportSchema = false
)
abstract class TravelAgencyDatabase : RoomDatabase() {

    abstract fun agencyDao(): TravelAgencyDao

    companion object {
        @Volatile
        private var INSTANCE: TravelAgencyDatabase? = null

        fun getInstance(context: Context): TravelAgencyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TravelAgencyDatabase::class.java,
                    "arbi_pori_travel_agency.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
