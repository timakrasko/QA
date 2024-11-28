package ua.edu.sumdu.library.repository

import kotlinx.coroutines.flow.Flow
import ua.edu.sumdu.library.data.Movie

interface MovieRepository {
    fun getAllMoviesStream(): Flow<List<Movie>>

    fun getMovieStream(id: Int): Flow<Movie?>

    suspend fun insertMovie(movie: Movie)

    suspend fun deleteMovie(movie: Movie)

    suspend fun updateMovie(movie: Movie)
}