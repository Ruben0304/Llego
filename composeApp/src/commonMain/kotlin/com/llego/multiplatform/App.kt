package com.llego.multiplatform

import androidx.compose.runtime.*
import com.llego.multiplatform.ui.screens.HomeScreen
import com.llego.multiplatform.ui.theme.LlegoTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    LlegoTheme {
        HomeScreen()
    }
}
//llegobackend-production.up.railway.app