package ua.edu.sumdu.library.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(movie: Movie)

    @Update
    suspend fun update(movie: Movie)

    @Delete
    suspend fun delete(movie: Movie)

    @Query("SELECT * FROM movie ORDER BY title")
    fun getAllMovies(): Flow<List<Movie>>

    @Query("SELECT * FROM movie WHERE id == :id ORDER BY title")
    fun getMovieById(id: Int): Flow<Movie>
}