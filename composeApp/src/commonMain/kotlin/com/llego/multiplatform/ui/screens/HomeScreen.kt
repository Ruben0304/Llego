package com.llego.multiplatform.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddLocation
import androidx.compose.material.icons.outlined.EditLocation
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.LocationSearching
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.ShareLocation
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.llego.multiplatform.ui.components.atoms.CartButton
import com.llego.multiplatform.ui.components.molecules.SearchBar
import com.llego.multiplatform.ui.components.background.CurvedBackground
import com.llego.multiplatform.ui.components.organisms.ProductsSection
import com.llego.multiplatform.ui.components.organisms.SemicircularSlider
import com.llego.multiplatform.ui.components.organisms.StoresSection
import com.llego.multiplatform.ui.state.HomeScreenEvent
import com.llego.multiplatform.ui.state.UiState
import com.llego.multiplatform.ui.viewmodels.HomeViewModel
import com.llego.multiplatform.ui.viewmodels.createViewModelFactory

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(factory = createViewModelFactory())
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BoxWithConstraints {
        val screenWidth = maxWidth
        val screenHeight = maxHeight

        val cardWidth = screenWidth * 0.30f
        val cardHeight = screenHeight * 0.28f
        val buttonHeight = screenHeight * 0.060f

        CurvedBackground {
            when {
                state.isLoading -> {
                    LoadingContent()
                }

                state.hasError -> {
                    ErrorContent(
                        onRetry = { viewModel.onEvent(HomeScreenEvent.RetryClicked) }
                    )
                }

                else -> {
                    SuccessContent(
                        state = state,
                        onEvent = viewModel::onEvent,
                        cardWidth = cardWidth,
                        cardHeight = cardHeight,
                        buttonHeight = buttonHeight
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator()
            Text("Cargando...")
        }
    }
}

@Composable
private fun ErrorContent(
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Error al cargar los datos",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Verifica tu conexión e intenta nuevamente",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Button(onClick = onRetry) {
                Text("Reintentar")
            }
        }
    }
}

@Composable
private fun SuccessContent(
    state: com.llego.multiplatform.ui.state.HomeScreenState,
    onEvent: (HomeScreenEvent) -> Unit,
    cardWidth: androidx.compose.ui.unit.Dp,
    cardHeight: androidx.compose.ui.unit.Dp,
    buttonHeight: androidx.compose.ui.unit.Dp
) {
    Column(
        modifier = Modifier.padding(top = 40.dp)
    ) {
        // Fixed header section (SearchBar, Location, Slider)
        Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SearchBar(
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight),
//                value = state.searchQuery,
                onValueChange = { query ->
                    onEvent(HomeScreenEvent.SearchQueryChanged(query))
                }
            )
            CartButton(
                modifier = Modifier.size(buttonHeight),
                icon = Icons.Outlined.ShoppingCart,
                contentDescription = "cart",
                onClick = { onEvent(HomeScreenEvent.CartClicked) },
//                badgeCount = if (state.totalCartItems > 0) state.totalCartItems else null
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        // Location section
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = "Ubicación actual",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.surfaceVariant,
                textAlign = TextAlign.Center
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "La Habana, Cuba",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "Ubicación",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

//        Spacer(modifier = Modifier.height(13.dp))

        if (state.categories.isNotEmpty()) {
            SemicircularSlider(
                categories = state.categories,
                modifier = Modifier.fillMaxWidth(),
//                onCategorySelected = { index ->
//                    onEvent(HomeScreenEvent.CategorySelected(index))
//                }
            )
        }
        }

        // Scrollable content section (Products and Stores)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
        if (state.filteredProducts.isNotEmpty()) {
            ProductsSection(
                products = state.filteredProducts,
                productCounts = state.productCounts,
                onIncrement = { productId ->
                    onEvent(HomeScreenEvent.IncrementProduct(productId))
                },
                onDecrement = { productId ->
                    onEvent(HomeScreenEvent.DecrementProduct(productId))
                },
                cardWidth = cardWidth,
                cardHeight = cardHeight,
                onSeeMoreClick = {
                    onEvent(HomeScreenEvent.SeeMoreClicked)
                }
            )
        } else {
            // Show empty state when no products match search
            if (state.searchQuery.isNotBlank()) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se encontraron productos para \"${state.searchQuery}\"",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(32.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.stores.isNotEmpty()) {
            StoresSection(
                stores = state.stores,
                onSeeMoreClick = {
                    onEvent(HomeScreenEvent.SeeMoreClicked)
                }
            )
        }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}