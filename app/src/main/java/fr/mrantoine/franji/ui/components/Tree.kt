package fr.mrantoine.franji.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import fr.mrantoine.franji.ui.theme.Dimens
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
@Composable
fun TreeNode(
    onClick: () -> Unit = {},
    text: String = "",
    isExpanded: Boolean = false
) {
    Column(
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.s),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.s)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = text,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = text,
                    color = if (isExpanded) Color.Black else Color.Gray,
                    fontWeight = if (isExpanded) FontWeight.SemiBold else FontWeight.Normal,
                    fontSize = 20.sp
                )
            }

            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight,
                contentDescription = text,
                tint = Color.Black,
            )
        }
        HorizontalDivider(
            color = if (isExpanded) MaterialTheme.colorScheme.primary else Color.Gray,
            thickness = 1.dp
        )
    }
}

@Composable
fun TreeLeaf(
    onClick: () -> Unit = {},
    text: String = "",
) {
    Column(
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 64.dp,
                    bottom = Dimens.s,
                    top = Dimens.s,
                    end = Dimens.s
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.s)
            ) {
                Text(
                    text = text,
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            }
        }
        HorizontalDivider(
            color = Color.LightGray,
            thickness = 1.dp
        )
    }
}

@Composable
fun TreeLeafAdd(
    onClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.s),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.s)
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
        HorizontalDivider(
            color = Color.LightGray,
            thickness = 1.dp
        )
    }
}

@Composable
fun Tree(
    nodes: List<Pair<String, List<String>>>,
    onClick: (() -> Unit) = {}
) {
    var expandedIndex by remember { mutableStateOf(-1) }

    Column {
        nodes.forEachIndexed { index, node ->
            val (label, children) = node
            val isExpanded = index == expandedIndex

            TreeNode(
                text = label,
                isExpanded = isExpanded,
                onClick = {
                    expandedIndex = if (isExpanded) -1 else index
                }
            )

            if (isExpanded) {
                children.forEach { childLabel ->
                    TreeLeaf(
                        text = childLabel,
                        onClick = onClick
                    )
                }
            }
        }
    }
}