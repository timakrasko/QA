package ua.edu.sumdu.filmlibrary.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import ua.edu.sumdu.filmlibrary.LibraryTopAppBar
import ua.edu.sumdu.filmlibrary.R
import ua.edu.sumdu.filmlibrary.ui.AppViewModelProvider
import ua.edu.sumdu.filmlibrary.ui.navigation.NavigationDestination
import ua.edu.sumdu.filmlibrary.ui.theme.FilmLibraryTheme
import ua.edu.sumdu.filmlibrary.ui.viewmodels.MovieEditViewModel

object MovieEditDestination : NavigationDestination {
    override val route = "movie_edit"
    override val titleRes = R.string.edit_movie_title
    const val movieIdArg = "movieId"
    val routeWithArgs = "$route/{$movieIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MovieEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
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
                title = stringResource(MovieEditDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        modifier = modifier
    ) { innerPadding ->
        MovieEntryBody(
            movieUiState = viewModel.movieUiState,
            onMovieValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateMovie()
                    navigateBack()
                }
            },
            onImageSelectClick = { galleryLauncher.launch("image/*") },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding()
                )
                .verticalScroll(rememberScrollState())
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MovieEditScreenPreview() {
    FilmLibraryTheme {
        MovieEditScreen(navigateBack = {}, onNavigateUp = {})
    }
}