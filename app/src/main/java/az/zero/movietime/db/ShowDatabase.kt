package az.zero.movietime.db

import androidx.room.Database
import androidx.room.RoomDatabase
import az.zero.movietime.data.Show

@Database(entities = [Show::class], version = 1)
abstract class ShowDatabase : RoomDatabase() {
    abstract fun showDao(): ShowDao
}