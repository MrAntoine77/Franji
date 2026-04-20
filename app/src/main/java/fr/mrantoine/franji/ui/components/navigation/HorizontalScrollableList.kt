package fr.mrantoine.franji.ui.components.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.MainPage
import fr.mrantoine.franji.ui.theme.Dimens
import androidx.compose.foundation.layout.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import fr.mrantoine.franji.R
import fr.mrantoine.franji.ui.screens.main.home.grammar.GrammarState
import fr.mrantoine.franji.ui.screens.main.home.kanji.KanjiState
import fr.mrantoine.franji.ui.screens.main.home.vocab.VocabState

@Composable
fun HorizontalScrollableList(
    navController: NavController,
    modifier: Modifier = Modifier,
    page: MainPage
) {
    LazyRow(
        modifier = modifier.padding(bottom = Dimens.m),
        contentPadding = PaddingValues(horizontal = Dimens.m),
        horizontalArrangement = Arrangement.spacedBy(Dimens.m)
    ) {

        items(page.items.size) { index ->

            val context = LocalContext.current
            val drawableId = context.resources.getIdentifier(page.items[index].img, "drawable", context.packageName)


            Box(
                modifier = Modifier
                    .width(page.width.dp)
                    .height(page.height.dp)
                    .clickable {
                        val path = page.items[index].path
                        if(path.startsWith("Kanji")) {
                            navController.navigate(Screen.KanjiList.route(
                                state = KanjiState.Category,
                                categoryPath = path,
                                kanjiId = ""
                            ))
                        }
                        else if(path.startsWith("Vocab")) {
                            navController.navigate(Screen.VocabList.route(
                                state = VocabState.Category,
                                categoryPath = path,
                                vocabId = ""
                            ))
                        }
                        else if(path.startsWith("Grammar")) {
                            navController.navigate(Screen.GrammarList.route(
                                state = GrammarState.Category,
                                categoryPath = path,
                                grammarId = ""
                            ))
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (drawableId != 0) {
                    Image(
                        painter = painterResource(id = drawableId),
                        contentDescription = "Wallpaper",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(page.items[index].color.removePrefix("0x").toLong(16))
                                )
                            )
                        )
                )

                Text(
                    text = page.items[index].path.replace("/", " "),
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                )
            }
        }
    }
}