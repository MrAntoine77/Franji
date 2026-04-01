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

@Composable
fun Lottie(
    autoPlay: Boolean = false,
    color: Color = Color.Black,
    speed: Float = 2f
) {
    val lottieJson = """{"v":"5.5.7","fr":64,"ip":0,"op":320,"w":109,"h":109,"ddd":0,"layers":[{"ddd":0,"ind":1,"ty":4,"ks":{"o":{"a":0,"k":100,"ix":11},"r":{"a":0,"k":0,"ix":10},"p":{"a":0,"k":[0,0,0],"ix":2},"a":{"a":0,"k":[0,0,0],"ix":1},"s":{"a":0,"k":[100,100,100],"ix":6}},"shapes":[{"ty":"gr","it":[{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[0.0,-1.37],[0.07,-10.19],[0.0,-0.13]],"o":[[0.89,0.89],[0.0,1.11],[-0.02,2.32],[0,0]],"v":[[16.75,25.22],[18.07,28.88],[18.07,66.51],[18.05,70.42]]},"ix":2}},{"ty":"tm","s":{"a":1,"k":[{"i":{"x":[0.667],"y":[1]},"o":{"x":[0.333],"y":[0]},"t":0,"s":[0]},{"t":32,"s":[100]}],"ix":1},"e":{"a":0,"k":0,"ix":2},"o":{"a":0,"k":0,"ix":3},"m":1,"ix":2},{"ty":"st","c":{"a":0,"k":[0,0,0,1],"ix":3},"o":{"a":0,"k":100,"ix":4},"w":{"a":0,"k":3,"ix":5},"lc":2,"lj":2,"ml":4,"bm":0},{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[-3.12,0.25],[-8.74,0.77],[-2.12,-0.24]],"o":[[2.5,1.19],[11.55,-0.91],[2.64,-0.23],[0,0]],"v":[[32.63,34.69],[40.08,36.04],[67.12,32.86],[75.51,32.94]]},"ix":2}},{"ty":"tr","p":{"a":0,"k":[0,0],"ix":2},"a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"r":{"a":0,"k":0,"ix":6},"o":{"a":0,"k":100,"ix":7},"sk":{"a":0,"k":0,"ix":4},"sa":{"a":0,"k":0,"ix":5}}],"np":4,"cix":2,"bm":0,"ix":1},{"ty":"gr","it":[{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[-1.68,0.16],[0.14,-1.02],[0.07,-9.26],[0.0,-0.18]],"o":[[5.66,-0.81],[1.76,-0.17],[-0.23,1.7],[-0.01,1.9],[0,0]],"v":[[19.29,27.75],[40.51,25.0],[43.26,27.91],[42.74,64.51],[42.72,67.8]]},"ix":2}},{"ty":"tm","s":{"a":1,"k":[{"i":{"x":[0.667],"y":[1]},"o":{"x":[0.333],"y":[0]},"t":32,"s":[0]},{"t":64,"s":[100]}],"ix":1},"e":{"a":0,"k":0,"ix":2},"o":{"a":0,"k":0,"ix":3},"m":1,"ix":2},{"ty":"st","c":{"a":0,"k":[0,0,0,1],"ix":3},"o":{"a":0,"k":100,"ix":4},"w":{"a":0,"k":3,"ix":5},"lc":2,"lj":2,"ml":4,"bm":0},{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[-3.12,0.25],[-8.74,0.77],[-2.12,-0.24]],"o":[[2.5,1.19],[11.55,-0.91],[2.64,-0.23],[0,0]],"v":[[32.63,34.69],[40.08,36.04],[67.12,32.86],[75.51,32.94]]},"ix":2}},{"ty":"tr","p":{"a":0,"k":[0,0],"ix":2},"a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"r":{"a":0,"k":0,"ix":6},"o":{"a":0,"k":100,"ix":7},"sk":{"a":0,"k":0,"ix":4},"sa":{"a":0,"k":0,"ix":5}}],"np":4,"cix":2,"bm":0,"ix":1},{"ty":"gr","it":[{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[-4.77,0.36]],"o":[[4.34,-0.47],[0,0]],"v":[[19.16,46.97],[41.77,44.76]]},"ix":2}},{"ty":"tm","s":{"a":1,"k":[{"i":{"x":[0.667],"y":[1]},"o":{"x":[0.333],"y":[0]},"t":64,"s":[0]},{"t":96,"s":[100]}],"ix":1},"e":{"a":0,"k":0,"ix":2},"o":{"a":0,"k":0,"ix":3},"m":1,"ix":2},{"ty":"st","c":{"a":0,"k":[0,0,0,1],"ix":3},"o":{"a":0,"k":100,"ix":4},"w":{"a":0,"k":3,"ix":5},"lc":2,"lj":2,"ml":4,"bm":0},{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[-3.12,0.25],[-8.74,0.77],[-2.12,-0.24]],"o":[[2.5,1.19],[11.55,-0.91],[2.64,-0.23],[0,0]],"v":[[32.63,34.69],[40.08,36.04],[67.12,32.86],[75.51,32.94]]},"ix":2}},{"ty":"tr","p":{"a":0,"k":[0,0],"ix":2},"a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"r":{"a":0,"k":0,"ix":6},"o":{"a":0,"k":100,"ix":7},"sk":{"a":0,"k":0,"ix":4},"sa":{"a":0,"k":0,"ix":5}}],"np":4,"cix":2,"bm":0,"ix":1},{"ty":"gr","it":[{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[-8.12,0.52]],"o":[[8.5,-0.97],[0,0]],"v":[[19.0,66.59],[41.49,64.48]]},"ix":2}},{"ty":"tm","s":{"a":1,"k":[{"i":{"x":[0.667],"y":[1]},"o":{"x":[0.333],"y":[0]},"t":96,"s":[0]},{"t":128,"s":[100]}],"ix":1},"e":{"a":0,"k":0,"ix":2},"o":{"a":0,"k":0,"ix":3},"m":1,"ix":2},{"ty":"st","c":{"a":0,"k":[0,0,0,1],"ix":3},"o":{"a":0,"k":100,"ix":4},"w":{"a":0,"k":3,"ix":5},"lc":2,"lj":2,"ml":4,"bm":0},{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[-3.12,0.25],[-8.74,0.77],[-2.12,-0.24]],"o":[[2.5,1.19],[11.55,-0.91],[2.64,-0.23],[0,0]],"v":[[32.63,34.69],[40.08,36.04],[67.12,32.86],[75.51,32.94]]},"ix":2}},{"ty":"tr","p":{"a":0,"k":[0,0],"ix":2},"a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"r":{"a":0,"k":0,"ix":6},"o":{"a":0,"k":100,"ix":7},"sk":{"a":0,"k":0,"ix":4},"sa":{"a":0,"k":0,"ix":5}}],"np":4,"cix":2,"bm":0,"ix":1},{"ty":"gr","it":[{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[-0.02,-1.02],[13.0,-11.0]],"o":[[0.95,1.25],[1.09,49.25],[0,0]],"v":[[57.55,16.0],[58.66,19.5],[41.25,95.25]]},"ix":2}},{"ty":"tm","s":{"a":1,"k":[{"i":{"x":[0.667],"y":[1]},"o":{"x":[0.333],"y":[0]},"t":128,"s":[0]},{"t":160,"s":[100]}],"ix":1},"e":{"a":0,"k":0,"ix":2},"o":{"a":0,"k":0,"ix":3},"m":1,"ix":2},{"ty":"st","c":{"a":0,"k":[0,0,0,1],"ix":3},"o":{"a":0,"k":100,"ix":4},"w":{"a":0,"k":3,"ix":5},"lc":2,"lj":2,"ml":4,"bm":0},{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[-3.12,0.25],[-8.74,0.77],[-2.12,-0.24]],"o":[[2.5,1.19],[11.55,-0.91],[2.64,-0.23],[0,0]],"v":[[32.63,34.69],[40.08,36.04],[67.12,32.86],[75.51,32.94]]},"ix":2}},{"ty":"tr","p":{"a":0,"k":[0,0],"ix":2},"a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"r":{"a":0,"k":0,"ix":6},"o":{"a":0,"k":100,"ix":7},"sk":{"a":0,"k":0,"ix":4},"sa":{"a":0,"k":0,"ix":5}}],"np":4,"cix":2,"bm":0,"ix":1},{"ty":"gr","it":[{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[-1.31,0.22],[0.0,-3.88],[0.0,-20.25],[1.08,1.0]],"o":[[6.56,-1.04],[2.97,-0.5],[0.0,1.49],[0.0,11.88],[0,0]],"v":[[59.5,17.98],[82.57,14.25],[87.78,18.75],[87.97,89.25],[79.67,91.75]]},"ix":2}},{"ty":"tm","s":{"a":1,"k":[{"i":{"x":[0.667],"y":[1]},"o":{"x":[0.333],"y":[0]},"t":160,"s":[0]},{"t":192,"s":[100]}],"ix":1},"e":{"a":0,"k":0,"ix":2},"o":{"a":0,"k":0,"ix":3},"m":1,"ix":2},{"ty":"st","c":{"a":0,"k":[0,0,0,1],"ix":3},"o":{"a":0,"k":100,"ix":4},"w":{"a":0,"k":3,"ix":5},"lc":2,"lj":2,"ml":4,"bm":0},{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[-3.12,0.25],[-8.74,0.77],[-2.12,-0.24]],"o":[[2.5,1.19],[11.55,-0.91],[2.64,-0.23],[0,0]],"v":[[32.63,34.69],[40.08,36.04],[67.12,32.86],[75.51,32.94]]},"ix":2}},{"ty":"tr","p":{"a":0,"k":[0,0],"ix":2},"a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"r":{"a":0,"k":0,"ix":6},"o":{"a":0,"k":100,"ix":7},"sk":{"a":0,"k":0,"ix":4},"sa":{"a":0,"k":0,"ix":5}}],"np":4,"cix":2,"bm":0,"ix":1},{"ty":"gr","it":[{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[-7.22,0.5]],"o":[[7.85,-1.0],[0,0]],"v":[[59.77,39.0],[86.72,36.25]]},"ix":2}},{"ty":"tm","s":{"a":1,"k":[{"i":{"x":[0.667],"y":[1]},"o":{"x":[0.333],"y":[0]},"t":192,"s":[0]},{"t":224,"s":[100]}],"ix":1},"e":{"a":0,"k":0,"ix":2},"o":{"a":0,"k":0,"ix":3},"m":1,"ix":2},{"ty":"st","c":{"a":0,"k":[0,0,0,1],"ix":3},"o":{"a":0,"k":100,"ix":4},"w":{"a":0,"k":3,"ix":5},"lc":2,"lj":2,"ml":4,"bm":0},{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[-3.12,0.25],[-8.74,0.77],[-2.12,-0.24]],"o":[[2.5,1.19],[11.55,-0.91],[2.64,-0.23],[0,0]],"v":[[32.63,34.69],[40.08,36.04],[67.12,32.86],[75.51,32.94]]},"ix":2}},{"ty":"tr","p":{"a":0,"k":[0,0],"ix":2},"a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"r":{"a":0,"k":0,"ix":6},"o":{"a":0,"k":100,"ix":7},"sk":{"a":0,"k":0,"ix":4},"sa":{"a":0,"k":0,"ix":5}}],"np":4,"cix":2,"bm":0,"ix":1},{"ty":"gr","it":[{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[-7.36,0.5]],"o":[[6.62,-0.62],[0,0]],"v":[[59.25,57.75],[86.61,55.75]]},"ix":2}},{"ty":"tm","s":{"a":1,"k":[{"i":{"x":[0.667],"y":[1]},"o":{"x":[0.333],"y":[0]},"t":224,"s":[0]},{"t":256,"s":[100]}],"ix":1},"e":{"a":0,"k":0,"ix":2},"o":{"a":0,"k":0,"ix":3},"m":1,"ix":2},{"ty":"st","c":{"a":0,"k":[0,0,0,1],"ix":3},"o":{"a":0,"k":100,"ix":4},"w":{"a":0,"k":3,"ix":5},"lc":2,"lj":2,"ml":4,"bm":0},{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[-3.12,0.25],[-8.74,0.77],[-2.12,-0.24]],"o":[[2.5,1.19],[11.55,-0.91],[2.64,-0.23],[0,0]],"v":[[32.63,34.69],[40.08,36.04],[67.12,32.86],[75.51,32.94]]},"ix":2}},{"ty":"tr","p":{"a":0,"k":[0,0],"ix":2},"a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"r":{"a":0,"k":0,"ix":6},"o":{"a":0,"k":100,"ix":7},"sk":{"a":0,"k":0,"ix":4},"sa":{"a":0,"k":0,"ix":5}}],"np":4,"cix":2,"bm":0,"ix":1}],"ip":0,"op":320,"st":0,"bm":0}]}""".trimIndent()


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        ClickToPlayLottie(
            lottieJson = lottieJson,
            autoPlay = autoPlay,
            color = color,
            speed = speed)
    }
}

@Composable
fun ClickToPlayLottie(
    lottieJson: String,
    autoPlay: Boolean,
    color: Color,
    speed: Float
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.JsonString(lottieJson)
    )

    var progress by remember { mutableStateOf(if (autoPlay) 0f else 1f) }
    var animationJob by remember { mutableStateOf<Job?>(null) }
    val scope = rememberCoroutineScope()

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

    // AutoPlay when first displayed
    LaunchedEffect(composition, autoPlay) {
        if (composition != null && autoPlay) {
            animationJob?.cancel()
            animationJob = scope.launch {
                progress = 0f
                val duration = (composition!!.duration).toLong()
                val start = System.currentTimeMillis()

                while (true) {
                    val elapsed = System.currentTimeMillis() - start
                    val raw = (elapsed.toFloat() / duration) * speed
                    progress = raw.coerceIn(0f, 1f)
                    if (raw >= 1f) break
                    delay(16)
                }
                progress = 1f
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
                animationJob?.cancel()
                animationJob = scope.launch {
                    progress = 0f
                    val duration = (composition?.duration ?: 1000).toLong()
                    val start = System.currentTimeMillis()

                    while (true) {
                        val elapsed = System.currentTimeMillis() - start
                        val raw = (elapsed.toFloat() / duration) * speed
                        progress = raw.coerceIn(0f, 1f)
                        if (raw >= 1f) break
                        delay(16)
                    }
                    progress = 1f
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