package com.llego.multiplatform.ui.components.molecules
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "Search for 'Grocery'",
    isCartBouncing: Boolean = false,
    onValueChange: (String) -> Unit = {}
) {
    var text by remember { mutableStateOf("") }
    
    // Animación para dar espacio al bounce del carrito
    val bounceOffset by animateDpAsState(
        targetValue = if (isCartBouncing) 20.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        )
    )
    
    // Asegurar que el offset nunca sea negativo
    val safeBounceOffset = maxOf(bounceOffset, 0.dp)

    TextField(
        value = text,
        onValueChange = {
            text = it
            onValueChange(it)
        },
        modifier = modifier
            .padding(end = safeBounceOffset)
            .clip(RoundedCornerShape(24.dp)),
        placeholder = {
            Text(
                text = hint,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true
    )
}

