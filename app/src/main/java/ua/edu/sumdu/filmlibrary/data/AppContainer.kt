package ua.edu.sumdu.filmlibrary.data

import android.content.Context
import ua.edu.sumdu.filmlibrary.repository.MovieRepository
import ua.edu.sumdu.filmlibrary.repository.OfflineMovieRepository
import ua.edu.sumdu.filmlibrary.data.MovieDatabase

interface AppContainer {
    val movieRepository: MovieRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val movieRepository: MovieRepository by lazy {
        OfflineMovieRepository(MovieDatabase.getDatabase(context).movieDao())
    }
}
