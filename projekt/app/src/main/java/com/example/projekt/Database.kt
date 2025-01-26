package com.example.projekt

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import java.util.Date
import androidx.room.TypeConverter
import androidx.room.TypeConverters


class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}


@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val priority: Int,
    val date: Date
)

@Dao
interface TaskDao {
    @Query("SELECT * FROM Task")
    suspend fun getAllTasks(): List<Task>

    @Insert
    suspend fun insertTask(grade: Task)

    @Update
    suspend fun updateTask(grade: Task)

    @Delete
    suspend fun deleteTask(grade: Task)
}

@Database(entities = [Task::class], version = 1)
@TypeConverters(Converters::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun TaskDao(): TaskDao
}