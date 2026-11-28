package fr.mrantoine.franji.ui.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min

object ThemeColor {
    val Brown = Color(0xFF59382C)          // texte + contour sombre
    val Pink = Color(0xFFFBA9AF)           // accents (lignes, bordures, fleur)
    val Cream = Color(0xFFFFF0E9)          // fond des boutons

    val DisabledCream = Color(0xFFEADFD9)
    val DisabledPink = Color(0xFFD6BDB5)
    val DisabledBrown = Color(0xFFAC9690)
}



@Composable
fun SpeechBubble(
    modifier: Modifier = Modifier,
    tailWidth: Dp = 18.dp,
    borderWidth: Dp = 2.dp,

    content: @Composable () -> Unit
) {
    class SpeechBubbleShape(
        private val tailWidth: Dp = 18.dp,
        private val tailHeight: Dp = 26.dp,
        private val cornerRadius: Dp = 40.dp,
        private val tailPosition: Float = 0.58f
    ) : Shape {

        override fun createOutline(
            size: Size,
            layoutDirection: LayoutDirection,
            density: Density
        ): Outline {
            val tw = with(density) { tailWidth.toPx() }
            val th = with(density) { tailHeight.toPx() }
            val r = min(with(density) { cornerRadius.toPx() }, size.height / 2f)

            val body = Path().apply {
                addRoundRect(
                    RoundRect(
                        left = tw,
                        top = 0f,
                        right = size.width,
                        bottom = size.height,
                        cornerRadius = CornerRadius(r)
                    )
                )
            }

            val tipY = size.height * tailPosition
            val baseX = tw + r * 0.3f

            val tail = Path().apply {
                moveTo(baseX, tipY - th / 2f)
                quadraticTo(tw * 0.5f, tipY - th * 0.35f, 0f, tipY)
                quadraticTo(tw * 0.6f, tipY + th * 0.1f, baseX, tipY + th / 2f)
                close()
            }

            return Outline.Generic(Path.combine(PathOperation.Union, body, tail))
        }
    }

    val shape = SpeechBubbleShape(tailWidth = tailWidth)
    val contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)

    Box(
        modifier = modifier
            .background(ThemeColor.Cream, shape)
            .border(borderWidth, ThemeColor.Brown, shape)
            .padding(
                start = tailWidth + contentPadding.calculateLeftPadding(LayoutDirection.Ltr),
                end = contentPadding.calculateRightPadding(LayoutDirection.Ltr),
                top = contentPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding()
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun FlowerDivider(
    modifier: Modifier = Modifier,
    flowerSize: Dp = 16.dp,
    lineThickness: Dp = 2.dp,
    lineColor: Color = ThemeColor.Pink,
    spacing: Dp = 8.dp
) {
    val context = LocalContext.current
    val flowerBitmap = remember {
        context.assets.open("conversations/conversation_flower.png").use {
            BitmapFactory.decodeStream(it).asImageBitmap()
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(lineThickness)
                .background(lineColor, CircleShape)
        )

        Image(
            bitmap = flowerBitmap,
            contentDescription = "Flower divider",
            modifier = Modifier.size(flowerSize),
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(lineColor)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(lineThickness)
                .background(lineColor, CircleShape)
        )
    }
}


@Composable
fun ChoiceButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    disabled: Boolean = false,
    fontSize: TextUnit = 22.sp
) {
    val shape = RoundedCornerShape(100.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(if (disabled) ThemeColor.DisabledPink else ThemeColor.Pink, shape)
            .padding(3.dp)
            .background(if (disabled) ThemeColor.DisabledCream else ThemeColor.Cream, shape)
            .clip(shape)
            .clickable(
                enabled = !disabled,
                role = Role.Button,
                onClick = onClick
            )
            .padding(PaddingValues(horizontal = 16.dp, vertical = 2.dp)),
        contentAlignment = Alignment.Center
    ) {
        HighlightedText(
            text = text,
            color = if (disabled) ThemeColor.DisabledBrown else ThemeColor.Brown,
            fontSize = fontSize,
            textAlign = TextAlign.Center,
            lineHeight = fontSize * 1.4f
        )
    }
}

@Composable
fun DisplayTextBox(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 22.sp,
) {
    val shape = RoundedCornerShape(100.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(ThemeColor.Brown, shape)
            .padding(4.dp)
            .background(ThemeColor.Pink, shape)
            .padding(4.dp)
            .background(ThemeColor.Cream, shape)
            .padding(PaddingValues(horizontal = 16.dp, vertical = 2.dp)),
        contentAlignment = Alignment.Center
    ) {
        HighlightedText(
            text = text,
            color = ThemeColor.Brown,
            fontSize = fontSize,
            textAlign = TextAlign.Center,
            lineHeight = fontSize * 1.4f
        )
    }
}


@Composable
fun InfoBox(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ThemeColor.Cream.copy(alpha = 0.70f),
    borderColor: Color = ThemeColor.Pink.copy(alpha = 0.75f),
    textColor: Color = ThemeColor.Brown,
    fontSize: TextUnit = 16.sp,
    cornerRadius: Dp = 20.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
) {
    val shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(backgroundColor)
            .border(2.dp, borderColor, shape)
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        HighlightedText(
            text = text,
            color = textColor,
            fontSize = fontSize,
            textAlign = TextAlign.Center,
            lineHeight = fontSize * 1.4f
        )
    }
}
