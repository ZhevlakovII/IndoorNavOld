package com.example.indoornavigationar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.indoornavigationar.database.navlocations.NavLocations
import com.example.indoornavigationar.database.navlocations.NavLocationsDao
import com.example.indoornavigationar.database.search.SearchItems
import com.example.indoornavigationar.database.search.SearchItemsDao
import com.example.indoornavigationar.utilites.DATABASE_NAME
import com.example.indoornavigationar.utilites.DATA_FILENAME
import com.example.indoornavigationar.workers.NavLocationsDatabaseWorker
import com.example.indoornavigationar.workers.NavLocationsDatabaseWorker.Companion.KEY_FILENAME

@Database(entities = [SearchItems::class, NavLocations::class], version = 6)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchItemsDao(): SearchItemsDao
    abstract fun navLocationsDao(): NavLocationsDao

    companion object {

        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {

            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val request = OneTimeWorkRequestBuilder<NavLocationsDatabaseWorker>()
                                .setInputData(workDataOf(KEY_FILENAME to DATA_FILENAME))
                                .build()
                            WorkManager.getInstance(context).enqueue(request)
                        }
                    }
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}