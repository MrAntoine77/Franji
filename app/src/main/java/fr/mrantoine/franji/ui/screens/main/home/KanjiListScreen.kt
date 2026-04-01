package fr.mrantoine.franji.ui.screens.main.home

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import fr.mrantoine.franji.ui.components.Tree
import fr.mrantoine.franji.ui.theme.Dimens

@Composable
fun KanjiListScreen(
    onKanjiClick: (() -> Unit) = {},
    title: String = "titre"
) {
    val kanjis = listOf(
        "日", "月", "火", "水", "木", "金", "土", "山", "川", "田",
        "人", "口", "目", "耳", "手", "足", "力", "心", "雨", "空",
        "花", "草", "魚", "鳥", "犬", "猫", "虫", "車", "門", "山",
        "石", "森", "海", "川", "火", "風", "雨", "雪", "天", "地",
        "王", "玉", "金", "銀", "銅", "鉄", "刀", "弓", "矢", "弦"
    ) // 50 kanjis
    var uppercaseTitle = title.uppercase()
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(Dimens.m)
    ) {
        Text(
            text = "- $uppercaseTitle -",
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
                        if (kanjiIndex < kanjis.size) {
                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .weight(1f)
                                    .padding(Dimens.s)
                                    .clickable {
                                        onKanjiClick()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = kanjis[kanjiIndex],
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