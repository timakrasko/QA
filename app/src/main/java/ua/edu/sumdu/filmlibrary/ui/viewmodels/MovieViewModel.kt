package ua.edu.sumdu.filmlibrary.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ua.edu.sumdu.filmlibrary.data.Movie
import ua.edu.sumdu.filmlibrary.repository.MovieRepository
import ua.edu.sumdu.filmlibrary.ui.screen.MovieDestination

class MovieViewModel(
    savedStateHandle: SavedStateHandle,
    private val movieRepository: MovieRepository
): ViewModel() {
    private val movieId: Int = checkNotNull(savedStateHandle[MovieDestination.movieIdArg])

    val uiState: StateFlow<MovieDetailsUiState> =
        movieRepository.getMovieStream(movieId)
            .filterNotNull()
            .map {
                MovieDetailsUiState(movieDetails = it.toMovieDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = MovieDetailsUiState()
            )

    suspend fun deleteMovie(){
        val currentMovie = uiState.value.movieDetails.toMovie()
        movieRepository.deleteMovie(currentMovie)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class MovieDetailsUiState(
    val movieDetails: MovieDetails = MovieDetails()
)

