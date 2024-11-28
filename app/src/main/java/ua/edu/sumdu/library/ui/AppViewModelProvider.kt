package ua.edu.sumdu.library.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ua.edu.sumdu.library.LibraryApplication
import ua.edu.sumdu.library.ui.viewmodels.MainViewModel
import ua.edu.sumdu.library.ui.viewmodels.MovieEditViewModel
import ua.edu.sumdu.library.ui.viewmodels.MovieEntryViewModel
import ua.edu.sumdu.library.ui.viewmodels.MovieViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            MovieEntryViewModel(libraryApplication().container.movieRepository)
        }

        initializer {
            MovieViewModel(
                this.createSavedStateHandle(),
                libraryApplication().container.movieRepository
            )
        }

        initializer {
            MainViewModel(
                libraryApplication().container.movieRepository
            )
        }
        initializer {
            MovieEditViewModel(
                this.createSavedStateHandle(),
                libraryApplication().container.movieRepository
            )
        }
    }
}


/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.libraryApplication(): LibraryApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as LibraryApplication)