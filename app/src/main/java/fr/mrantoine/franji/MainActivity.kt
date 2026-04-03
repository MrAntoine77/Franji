package fr.mrantoine.franji

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavType
import fr.mrantoine.franji.ui.screens.auth.SplashScreen
import fr.mrantoine.franji.ui.screens.auth.WelcomeScreen
import fr.mrantoine.franji.ui.theme.FranjiTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fr.mrantoine.franji.ui.screens.auth.*
import fr.mrantoine.franji.ui.screens.main.cards.CardsListScreen
import fr.mrantoine.franji.ui.screens.main.cards.CardsPlayingScreen
import fr.mrantoine.franji.ui.screens.main.cards.CardsRecapScreen
import fr.mrantoine.franji.ui.screens.main.home.AllScreen
import fr.mrantoine.franji.ui.screens.main.home.KanjiCategoryListScreen
import fr.mrantoine.franji.ui.screens.main.home.KanjiCategoryScreen
import fr.mrantoine.franji.ui.screens.main.home.KanjiInfoScreen


sealed class Screen(val route: String) {

    data object Splash : Screen("splash")
    data object Welcome : Screen("welcome")
    data object Register : Screen("register")
    data object Login : Screen("login")
    data object HomeAll : Screen("home_all")
    data object KanjiCategoryList : Screen("kanji_category_list")

    data object KanjiCategory : Screen("kanji_category/{categoryPath}") {
        const val ARG = "categoryPath"
        fun route(categoryPath: String) = "kanji_category/${Uri.encode(categoryPath)}"
    }
    data object KanjiInfo : Screen("kanji_info/{kanjiChar}") {
        const val ARG = "kanjiChar"
        fun route(kanjiChar: String) = "kanji_info/${Uri.encode(kanjiChar)}"
    }

    data object CardsList : Screen("cards_list")

    data object CardsRecap : Screen("cards_recap/{cardsPath}") {
        const val ARG = "cardsPath"
        fun route(cardsPath: String) = "cards_recap/${Uri.encode(cardsPath)}"
    }


    data object CardsPlaying : Screen("cards_playing/{cardsArray}/{title}") {
        const val ARG_CARDS = "cardsArray"
        const val ARG_TITLE = "title"

        fun route(cardsArray: Array<String>, title: String): String {
            val encodedCards = Uri.encode(cardsArray.joinToString(","))
            val encodedTitle = Uri.encode(title)
            return "cards_playing/$encodedCards/$encodedTitle"
        }
    }
}


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FranjiTheme(dynamicColor = false) {
                val navController = rememberNavController()


                NavHost(
                    navController = navController,
                    startDestination = Screen.Splash.route,
                    enterTransition = { fadeIn(animationSpec = tween(0)) },
                    exitTransition = { fadeOut(animationSpec = tween(0)) },
                    popEnterTransition = { fadeIn(animationSpec = tween(0)) },
                    popExitTransition = { fadeOut(animationSpec = tween(0)) }
                ) {
                    composable(Screen.Splash.route) {
                        SplashScreen(navController)
                    }
                    composable(Screen.Welcome.route) {
                        WelcomeScreen(navController)
                    }
                    composable(Screen.Register.route) {
                        RegisterScreen(navController)
                    }
                    composable(Screen.Login.route) {
                        LoginScreen(navController)
                    }
                    composable(Screen.HomeAll.route) {
                        AllScreen(navController)
                    }
                    composable(Screen.KanjiCategoryList.route) {
                        KanjiCategoryListScreen(navController)
                    }
                    composable(
                        route = Screen.KanjiCategory.route,
                        arguments = listOf(navArgument(Screen.KanjiCategory.ARG) {
                            type = NavType.StringType
                        })
                    ) { backStackEntry ->

                        val categoryPath = backStackEntry.arguments
                            ?.getString(Screen.KanjiCategory.ARG)
                            ?.let { Uri.decode(it) } ?: ""

                        KanjiCategoryScreen(navController, categoryPath)
                    }
                    composable(
                        route = Screen.KanjiInfo.route,
                        arguments = listOf(navArgument(Screen.KanjiInfo.ARG) {
                            type = NavType.StringType
                        })
                    ) { backStackEntry ->

                        val kanjiChar = backStackEntry.arguments
                            ?.getString(Screen.KanjiInfo.ARG)
                            ?.let { Uri.decode(it) } ?: ""

                        KanjiInfoScreen(navController, kanjiChar)
                    }

                    composable(Screen.CardsList.route) {
                        CardsListScreen(navController)
                    }

                    composable(
                        route = Screen.CardsRecap.route,
                        arguments = listOf(navArgument(Screen.CardsRecap.ARG) {
                            type = NavType.StringType
                        })
                    ) { backStackEntry ->

                        val cardsPath = backStackEntry.arguments
                            ?.getString(Screen.CardsRecap.ARG)
                            ?.let { Uri.decode(it) } ?: ""

                        CardsRecapScreen(navController, cardsPath)
                    }

                    composable(
                        route = Screen.CardsPlaying.route,
                        arguments = listOf(
                            navArgument(Screen.CardsPlaying.ARG_CARDS) { type = NavType.StringType },
                            navArgument(Screen.CardsPlaying.ARG_TITLE) { type = NavType.StringType }
                        )
                    ) { backStackEntry ->

                        val cardsPlayingString = backStackEntry.arguments
                            ?.getString(Screen.CardsPlaying.ARG_CARDS)
                            ?.let { Uri.decode(it) }
                            ?: ""

                        val title = backStackEntry.arguments
                            ?.getString(Screen.CardsPlaying.ARG_TITLE)
                            ?.let { Uri.decode(it) }
                            ?: ""

                        val cardsArray: MutableList<String> =
                            if (cardsPlayingString.isEmpty()) mutableListOf()
                            else cardsPlayingString.split(",").toMutableList()

                        CardsPlayingScreen(
                            navController = navController,
                            cardsArray = cardsArray,
                            title = title
                        )
                    }
                }
            }
        }
    }
}