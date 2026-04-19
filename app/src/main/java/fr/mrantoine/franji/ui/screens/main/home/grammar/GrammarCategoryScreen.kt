package fr.mrantoine.franji.ui.screens.main.home.kanji

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.CategoryStorage
import fr.mrantoine.franji.storage.Grammar
import fr.mrantoine.franji.storage.GrammarStorage
import fr.mrantoine.franji.ui.components.navigation.BottomBar
import fr.mrantoine.franji.ui.components.navigation.HomeTopBar
import fr.mrantoine.franji.ui.theme.Dimens

@Composable
fun GrammarCategoryScreen(
    navController: NavController,
    categoryPath: String
) {
    Scaffold(
        topBar = {
            HomeTopBar(
                navController = navController,
                selectedCategoryIndex = 3,
                redirectRoute = Screen.SearchGrammar.route
            )
        },
        bottomBar = {
            BottomBar(
                modifier = Modifier.navigationBarsPadding(),
                selectedIndex = 0,
                navController = navController
            )
        }
    ) { innerPadding ->

        var grammar_list_id by remember { mutableStateOf(emptyArray<String>()) }
        val grammar_list = remember { mutableStateListOf<Grammar>() }
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            grammar_list_id = CategoryStorage.getCategoryByPath(context, categoryPath)
            grammar_list_id.forEach { grammarId ->
                grammar_list.add(GrammarStorage.getGrammarById(context, grammarId))
            }
        }

        Box(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            val title = categoryPath.uppercase().replace("/", " ")
            LazyColumn(
                modifier = Modifier
                    .padding(Dimens.m)
            ) {
                item {
                    Text(
                        text = "- $title -",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Dimens.m),
                        textAlign = TextAlign.Center
                    )
                }

                grammar_list.forEach { grammar ->
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(Screen.GrammarInfo.route(grammar.id))
                                }
                                .padding(
                                    top = Dimens.s,
                                    bottom = Dimens.s
                                ),
                        ) {
                            Text(
                                text = "${grammar.title} - ${grammar.subtitle}",
                                fontSize = 16.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.tertiary,
                            thickness = 1.dp
                        )
                    }
                }
            }
        }
    }
}