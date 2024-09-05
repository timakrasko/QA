package ua.edu.sumdu.filmlibrary.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ua.edu.sumdu.filmlibrary.data.Movie
import ua.edu.sumdu.filmlibrary.repository.MovieRepository

class MainViewModel(movieRepository: MovieRepository): ViewModel() {
    val mainUiState: StateFlow<MainUiState> =
        movieRepository.getAllMoviesStream().map { MainUiState(it) }.
        stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = MainUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class MainUiState(val movieList: List<Movie> = listOf())