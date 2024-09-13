package ua.edu.sumdu.filmlibrary.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ua.edu.sumdu.filmlibrary.ui.screen.MainDestination
import ua.edu.sumdu.filmlibrary.ui.screen.MainScreen
import ua.edu.sumdu.filmlibrary.ui.screen.MovieDestination
import ua.edu.sumdu.filmlibrary.ui.screen.MovieEditDestination
import ua.edu.sumdu.filmlibrary.ui.screen.MovieEditScreen
import ua.edu.sumdu.filmlibrary.ui.screen.MovieEntryDestination
import ua.edu.sumdu.filmlibrary.ui.screen.MovieEntryScreen
import ua.edu.sumdu.filmlibrary.ui.screen.MovieScreen

@Composable
fun MovieNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = MainDestination.route,
        modifier = modifier
    ){
        composable(route = MainDestination.route) {
            MainScreen(
                navigationToMovieEntry = { navController.navigate(MovieEntryDestination.route) },
                navigationToMovieUpdate = {
                    navController.navigate("${MovieDestination.route}/${it}")
                }
            )
        }
        composable(route = MovieEntryDestination.route) {
            MovieEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = MovieDestination.routeWithArgs,
            arguments = listOf(navArgument(MovieDestination.movieIdArg) {
                type = NavType.IntType
            })
        ) {
            MovieScreen(
                navigateToEditMovie = { navController.navigate("${MovieEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = MovieEditDestination.routeWithArgs,
            arguments = listOf(navArgument(MovieDestination.movieIdArg) {
                type = NavType.IntType
            })
        ) {
            MovieEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}