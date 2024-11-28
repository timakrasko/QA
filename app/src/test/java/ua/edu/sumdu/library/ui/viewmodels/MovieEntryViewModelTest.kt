package ua.edu.sumdu.library.ui.viewmodels

import android.content.Context
import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import ua.edu.sumdu.library.repository.MovieRepository

@OptIn(ExperimentalCoroutinesApi::class)
class MovieEntryViewModelTest {

    private lateinit var movieRepository: MovieRepository
    private lateinit var viewModel: MovieEntryViewModel

    @Before
    fun setUp() {
        movieRepository = mock(MovieRepository::class.java)
        viewModel = MovieEntryViewModel(movieRepository)
    }

    @Test
    fun `updateUiState updates the state correctly`() {
        val movieDetails = MovieDetails(
            id = 1,
            title = "Test Title",
            description = "Test Description",
            posterPath = "/path/to/poster"
        )

        viewModel.updateUiState(movieDetails)

        assertEquals(movieDetails, viewModel.movieUiState.movieDetails)
        assertTrue(viewModel.movieUiState.isEntryValid)
    }

    @Test
    fun `validateInput returns false when title or description is blank`() {
        val invalidDetails = MovieDetails(title = "", description = "")
        val validDetails = MovieDetails(title = "Title", description = "Description")

        viewModel.updateUiState(invalidDetails)
        assertFalse(viewModel.movieUiState.isEntryValid)

        viewModel.updateUiState(validDetails)
        assertTrue(viewModel.movieUiState.isEntryValid)
    }

    @Test
    fun `saveMovie calls insertMovie when input is valid`() = runBlockingTest {
        val validDetails = MovieDetails(
            id = 1,
            title = "Title",
            description = "Description"
        )
        viewModel.updateUiState(validDetails)

        viewModel.saveMovie()

        verify(movieRepository, times(1)).insertMovie(validDetails.toMovie())
    }


}