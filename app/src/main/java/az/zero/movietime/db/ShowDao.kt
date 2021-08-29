package az.zero.movietime.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import az.zero.movietime.data.Show

@Dao
interface ShowDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllShows(shows: List<Show>)

    @Query("DELETE FROM shows_table")
    suspend fun deleteAllShows()
}