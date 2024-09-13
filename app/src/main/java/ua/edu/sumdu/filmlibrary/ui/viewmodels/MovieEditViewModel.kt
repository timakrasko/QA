package ua.edu.sumdu.filmlibrary.ui.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ua.edu.sumdu.filmlibrary.repository.MovieRepository
import ua.edu.sumdu.filmlibrary.ui.screen.MovieEditDestination
import java.io.File
import java.io.FileOutputStream

class MovieEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val movieRepository: MovieRepository
) : ViewModel(){

    var movieUiState by mutableStateOf(MovieUiState())
        private set

    private val movieId: Int = checkNotNull(savedStateHandle[MovieEditDestination.movieIdArg])

    init {
        viewModelScope.launch {
            movieUiState = movieRepository.getMovieStream(movieId)
                .filterNotNull()
                .first()
                .toMovieUiState()
        }
    }

    private fun validateInput(uiState: MovieDetails = movieUiState.movieDetails): Boolean {
        return with(uiState) {
            title.isNotBlank() && description.isNotBlank()
        }
    }

    suspend fun updateMovie() {
        if (validateInput(movieUiState.movieDetails)) {
            movieRepository.updateMovie(movieUiState.movieDetails.toMovie())
        }
    }

    fun updateUiState(movieDetails: MovieDetails) {
        movieUiState = MovieUiState(movieDetails = movieDetails, isEntryValid = validateInput(movieDetails))
    }

    fun savePosterImage(context: Context, uri: Uri): String? {
        return saveImageToInternalStorage(context, uri)
    }

    fun saveImageToInternalStorage(context: Context, imageUri: Uri): String? {
        try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            val filename = "image_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, filename)

            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}