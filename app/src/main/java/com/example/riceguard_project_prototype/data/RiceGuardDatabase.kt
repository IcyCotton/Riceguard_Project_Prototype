package com.example.riceguard_project_prototype.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DiagnosisResult::class], version = 1, exportSchema = false)
abstract class RiceGuardDatabase : RoomDatabase() {
    abstract fun diagnosisDao(): DiagnosisDao

    companion object {
        @Volatile
        private var INSTANCE: RiceGuardDatabase? = null

        fun getDatabase(context: Context): RiceGuardDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RiceGuardDatabase::class.java,
                    "riceguard_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
