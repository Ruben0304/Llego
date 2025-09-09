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
        LoaderSize.Small -> 28.dp
        LoaderSize.Medium -> 56.dp
        LoaderSize.Large -> 84.dp
    }
    
    CircularProgressIndicator(
        modifier = modifier.size(loaderSize),
        strokeWidth = when (size) {
            LoaderSize.Small -> 3.dp
            LoaderSize.Medium -> 5.dp
            LoaderSize.Large -> 7.dp
        }
    )
}