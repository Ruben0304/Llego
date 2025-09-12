package com.llego.multiplatform.ui.components.atoms

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

@Composable
fun CartButton(
    icon: ImageVector,
    contentDescription: String = "",
    modifier: Modifier = Modifier,
    badgeCount: Int? = null,
    triggerBounce: Boolean = false,
    onBounceEnd: () -> Unit = {},
    onClick: () -> Unit
) {
    var shouldBounce by remember { mutableStateOf(false) }
    var showBadge by remember { mutableStateOf(false) }
    var pendingBadgeCount by remember { mutableStateOf<Int?>(null) }
    
    // Inicializar el badge solo si no está bouncing
    LaunchedEffect(Unit) {
        if (badgeCount != null && badgeCount > 0) {
            showBadge = true
        }
    }
    
    LaunchedEffect(triggerBounce) {
        if (triggerBounce) {
            shouldBounce = true
            showBadge = false // Ocultar badge inmediatamente
            pendingBadgeCount = badgeCount // Guardar el valor para después del bounce
        }
    }
    
    // Manejar cambios de badgeCount solo cuando NO hay bounce activo
    LaunchedEffect(badgeCount) {
        if (!shouldBounce && pendingBadgeCount == null) {
            showBadge = badgeCount != null && badgeCount > 0
        }
    }
    
    val bounceScale by animateFloatAsState(
        targetValue = if (shouldBounce) 1.4f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        finishedListener = {
            if (shouldBounce) {
                shouldBounce = false
                // Mostrar badge solo después del bounce si hay items
                showBadge = pendingBadgeCount != null && pendingBadgeCount!! > 0
                pendingBadgeCount = null
                onBounceEnd()
            }
        }
    )
    
    Box(
        modifier = modifier
            .zIndex(2001f)
            .scale(bounceScale),
        contentAlignment = Alignment.TopEnd
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface, CircleShape).zIndex(2000f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        // Badge contador - solo aparece cuando showBadge es true
        if (showBadge && badgeCount != null && badgeCount > 0) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        MaterialTheme.colorScheme.secondary,
                        CircleShape
                    )
                    .offset(x = 4.dp, y = (-4).dp).zIndex(2010f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (badgeCount > 99) "99+" else badgeCount.toString(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center).fillMaxWidth().padding(end = 8.dp),
                )
            }
        }
    }
}
