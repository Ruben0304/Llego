package com.llego.multiplatform.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddLocation
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.EditLocation
import androidx.compose.material.icons.outlined.FilterList
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
import com.llego.multiplatform.ui.components.molecules.ProductCard
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
    val searchBarOffsetY = remember { Animatable(0f) }
    val backButtonSize by animateDpAsState(
        targetValue = if (state.isInSeeMoreMode) buttonHeight else 0.dp,
        animationSpec = tween(durationMillis = 400)
    )
    val filterButtonSize by animateDpAsState(
        targetValue = if (state.isInSeeMoreMode) buttonHeight else 0.dp,
        animationSpec = tween(durationMillis = 400)
    )
    
    LaunchedEffect(state.isInSeeMoreMode) {
        searchBarOffsetY.animateTo(
            targetValue = if (state.isInSeeMoreMode) 10f else 0f,
            animationSpec = tween(durationMillis = 600)
        )
    }
    
    Column(
        modifier = Modifier.padding(top = 40.dp)
    ) {
        // Fixed header section (SearchBar, Location, Slider)
        Column {
        
        // Row principal con botones y SearchBar (solo en estado inicial)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Botón atrás (aparece/desaparece animado) - solo visible en modo ver más
            if (backButtonSize > 0.dp) {
                CartButton(
                    modifier = Modifier.size(backButtonSize),
                    icon = Icons.Outlined.ArrowBack,
                    contentDescription = "back",
                    onClick = { onEvent(HomeScreenEvent.SeeMoreClicked) }
                )
            }
            
            // SearchBar solo en estado inicial
            if (!state.isInSeeMoreMode) {
                SearchBar(
                    modifier = Modifier
                        .weight(1f)
                        .height(buttonHeight),
//                    value = state.searchQuery,
                    onValueChange = { query ->
                        onEvent(HomeScreenEvent.SearchQueryChanged(query))
                    }
                )
            } else {
                // Título centrado en modo ver más
                Text(
                    text = "Productos",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
            
            // CartButton (siempre visible y fijo)
            CartButton(
                modifier = Modifier.size(buttonHeight),
                icon = Icons.Outlined.ShoppingCart,
                contentDescription = "cart",
                onClick = { onEvent(HomeScreenEvent.CartClicked) },
//                badgeCount = if (state.totalCartItems > 0) state.totalCartItems else null
            )
        }
        
        // Row separado para SearchBar + FilterButton en modo ver más (width completo)
        if (state.isInSeeMoreMode) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .offset(y = searchBarOffsetY.value.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SearchBar(
                    modifier = Modifier
                        .weight(1f)
                        .height(buttonHeight),
//                    value = state.searchQuery,
                    onValueChange = { query ->
                        onEvent(HomeScreenEvent.SearchQueryChanged(query))
                    }
                )
                
                // Botón de filtro (aparece animado)
                if (filterButtonSize > 0.dp) {
                    CartButton(
                        modifier = Modifier.size(filterButtonSize),
                        icon = Icons.Outlined.FilterList,
                        contentDescription = "filter",
                        onClick = { /* TODO: Implementar filtro */ }
                    )
                }
            }
        }

        // Spacer diferente según el modo
        if (state.isInSeeMoreMode) {
            Spacer(modifier = Modifier.height(100.dp)) // Más espacio en modo ver más
        } else {
            Spacer(modifier = Modifier.height(15.dp))
        }

        // Location section
        if (!state.isInSeeMoreMode) {
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
        }

//        Spacer(modifier = Modifier.height(13.dp))

        if (state.categories.isNotEmpty() && !state.isInSeeMoreMode) {
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
            // Espacio inicial mayor en modo ver más para que el grid quede después de la searchbar
            if (state.isInSeeMoreMode) {
                Spacer(modifier = Modifier.height(80.dp)) // Espacio adicional para el grid
                
                // Grid de productos en modo ver más
                if (state.filteredProducts.isNotEmpty()) {
                    val rows = (state.filteredProducts.size + 2) / 3 // Calcular filas necesarias
                    val gridHeight = (cardHeight + 16.dp) * rows + 32.dp // Altura dinámica
                    
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(gridHeight)
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        itemsIndexed(state.filteredProducts) { index, product ->
                            var isVisible by remember { mutableStateOf(false) }
                            
                            LaunchedEffect(key1 = index) {
                                kotlinx.coroutines.delay(index * 100L) // Retraso progresivo
                                isVisible = true
                            }
                            
                            AnimatedVisibility(
                                visible = isVisible,
                                enter = fadeIn(animationSpec = tween(300)) + 
                                       scaleIn(animationSpec = tween(300))
                            ) {
                                ProductCard(
                                    imageUrl = product.imageUrl,
                                    name = product.name,
                                    shop = product.shop,
                                    weight = product.weight,
                                    price = product.price,
                                    count = state.productCounts[product.id] ?: 0,
                                    onIncrement = { onEvent(HomeScreenEvent.IncrementProduct(product.id)) },
                                    onDecrement = { onEvent(HomeScreenEvent.DecrementProduct(product.id)) },
                                    modifier = Modifier.height(cardHeight)
                                )
                            }
                        }
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(16.dp))
            }
        
        if (!state.isInSeeMoreMode) {
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
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.stores.isNotEmpty() && !state.isInSeeMoreMode) {
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