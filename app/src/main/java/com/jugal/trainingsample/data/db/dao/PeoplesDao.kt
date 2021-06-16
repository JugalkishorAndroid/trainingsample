package com.jugal.trainingsample.data.db.dao

import androidx.room.*
import com.jugal.trainingsample.data.db.schema.Peoples

@Dao
interface PeoplesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPeopleList(vararg products: Peoples)

    @Query("SELECT * FROM peoples")
    fun getAll(): List<Peoples>

}