# ViewModel Implementation Guide for Kotlin Multiplatform

This guide demonstrates how to implement ViewModels in Kotlin Multiplatform (KMP) following the official Android documentation recommendations.

## Overview

ViewModels in KMP serve as a bridge between shared business logic and UI components, providing:
- Cross-platform state management
- Lifecycle-aware data handling
- Unidirectional data flow
- Separation of concerns

## Dependencies

The following dependencies are required in your `build.gradle.kts`:

```kotlin
// In libs.versions.toml
androidx-lifecycle = "2.9.3"

androidx-lifecycle-viewmodel = { module = "org.jetbrains.androidx.lifecycle:lifecycle-viewmodel", version.ref = "androidx-lifecycle" }
androidx-lifecycle-viewmodelCompose = { module = "org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose", version.ref = "androidx-lifecycle" }
androidx-lifecycle-runtimeCompose = { module = "org.jetbrains.androidx.lifecycle:lifecycle-runtime-compose", version.ref = "androidx-lifecycle" }

// In commonMain dependencies
api(libs.androidx.lifecycle.viewmodel)
implementation(libs.androidx.lifecycle.viewmodelCompose)
implementation(libs.androidx.lifecycle.runtimeCompose)
```

## Architecture Components

### 1. UI State Management

```kotlin
// UiState.kt - Generic state wrapper
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(
        val exception: Throwable? = null,
        val message: String = "An error occurred"
    ) : UiState<Nothing>
}

// Extension functions for convenience
fun <T> UiState<T>.isLoading(): Boolean = this is UiState.Loading
fun <T> UiState<T>.isSuccess(): Boolean = this is UiState.Success
fun <T> UiState<T>.isError(): Boolean = this is UiState.Error
fun <T> UiState<T>.getDataOrNull(): T? = when (this) {
    is UiState.Success -> data
    else -> null
}
```

### 2. Screen-Specific State

```kotlin
// HomeScreenState.kt - Screen-specific state
data class HomeScreenState(
    val productsState: UiState<List<Product>> = UiState.Loading,
    val categoriesState: UiState<List<CategoryData>> = UiState.Loading,
    val searchQuery: String = "",
    val selectedCategoryIndex: Int = 0,
    val productCounts: Map<Int, Int> = emptyMap(),
    val isRefreshing: Boolean = false
) {
    val selectedCategory: CategoryData?
        get() = categoriesState.getDataOrNull()?.getOrNull(selectedCategoryIndex)
    
    val totalCartItems: Int
        get() = productCounts.values.sum()
}

// Events for user interactions
sealed interface HomeScreenEvent {
    data class SearchQueryChanged(val query: String) : HomeScreenEvent
    data class CategorySelected(val index: Int) : HomeScreenEvent
    data class ProductCountIncremented(val productId: Int) : HomeScreenEvent
    data class ProductCountDecremented(val productId: Int) : HomeScreenEvent
    data object RefreshRequested : HomeScreenEvent
    data object SeeMoreClicked : HomeScreenEvent
    data object CartClicked : HomeScreenEvent
}
```

### 3. ViewModel Implementation

```kotlin
// HomeViewModel.kt - Main ViewModel
class HomeViewModel(
    private val productRepository: ProductRepository = ProductRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenState())
    val uiState: StateFlow<HomeScreenState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.SearchQueryChanged -> updateSearchQuery(event.query)
            is HomeScreenEvent.CategorySelected -> selectCategory(event.index)
            is HomeScreenEvent.ProductCountIncremented -> incrementProductCount(event.productId)
            is HomeScreenEvent.ProductCountDecremented -> decrementProductCount(event.productId)
            is HomeScreenEvent.RefreshRequested -> refreshData()
            is HomeScreenEvent.SeeMoreClicked -> handleSeeMoreClick()
            is HomeScreenEvent.CartClicked -> handleCartClick()
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    productsState = UiState.Loading,
                    categoriesState = UiState.Loading
                )

                // Load data (add network delay simulation)
                delay(1000)
                
                val categories = getCategories()
                val products = productRepository.getProducts()
                val productCounts = products.associate { it.id to 0 }
                
                _uiState.value = _uiState.value.copy(
                    categoriesState = UiState.Success(categories),
                    productsState = UiState.Success(products),
                    productCounts = productCounts
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    productsState = UiState.Error(e, "Failed to load products"),
                    categoriesState = UiState.Error(e, "Failed to load categories")
                )
            }
        }
    }

    // Other methods...
}
```

### 4. ViewModel Factory

```kotlin
// ViewModelFactory.kt - Factory for dependency injection
object ViewModelFactory {
    val homeViewModelFactory = viewModelFactory {
        initializer {
            HomeViewModel(
                productRepository = ProductRepository()
            )
        }
    }
}

fun createHomeViewModel(): HomeViewModel {
    return ViewModelFactory.homeViewModelFactory.create(HomeViewModel::class)
}
```

### 5. Compose Integration

```kotlin
// HomeScreen.kt - Compose UI integration
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(factory = ViewModelFactory.homeViewModelFactory)
) {
    // Lifecycle-aware state collection
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // Handle different UI states
    when {
        uiState.productsState is UiState.Loading -> LoadingContent()
        uiState.productsState is UiState.Error -> ErrorContent(
            message = uiState.productsState.message,
            onRetry = { viewModel.onEvent(HomeScreenEvent.RefreshRequested) }
        )
        uiState.productsState is UiState.Success -> SuccessContent(
            uiState = uiState,
            onEvent = viewModel::onEvent
        )
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Text("Loading products...")
        }
    }
}

@Composable
private fun SuccessContent(
    uiState: HomeScreenState,
    onEvent: (HomeScreenEvent) -> Unit
) {
    val products = uiState.productsState.getDataOrNull() ?: emptyList()
    
    // UI implementation with event handling
    ProductsSection(
        products = products,
        productCounts = uiState.productCounts,
        onIncrement = { productId ->
            onEvent(HomeScreenEvent.ProductCountIncremented(productId))
        },
        onDecrement = { productId ->
            onEvent(HomeScreenEvent.ProductCountDecremented(productId))
        }
    )
}
```

## Best Practices

### 1. State Management
- Use `StateFlow` for exposing state from ViewModels
- Implement proper loading, success, and error states
- Keep state immutable and use data classes
- Use `collectAsStateWithLifecycle()` in Compose for lifecycle-aware collection

### 2. Event Handling
- Implement unidirectional data flow with events
- Use sealed interfaces for type-safe event definitions
- Handle all user interactions through events

### 3. Error Handling
- Wrap data operations in try-catch blocks
- Provide meaningful error messages
- Implement retry mechanisms for failed operations

### 4. Performance
- Use `viewModelScope` for coroutines
- Avoid unnecessary state updates
- Implement proper cancellation for long-running operations

### 5. Testing
- Test ViewModels independently of UI
- Mock repositories and dependencies
- Test different state transitions

## Platform-Specific Considerations

### iOS Integration
For iOS, you might need additional setup:

```kotlin
// Expected/actual pattern for iOS-specific features
expect class ViewModelStoreOwner

// iOS-specific implementation would go in iosMain
```

### Desktop Support
For desktop platforms, ensure proper coroutine context:

```kotlin
// Add to jvmMain dependencies
implementation(libs.kotlinx.coroutinesSwing)
```

## Migration Guide

To migrate existing screens to use ViewModels:

1. **Extract State**: Move all mutable state from Composables to ViewModel
2. **Define Events**: Create sealed interface for user interactions
3. **Implement UiState**: Wrap data in UiState for proper loading/error handling
4. **Update UI**: Use `collectAsStateWithLifecycle()` and event handlers
5. **Test**: Ensure all functionality works correctly

## Common Patterns

### Loading States
```kotlin
// In ViewModel
_uiState.value = _uiState.value.copy(isLoading = true)

// In Compose
if (uiState.isLoading) {
    CircularProgressIndicator()
}
```

### Error Handling
```kotlin
// In ViewModel
catch (e: Exception) {
    _uiState.value = _uiState.value.copy(
        error = UiState.Error(e, "Network error occurred")
    )
}

// In Compose
if (uiState.error is UiState.Error) {
    ErrorMessage(uiState.error.message)
}
```

### Data Refresh
```kotlin
// In ViewModel
fun refresh() {
    viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isRefreshing = true)
        try {
            val newData = repository.fetchData()
            _uiState.value = _uiState.value.copy(
                data = UiState.Success(newData),
                isRefreshing = false
            )
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                error = UiState.Error(e),
                isRefreshing = false
            )
        }
    }
}
```

This implementation follows the official Android KMP documentation and provides a solid foundation for scalable, maintainable ViewModels in your Kotlin Multiplatform project.