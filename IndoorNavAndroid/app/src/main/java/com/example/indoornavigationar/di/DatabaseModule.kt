package com.example.indoornavigationar.di

import android.content.Context
import com.example.indoornavigationar.database.AppDatabase
import com.example.indoornavigationar.database.navlocations.NavLocationsDao
import com.example.indoornavigationar.database.search.SearchItemsDao
import com.example.indoornavigationar.utilites.CheckMatchingCoordinates
import com.example.indoornavigationar.utilites.PathCreator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideSearchItemsDao(appDatabase: AppDatabase): SearchItemsDao {
        return appDatabase.searchItemsDao()
    }

    @Provides
    fun provideNavLocationsDao(appDatabase: AppDatabase): NavLocationsDao {
        return appDatabase.navLocationsDao()
    }

    @Provides
    fun provideCheckMatchingCoordinates(): CheckMatchingCoordinates {
        return CheckMatchingCoordinates()
    }

    @Provides
    fun providePathCreator(@ApplicationContext context: Context): PathCreator {
        return PathCreator(getApplication(context))
    }
}