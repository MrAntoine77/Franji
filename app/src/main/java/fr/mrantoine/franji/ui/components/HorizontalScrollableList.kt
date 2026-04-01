package fr.mrantoine.franji.ui.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.mrantoine.franji.ui.theme.Dimens

@Composable
fun HorizontalScrollableList(
    modifier: Modifier = Modifier,
    items: List<String>,
    itemWidth: Dp = 200.dp,
    itemHeight: Dp = 200.dp,
    onItemClick: (index: Int) -> Unit = {}
) {
    LazyRow(
        modifier = modifier.padding(bottom = Dimens.m),
        contentPadding = PaddingValues(horizontal = Dimens.m),
        horizontalArrangement = Arrangement.spacedBy(Dimens.m)
    ) {
        itemsIndexed(items) { index, item ->
            Box(
                modifier = Modifier
                    .width(itemWidth)
                    .height(itemHeight)
                    .clickable { onItemClick(index) }
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}