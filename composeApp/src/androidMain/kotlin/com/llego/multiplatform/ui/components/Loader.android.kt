package com.llego.multiplatform.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
actual fun Loader(
    modifier: Modifier,
    size: LoaderSize
) {
    val loaderSize = when (size) {
        LoaderSize.Small -> 24.dp
        LoaderSize.Medium -> 48.dp
        LoaderSize.Large -> 72.dp
    }
    
    CircularProgressIndicator(
        modifier = modifier.size(loaderSize),
        strokeWidth = when (size) {
            LoaderSize.Small -> 2.dp
            LoaderSize.Medium -> 4.dp
            LoaderSize.Large -> 6.dp
        }
    )
}