package com.llego.multiplatform.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun Loader(
    modifier: Modifier = Modifier,
    size: LoaderSize = LoaderSize.Medium
)

enum class LoaderSize {
    Small,
    Medium,
    Large
}