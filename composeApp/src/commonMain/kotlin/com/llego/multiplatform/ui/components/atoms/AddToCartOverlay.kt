package com.llego.multiplatform.ui.components.atoms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.zIndex

data class AnimationData(
    val imageUrl: String,
    val startPosition: Offset,
    val endPosition: Offset
)

@Composable
fun AddToCartOverlay(
    animationTrigger: AnimationData?,
    onAnimationEnd: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var currentAnimation by remember { mutableStateOf<AnimationData?>(null) }
    var isAnimating by remember { mutableStateOf(false) }
    
    LaunchedEffect(animationTrigger) {
        animationTrigger?.let { animation ->
            currentAnimation = animation
            isAnimating = true
        }
    }
    
    Box(modifier = modifier.fillMaxSize()) {
        currentAnimation?.let { animation ->
            AddToCartAnimation(
                imageUrl = animation.imageUrl,
                startPosition = animation.startPosition,
                endPosition = animation.endPosition,
                isAnimating = isAnimating,
                onAnimationEnd = {
                    isAnimating = false
                    currentAnimation = null
                    onAnimationEnd()
                },
                modifier = Modifier.zIndex(100000f)
            )
        }
        
        content()
    }
}

@Composable
fun rememberCartPosition(): MutableState<Offset> {
    return remember { mutableStateOf(Offset.Zero) }
}

@Composable
fun Modifier.trackCartPosition(cartPosition: MutableState<Offset>): Modifier {
    return this.onGloballyPositioned { coordinates ->
        val position = coordinates.positionInRoot()
        val size = coordinates.size
        cartPosition.value = Offset(
            x = position.x + size.width / 2f,
            y = position.y + size.height / 2f
        )
    }
}