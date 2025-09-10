package com.llego.multiplatform.ui.components.atoms

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.interaction.MutableInteractionSource
import com.llego.multiplatform.ui.theme.shapes.CounterControlsShape
import com.llego.multiplatform.ui.components.atoms.PillButton

@Composable
fun CounterControls(
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Colores animados
    val backgroundColor by animateColorAsState(
        targetValue = if (count > 0) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = tween(300),
        label = "background_color"
    )
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = CounterControlsShape()
            ),

        contentAlignment = Alignment.Center
    ) {
        // Transición animada entre estados
        AnimatedContent(
            targetState = count == 0,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) + scaleIn(animationSpec = tween(300)) togetherWith
                fadeOut(animationSpec = tween(300)) + scaleOut(animationSpec = tween(300))
            },
            label = "counter_content"
        ) { isInitialState ->
            if (isInitialState) {
                // Estado inicial: ícono "+" sin círculo
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = MutableInteractionSource()
                        ) { onIncrement() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            } else {
                // Estado con contador y botones
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    PillButton("–", onDecrement)
                    AnimatedContent(
                        targetState = count,
                        transitionSpec = {
                            slideInVertically(
                                initialOffsetY = { if (targetState > initialState) -it else it },
                                animationSpec = tween(300)
                            ) + fadeIn(animationSpec = tween(300)) togetherWith
                            slideOutVertically(
                                targetOffsetY = { if (targetState > initialState) it else -it },
                                animationSpec = tween(300)
                            ) + fadeOut(animationSpec = tween(300))
                        },
                        label = "counter_text"
                    ) { animatedCount ->
                        Text(
                            text = animatedCount.toString(),
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    PillButton("+", onIncrement)
                }
            }
        }
    }
}