package ua.edu.sumdu.library.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import ua.edu.sumdu.library.R
import ua.edu.sumdu.library.LibraryTopAppBar
import ua.edu.sumdu.library.data.Movie
import ua.edu.sumdu.library.ui.AppViewModelProvider
import ua.edu.sumdu.library.ui.navigation.NavigationDestination
import ua.edu.sumdu.library.ui.theme.MovieLibraryTheme
import ua.edu.sumdu.library.ui.viewmodels.MainViewModel

object MainDestination : NavigationDestination{
    override val route = "main"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navigationToMovieEntry: () -> Unit,
    navigationToMovieUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val mainUiState by viewModel.mainUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LibraryTopAppBar(
                title = stringResource(MainDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigationToMovieEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.movie_entry_title)
                )
            }
        },
    ) { innerPadding ->
        MainBody(
            movieList = mainUiState.movieList,
            onMovieClick = navigationToMovieUpdate,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = innerPadding,
        )
    }
}

@Composable
private fun MainBody(
    movieList: List<Movie>,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        if (movieList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_movies),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(contentPadding),
            )
        } else {
            MovieList(
                movieList = movieList,
                onMovieClick = { onMovieClick(it.id) },
                contentPadding = contentPadding,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun MovieList(
    movieList: List<Movie>,
    onMovieClick: (Movie) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = movieList, key = { it.id}) { movie ->
            MovieItem(movie = movie,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onMovieClick(movie) }
            )
        }
    }
}

@Composable
private fun MovieItem(
    movie: Movie,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(170.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                val painter = rememberAsyncImagePainter(
                    model = movie.posterPath.takeIf { it.isNotBlank() }?.toUri() ?: R.drawable.lotr
                )
                Image(
                    painter = painter,
                    contentDescription = "Movie poster",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .width(120.dp)
                        .fillMaxHeight()
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = movie.title,
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = movie.description,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeBodyPreview() {
    MovieLibraryTheme {
        MainBody(listOf(
            Movie(1, "Barby", "", "", ""),
            Movie(2, "Barby2", "", "", "")
        ), onMovieClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun HomeBodyEmptyListPreview() {
    MovieLibraryTheme {
        MainBody(listOf(), onMovieClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun MoviePreview() {
    MovieLibraryTheme {
        MovieItem(
            Movie(1, "Barby", "", "", "")
        )
    }
}
