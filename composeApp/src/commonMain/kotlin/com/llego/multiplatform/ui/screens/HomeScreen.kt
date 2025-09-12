package com.llego.multiplatform.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowBackIos
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.llego.multiplatform.ui.components.atoms.CartButton
import com.llego.multiplatform.ui.components.atoms.AddToCartOverlay
import com.llego.multiplatform.ui.components.atoms.AnimationData
import com.llego.multiplatform.ui.components.atoms.rememberCartPosition
import com.llego.multiplatform.ui.components.atoms.trackCartPosition
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

        CurvedBackground(
            homeState = state
        ) {
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
    
    // Animation state
    var animationTrigger by remember { mutableStateOf<AnimationData?>(null) }
    val cartPosition = rememberCartPosition()
    var triggerCartBounce by remember { mutableStateOf(false) }
    var isCartBouncing by remember { mutableStateOf(false) }
    
    LaunchedEffect(state.isInSeeMoreMode) {
        if (state.isInSeeMoreMode) {
            // Cuando cambia a see more, empezar desde la posición inicial (negativa)
            searchBarOffsetY.snapTo(-120f)
            searchBarOffsetY.animateTo(
                targetValue = 80f,
                animationSpec = tween(durationMillis = 800)
            )
        } else {
            // Cuando vuelve al estado inicial, resetear
            searchBarOffsetY.snapTo(0f)
        }
    }
    
    // Coordinar animación de bounce entre CartButton y SearchBar
    LaunchedEffect(triggerCartBounce) {
        if (triggerCartBounce) {
            isCartBouncing = true
        }
    }
    
    AddToCartOverlay(
        animationTrigger = animationTrigger,
        onAnimationEnd = { 
            animationTrigger = null
            triggerCartBounce = true
        }
    ) {
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
                    icon = Icons.AutoMirrored.Outlined.ArrowBackIos,
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
                    isCartBouncing = isCartBouncing,
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
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
            
            // CartButton (siempre visible y fijo)
            CartButton(
                modifier = Modifier
                    .size(buttonHeight)
                    .trackCartPosition(cartPosition)
                    .zIndex(200000f),
                icon = Icons.Outlined.ShoppingCart,
                contentDescription = "cart",
                badgeCount = state.totalCartItems.takeIf { it > 0 },
                triggerBounce = triggerCartBounce,
                onBounceEnd = { 
                    triggerCartBounce = false 
                    isCartBouncing = false 
                },
                onClick = { onEvent(HomeScreenEvent.CartClicked) }
            )
        }
        
        // LazyRow de categorías - solo visible en modo ver más
        AnimatedVisibility(
            visible = state.isInSeeMoreMode && state.categories.isNotEmpty(),
            enter = slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(durationMillis = 600)
            ) + fadeIn(animationSpec = tween(durationMillis = 600)),
            exit = slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(durationMillis = 400)
            )
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(top = 25.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                itemsIndexed(state.categories) { index, category ->
                    val isSelected = state.selectedCategoryIndex == index
                    Text(
                        text = category.text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isSelected) 
                            MaterialTheme.colorScheme.onSecondaryContainer
                        else 
                            MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.6f),
                        modifier = Modifier
                            .clickable {
                                onEvent(HomeScreenEvent.CategorySelected(index))
                            }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
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
                    isCartBouncing = isCartBouncing,
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
//                Spacer(modifier = Modifier.height(30.dp)) // Espacio adicional para el grid
                
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
                                    onAddToCartAnimation = { imageUrl, startPosition ->
                                        animationTrigger = AnimationData(
                                            imageUrl = imageUrl,
                                            startPosition = startPosition,
                                            endPosition = cartPosition.value
                                        )
                                    },
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
                    },
                    onAddToCartAnimation = { imageUrl, startPosition ->
                        animationTrigger = AnimationData(
                            imageUrl = imageUrl,
                            startPosition = startPosition,
                            endPosition = cartPosition.value
                        )
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
}