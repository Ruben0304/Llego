package com.llego.multiplatform.ui.components.atoms

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlin.math.roundToInt

@Composable
fun AddToCartAnimation(
    imageUrl: String,
    startPosition: Offset,
    endPosition: Offset,
    isAnimating: Boolean,
    onAnimationEnd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    
    val animationDurationMs = 1500
    
    var animationStarted by remember { mutableStateOf(false) }
    
    LaunchedEffect(isAnimating) {
        if (isAnimating && !animationStarted) {
            animationStarted = true
        } else if (!isAnimating) {
            animationStarted = false
        }
    }
    
    val offsetAnimation by animateOffsetAsState(
        targetValue = if (animationStarted) endPosition else startPosition,
        animationSpec = tween(
            durationMillis = animationDurationMs,
            easing = FastOutSlowInEasing
        ),
        finishedListener = { 
            if (animationStarted) {
                onAnimationEnd()
                animationStarted = false
            }
        },
        label = "position_animation"
    )
    
    val scaleAnimation by animateFloatAsState(
        targetValue = if (animationStarted) 0.4f else 1f,
        animationSpec = tween(
            durationMillis = animationDurationMs,
            easing = FastOutSlowInEasing
        ),
        label = "scale_animation"
    )
    
    if (isAnimating) {
        Box(
            modifier = modifier
                .offset {
                    IntOffset(
                        x = (offsetAnimation.x - with(density) { 24.dp.toPx() }).roundToInt(),
                        y = (offsetAnimation.y - with(density) { 24.dp.toPx() }).roundToInt()
                    )
                }
                .size(48.dp)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
                    .graphicsLayer(
                        scaleX = scaleAnimation,
                        scaleY = scaleAnimation
                    ),
                contentScale = ContentScale.Crop
            )
        }
    }
}