package com.example.redminecapstoneproject.di

import android.app.Activity
import android.content.Context
import com.example.redminecapstoneproject.database.UserRoomDatabase
import com.example.redminecapstoneproject.repository.Repository

object Injection {
    fun provideRepository(context: Context): Repository {
        val database = UserRoomDatabase.getDatabase(context)
        //val apiService = ApiConfig.getApiService()
        return Repository(database,context)
    }
}