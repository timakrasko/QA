package ua.edu.sumdu.library.repository

import kotlinx.coroutines.flow.Flow
import ua.edu.sumdu.library.data.Movie
import ua.edu.sumdu.library.data.MovieDao

class OfflineMovieRepository(private val movieDao: MovieDao): MovieRepository {
    override fun getAllMoviesStream(): Flow<List<Movie>> = movieDao.getAllMovies()

    override fun getMovieStream(id: Int): Flow<Movie?> = movieDao.getMovieById(id)

    override suspend fun insertMovie(movie: Movie) = movieDao.insert(movie)

    override suspend fun deleteMovie(movie: Movie) = movieDao.delete(movie)

    override suspend fun updateMovie(movie: Movie) = movieDao.update(movie)

}