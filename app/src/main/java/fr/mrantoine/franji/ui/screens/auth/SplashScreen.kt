package fr.mrantoine.franji.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.mrantoine.franji.R
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.CategoryStorage
import fr.mrantoine.franji.storage.GrammarStorage
import fr.mrantoine.franji.storage.KanjiStorage
import fr.mrantoine.franji.storage.MainPageStorage
import fr.mrantoine.franji.storage.VocabStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    navController: NavController,
) {
    val context = LocalContext.current
    var showLoader by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {

        val loaderDelay = launch {
            delay(2500)
            showLoader = true
        }

        val splashMinDelay = launch {
            delay(2000)
        }

        val loadingJob = launch {
            val clearCache = false
            if(clearCache) {
                CategoryStorage.clearCache(context)
                KanjiStorage.clearCache(context)
                VocabStorage.clearCache(context)
                MainPageStorage.clearCache(context)
            }


            CategoryStorage.loadCache(context)
            KanjiStorage.loadCache(context)
            VocabStorage.loadCache(context)
            MainPageStorage.loadCache(context)

            //CategoryStorage Loading
            CategoryStorage.getCategoriesPaths()
            CategoryStorage.getCategoriesPaths("Kanji")
            CategoryStorage.getCategoriesPaths("Vocab")

            //KanjiStorage Loading
            val kanjiIdList = CategoryStorage.getCategoryIds("Kanji/Tout/Tout")
            CategoryStorage.getCategoryIds("Kanji/JLPT5/Tout")
            CategoryStorage.getCategoryIds("Kanji/JLPT5/1-20")
            CategoryStorage.getCategoryIds("Kanji/JLPT5/21-40")
            kanjiIdList.forEach { kanji ->
                KanjiStorage.getKanjiById(kanji)
                KanjiStorage.getLottieByKanjiId(kanji)
            }

            //VocabStorage Loading
            val vocabListId = CategoryStorage.getCategoryIds("Vocab/Tout/Tout")
            vocabListId.forEach { vocab ->
                VocabStorage.getVocabById(vocab)
            }

            //GrammarStorage Loading
            val grammarListId = CategoryStorage.getCategoryIds("Grammar/Tout/Tout")
            grammarListId.forEach { grammar ->
                GrammarStorage.getGrammarById(grammar)
            }

            //MainPageStorage Loading
            MainPageStorage.getMainPage("Kanji")
            MainPageStorage.getMainPage("Vocabulaire")
            MainPageStorage.getMainPage("Grammaire")

            CategoryStorage.saveCache(context)
            KanjiStorage.saveCache(context)
            VocabStorage.saveCache(context)
            MainPageStorage.saveCache(context)
        }


        splashMinDelay.join()
        loadingJob.join()

        navController.navigate(Screen.Welcome.route)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_screen),
            contentDescription = "Wallpaper",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (showLoader) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 80.dp)
            )
        }
    }
}