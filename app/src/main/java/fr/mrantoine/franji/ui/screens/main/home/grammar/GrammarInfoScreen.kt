package fr.mrantoine.franji.ui.screens.main.home.kanji

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.mrantoine.franji.storage.Grammar
import fr.mrantoine.franji.storage.GrammarStorage
import fr.mrantoine.franji.ui.components.HighlightedText
import fr.mrantoine.franji.ui.components.navigation.BottomBar
import fr.mrantoine.franji.ui.components.navigation.HomeTopBar
import fr.mrantoine.franji.ui.theme.Dimens


@Composable
fun GrammarInfoScreen(
    navController: NavController,
    grammarId: String
) {
    Scaffold(
        topBar = {
            HomeTopBar(
                navController = navController,
                selectedCategoryIndex = 3
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
        val scrollState = rememberLazyListState()
        var grammar by remember { mutableStateOf(Grammar()) }

        LaunchedEffect(Unit) {
            grammar = GrammarStorage.getGrammarById(grammarId)
        }

        LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)      // padding fourni par le Scaffold
                .padding(Dimens.s),
            state = scrollState,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = grammar.title,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 38.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Text(
                    text = grammar.subtitle,
                    fontSize = 26.sp,
                    lineHeight = 34.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item( ) {Spacer(modifier = Modifier.height(Dimens.m))}
            item {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = grammar.desc,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start
                )


            }
            item( ) {Spacer(modifier = Modifier.height(Dimens.m))}

            if(grammar.examples.isNotEmpty()) {
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Exemples :",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Start
                    )
                }
                grammar.examples.forEach { example ->
                    item( ) {Spacer(modifier = Modifier.height(Dimens.s))}
                    item {
                        HighlightedText(
                            modifier = Modifier.fillMaxWidth(),
                            text = example.jp,
                            fontSize = 20.sp,
                        )
                    }
                    item {
                        HighlightedText(
                            modifier = Modifier.fillMaxWidth(),
                            text = example.lecture,
                            fontSize = 20.sp,
                            fontStyle = FontStyle.Italic,
                            color = Color.Gray
                        )
                    }
                    item {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = example.fr,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                    item {
                        HorizontalDivider(
                            color = Color.LightGray,
                            thickness = 1.dp
                        )
                    }
                }

            }


        }
    }
}
