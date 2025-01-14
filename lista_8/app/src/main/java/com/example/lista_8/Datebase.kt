package com.example.lista_8

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update

@Entity
data class Grade(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subject: String,
    val grade: Float
)

@Dao
interface GradeDao {
    @Query("SELECT * FROM Grade")
    suspend fun getAllGrades(): List<Grade>

    @Insert
    suspend fun insertGrade(grade: Grade)

    @Update
    suspend fun updateGrade(grade: Grade)

    @Delete
    suspend fun deleteGrade(grade: Grade)
}

@Database(entities = [Grade::class], version = 1)
abstract class GradeDatabase : RoomDatabase() {
    abstract fun gradeDao(): GradeDao
}