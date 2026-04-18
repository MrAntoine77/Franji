package fr.mrantoine.franji.ui.components.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import fr.mrantoine.franji.storage.UserProgress
import kotlin.String
import kotlin.collections.Map

@Composable
fun TreeNode(
    text: String,
    isNode: Boolean = true,
    isSelected: Boolean = false,
    level: Int = 0,
    add: String = ""
) {
    val isExpanded = isNode and isSelected
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.s)
        ) {
            if((level == 0) and isNode) {
                Icon(
                    imageVector = if (isSelected) Icons.Outlined.Star else Icons.TwoTone.Star,
                    contentDescription = text,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(Dimens.l)
                )
            }
            else {
                for(i in 0..level) {
                    Box(modifier = Modifier.size(Dimens.l))
                }
            }
            val text = if(isNode) text else "$text - $add%"
            Text(
                text = "$text",
                color = if (isExpanded) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.secondary,
                fontWeight = if (isExpanded) FontWeight.SemiBold else FontWeight.Normal,
                fontSize = 20.sp
            )
        }
        if (isNode) {
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = text,
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Composable
fun TreeNodeAdd(
    onClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.s),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = "Add",
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}
@Composable
fun Tree(
    treeData: Map<String, Any>,
    level: Int = 0,
    onClick: (arg: String) -> Unit = {},
    onAdd: () -> Unit = {},
    path: String = "" ,
    progressMap: Map<String, UserProgress> = emptyMap()
) {
    treeData.forEach { (key, value) ->

        val isNode = value is Map<*, *>
        var isSelected by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .clickable(
                    onClick = {
                        if(key != "+")
                        {
                            if(!isNode) {
                                onClick("$path/$key")
                            } else {
                                isSelected = !isSelected
                            }
                        }
                    }
                )
            ) {

            if(!isNode and (key == "+")) {
                TreeNodeAdd(onClick = onAdd)
            }
            else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.s),
                    modifier = Modifier
                        .padding(Dimens.s)
                ) {
                    val progress = if(progressMap.containsKey("$path/$key")) (progressMap["$path/$key"]!!.progress*100).toInt().toString() else ""
                    TreeNode(
                        text = key,
                        isNode = isNode,
                        isSelected = isSelected,
                        level = level,
                        add = progress
                    )

                }
            }
        }

        HorizontalDivider(
            color = when {
                (isNode and isSelected) -> MaterialTheme.colorScheme.primary
                (isNode and !isSelected) -> MaterialTheme.colorScheme.secondary
                else -> MaterialTheme.colorScheme.tertiary
            },
            thickness = 1.dp
        )

        if(isNode && isSelected)
        {
            Tree(
                treeData = value as Map<String, Any>,
                level = level + 1,
                onClick = onClick,
                path = if(level == 0) key else "$path/$key",
                progressMap = progressMap
            )
        }
    }
}


