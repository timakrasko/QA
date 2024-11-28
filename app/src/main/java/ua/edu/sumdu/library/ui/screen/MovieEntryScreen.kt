package ua.edu.sumdu.library.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import ua.edu.sumdu.library.R
import ua.edu.sumdu.library.LibraryTopAppBar
import ua.edu.sumdu.library.ui.AppViewModelProvider
import ua.edu.sumdu.library.ui.navigation.NavigationDestination
import ua.edu.sumdu.library.ui.theme.MovieLibraryTheme
import ua.edu.sumdu.library.ui.viewmodels.MovieDetails
import ua.edu.sumdu.library.ui.viewmodels.MovieEntryViewModel
import ua.edu.sumdu.library.ui.viewmodels.MovieUiState

object MovieEntryDestination : NavigationDestination {
    override val route = "movie_entry"
    override val titleRes = R.string.movie_entry_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: MovieEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val posterPath = viewModel.savePosterImage(context, it)
            posterPath?.let { path ->
                viewModel.updateUiState(viewModel.movieUiState.movieDetails.copy(posterPath = path))
            }
        }
    }

    Scaffold(
        topBar = {
            LibraryTopAppBar(
                title = stringResource(MovieEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        MovieEntryBody(
            movieUiState = viewModel.movieUiState,
            onMovieValueChange = viewModel::updateUiState,
            onImageSelectClick = { galleryLauncher.launch("image/*") }, // Додаємо виклик вибору зображення
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveMovie()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding()
                )
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun MovieEntryBody(
    movieUiState: MovieUiState,
    onMovieValueChange: (MovieDetails) -> Unit,
    onImageSelectClick: () -> Unit, // Додаємо параметр для вибору зображення
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        MovieInputForm(
            movieDetails = movieUiState.movieDetails,
            onValueChange = onMovieValueChange,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onImageSelectClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.select_image))
        }
        movieUiState.movieDetails.posterPath.takeIf { it.isNotBlank() }?.let { imagePath ->
            Text(text = "Selected image: $imagePath")
        }

        Button(
            onClick = onSaveClick,
            enabled = movieUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_action))
        }
    }
}

@Composable
fun MovieInputForm(
    movieDetails: MovieDetails,
    modifier: Modifier = Modifier,
    onValueChange: (MovieDetails) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        OutlinedTextField(
            value = movieDetails.title,
            onValueChange = { onValueChange(movieDetails.copy(title = it)) },
            label = { Text(stringResource(R.string.movie_title_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = movieDetails.description,
            onValueChange = { onValueChange(movieDetails.copy(description = it)) },
            label = { Text(stringResource(R.string.movie_description_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        if (enabled) {
            Text(
                text = stringResource(R.string.required_fields),
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieEntryScreenPreview() {
    MovieLibraryTheme {
        MovieEntryBody(movieUiState = MovieUiState(
            MovieDetails(
                title = "Movie title", description = "description"
            )
        ), onMovieValueChange = {}, onSaveClick = {}, onImageSelectClick = {})
    }
}
