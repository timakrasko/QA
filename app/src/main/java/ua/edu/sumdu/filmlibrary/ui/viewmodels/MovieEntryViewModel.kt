package ua.edu.sumdu.filmlibrary.ui.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ua.edu.sumdu.filmlibrary.data.Movie
import ua.edu.sumdu.filmlibrary.repository.MovieRepository
import java.io.File
import java.io.FileOutputStream

class MovieEntryViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    var movieUiState by mutableStateOf(MovieUiState())
        private set

    fun updateUiState (movieDetails: MovieDetails) {
        movieUiState =
            MovieUiState(movieDetails = movieDetails, isEntryValid = validateInput(movieDetails))
    }

    private fun validateInput(uiState: MovieDetails = movieUiState.movieDetails): Boolean {
        return  with(uiState) {
            title.isNotBlank() && description.isNotBlank()
        }
    }

    suspend fun saveMovie() {
        if (validateInput()) {
            movieRepository.insertMovie(movieUiState.movieDetails.toMovie())
        }
    }

    fun savePosterImage(context: Context, uri: Uri): String? {
        return saveImageToInternalStorage(context, uri)
    }

    fun saveImageToInternalStorage(context: Context, imageUri: Uri): String? {
        try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            // Створюємо файл у внутрішньому сховищі
            val filename = "image_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, filename)

            // Зберігаємо Bitmap у файл
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            // Повертаємо шлях до збереженого зображення
            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}

data class MovieUiState(
    val movieDetails: MovieDetails = MovieDetails(),
    val isEntryValid: Boolean = false
)

data class MovieDetails(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val posterPath: String = ""
)

fun MovieDetails.toMovie(): Movie = Movie(
    id = id,
    title = title,
    description = description,
    posterPath = posterPath,
    addedBy = ""
)


fun Movie.toMovieUiState(isEntryValid: Boolean = false): MovieUiState = MovieUiState(
    movieDetails = this.toMovieDetails(),
    isEntryValid = isEntryValid
)


fun Movie.toMovieDetails(): MovieDetails = MovieDetails(
    id = id,
    title = title,
    description = description
)