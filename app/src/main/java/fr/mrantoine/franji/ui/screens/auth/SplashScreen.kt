package fr.mrantoine.franji.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import fr.mrantoine.franji.R
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.CategoryStorage
import fr.mrantoine.franji.storage.KanjiStorage
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController
) {

    ///////////// APPELS RESEAUX //////////////////
    LaunchedEffect(Unit) {
        // Categories
        CategoryStorage.getCategories()
        CategoryStorage.getCategories("Kanji")
        var kanji_char_list = CategoryStorage.getCategoriesKanjiChar("Kanji/JLPT5/Tout")
        CategoryStorage.getCategoriesKanjiChar("Kanji/JLPT5/1-20")
        CategoryStorage.getCategoriesKanjiChar("Kanji/JLPT5/21-40")
        var kanji_id_list = CategoryStorage.getCategoriesKanjiId("Kanji/JLPT5/Tout")
        CategoryStorage.getCategoriesKanjiId("Kanji/JLPT5/1-20")
        CategoryStorage.getCategoriesKanjiId("Kanji/JLPT5/21-40")
        // Kanjis
        kanji_id_list.forEach { kanji ->
            KanjiStorage.getKanjiById(kanji)
            KanjiStorage.getLottieByKanjiId(kanji)
        }
        kanji_char_list.forEach { kanji ->
            KanjiStorage.getKanjiByChar(kanji)
        }
        navController.navigate(Screen.Welcome.route)
    }
    //////////////////////////////////////////////

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_screen),
            contentDescription = "Wallpaper",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}