package com.example.indoornavigationar.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.indoornavigationar.database.AppDatabase
import com.example.indoornavigationar.database.navlocations.NavLocations
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class NavLocationsDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val filename = inputData.getString(KEY_FILENAME)
            if (filename != null) {
                applicationContext.assets.open(filename).use { inputStream ->
                    JsonReader(inputStream.reader()).use { jsonReader ->
                        val itemType = object : TypeToken<List<NavLocations>>() {}.type
                        val navLocationsList: List<NavLocations> = Gson().fromJson(jsonReader, itemType)

                        val database = AppDatabase.getInstance(applicationContext)
                        database.navLocationsDao().insertAll(navLocationsList)

                        Result.success()
                    }
                }
            } else {
                Log.e(TAG, "Error seeding database - no valid filename")
                Result.failure()
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "NavLocationsDatabaseWorker"
        const val KEY_FILENAME = "DATA_FILENAME"
    }
}