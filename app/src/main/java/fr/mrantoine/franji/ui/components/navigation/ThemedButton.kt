package fr.mrantoine.franji.ui.components.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.mrantoine.franji.ui.theme.Dimens

@Composable
fun ThemedButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    isPrimary: Boolean = true
) {
    val backgroundColor = if (isPrimary) MaterialTheme.colorScheme.primary else Color.White
    val contentColor = if (isPrimary) Color.White else Color.Black

    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(Dimens.xxl),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp
        )
    }
}