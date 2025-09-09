package com.llego.multiplatform.ui.components.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CartButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface, CircleShape)
    ) {
        Icon(
            imageVector = Icons.Outlined.ShoppingCart,
            contentDescription = "Cart Icon",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}
