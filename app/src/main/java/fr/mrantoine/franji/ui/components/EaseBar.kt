package fr.mrantoine.franji.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import fr.mrantoine.franji.ui.theme.Dimens

@Composable
fun EaseBarItem(
    modifier: Modifier = Modifier,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color)
            .clickable { onClick() }
            .height(Dimens.xxxl),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun EaseBar(
    modifier: Modifier = Modifier,
    items: Map<String, Pair<Color, () -> Unit>> = emptyMap()
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            items.entries.forEachIndexed { index, entry ->
                val label = entry.key
                val (color, action) = entry.value

                EaseBarItem(
                    onClick = action,
                    modifier = Modifier.weight(1f),
                    label = label,
                    color = color
                )
            }
        }
    }
}