package ua.edu.sumdu.library.data

import android.content.Context
import ua.edu.sumdu.library.repository.MovieRepository
import ua.edu.sumdu.library.repository.OfflineMovieRepository

interface AppContainer {
    val movieRepository: MovieRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val movieRepository: MovieRepository by lazy {
        OfflineMovieRepository(MovieDatabase.getDatabase(context).movieDao())
    }
}
