package fr.mrantoine.franji.ui.screens.main.home

import Kanji
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import fr.mrantoine.franji.ui.components.Lottie
import fr.mrantoine.franji.ui.theme.Dimens
import getKanjiByCharId
import getLottie

@Composable
fun KanjiListScreen(
    onKanjiClick: ((Kanji) -> Unit) = {},
    path: String,
    kanjis: Array<String> = emptyArray()
) {

    var title = path.uppercase().replace("/", " ")
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(Dimens.m)
    ) {
        Text(
            text = "- $title -",
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.m),
            textAlign = TextAlign.Center
        )

        val columns = 5
        val rows = (kanjis.size + columns - 1) / columns

        Column {
            for (rowIndex in 0 until rows) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (colIndex in 0 until columns) {
                        val kanjiIndex = rowIndex * columns + colIndex
                        var kanji by remember { mutableStateOf<Kanji>(Kanji("", emptyList(), "")) }
                        if (kanjiIndex < kanjis.size) {
                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .weight(1f)
                                    .padding(Dimens.s)
                                    .clickable {
                                        onKanjiClick(kanji)
                                    },
                                contentAlignment = Alignment.Center
                            ) {


                                LaunchedEffect(kanjis[kanjiIndex]) {
                                    kanji = getKanjiByCharId(kanjis[kanjiIndex])
                                }

                                Text(
                                    text = kanji?.kanji ?: "",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}