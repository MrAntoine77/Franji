package fr.mrantoine.franji.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



fun multiplyStrokeWidths(json: String, multiplier: Float): String {
    // FONCTION SENSIBLE, nécessite un JSON avec une épaisseur de trait = a 3
    val target = "\"k\":3"
    val replacement = "\"k\":${3f * multiplier}"
    return json.replace(target, replacement)
}
@Composable
fun Lottie(
    data: String,
    autoPlay: Boolean = false,
    replayable: Boolean = true,
    color: Color = Color.Black,
    speed: Float = 1f,
    lines: Int = 0,
    lineMultiplier: Float = 1f

) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        ClickToPlayLottie(
            lottieJson = multiplyStrokeWidths(data, lineMultiplier),
            autoPlay = autoPlay,
            color = color,
            speed = speed,
            lines = lines,
            replayable = replayable
        )
    }
}

@Composable
fun ClickToPlayLottie(
    lottieJson: String,
    autoPlay: Boolean,
    color: Color,
    speed: Float,
    lines: Int = 0,
    replayable: Boolean
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.JsonString(lottieJson)
    )

    var progress by remember { mutableStateOf(if (autoPlay) 0f else 1f) }
    var animationJob by remember { mutableStateOf<Job?>(null) }
    val scope = rememberCoroutineScope()

    val maxProgress = remember(composition) {
        val frames = (lines * 32).toFloat()
        if (composition != null && lines > 0) {
            val totalFrames = composition!!.durationFrames
            (frames / totalFrames).coerceIn(0f, 1f)
        } else 1f
    }

    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                color.hashCode(),
                BlendModeCompat.SRC_ATOP
            ),
            keyPath = arrayOf("**")
        )
    )

    LaunchedEffect(composition, autoPlay) {
        if (composition != null && autoPlay) {
            animationJob?.cancel()
            animationJob = scope.launch {
                progress = 0f
                val duration = composition!!.duration.toLong()
                val start = System.currentTimeMillis()

                while (true) {
                    val elapsed = System.currentTimeMillis() - start
                    val raw = (elapsed.toFloat() / duration) * speed
                    progress = raw.coerceIn(0f, maxProgress)
                    if (raw >= maxProgress) break
                    delay(16)
                }
                progress = maxProgress
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                if(replayable) {
                    animationJob?.cancel()
                    animationJob = scope.launch {
                        progress = 0f
                        val duration = (composition?.duration ?: 1000).toLong()
                        val start = System.currentTimeMillis()

                        while (true) {
                            val elapsed = System.currentTimeMillis() - start
                            val raw = (elapsed.toFloat() / duration) * speed
                            progress = raw.coerceIn(0f, maxProgress)
                            if (raw >= maxProgress) break
                            delay(16)
                        }
                        progress = maxProgress
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.fillMaxSize(),
            dynamicProperties = dynamicProperties
        )
    }
}