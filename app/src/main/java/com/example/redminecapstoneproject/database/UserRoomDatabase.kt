package com.example.redminecapstoneproject.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.redminecapstoneproject.ui.testing.DonorDataRoom
import com.example.redminecapstoneproject.ui.testing.RegisAccountDataRoom

@Database(entities = [RegisAccountDataRoom::class,DonorDataRoom::class], version =3,exportSchema = true)
abstract class UserRoomDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    companion object {
        @Volatile
        private var INSTANCE: UserRoomDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): UserRoomDatabase {
            if (INSTANCE == null) {
                synchronized(UserRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        UserRoomDatabase::class.java, "user_database").fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE as UserRoomDatabase
        }
    }
}