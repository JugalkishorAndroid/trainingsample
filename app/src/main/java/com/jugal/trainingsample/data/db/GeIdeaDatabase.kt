package com.jugal.trainingsample.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jugal.trainingsample.data.db.dao.PeoplesDao
import com.jugal.trainingsample.data.db.schema.Peoples

@Database(
    entities = [
        Peoples::class
    ], version = 1, exportSchema = true
)
abstract class GeIdeaDatabase : RoomDatabase() {
    abstract fun peoplesDao(): PeoplesDao


    companion object Factory {
        private const val DATABASE_NAME = "ge_idea_.db"
        fun create(c: Context): GeIdeaDatabase =
            Room.databaseBuilder(c, GeIdeaDatabase::class.java, DATABASE_NAME).build()
    }

}