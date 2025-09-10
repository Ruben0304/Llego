package com.llego.multiplatform.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llego.multiplatform.data.repositories.ProductRepository
import com.llego.multiplatform.data.repositories.CategoryRepository
import com.llego.multiplatform.data.repositories.StoreRepository
import com.llego.multiplatform.data.model.Product
import com.llego.multiplatform.data.model.Store
import com.llego.multiplatform.ui.components.organisms.CategoryData
import com.llego.multiplatform.ui.state.HomeScreenState
import com.llego.multiplatform.ui.state.HomeScreenEvent
import com.llego.multiplatform.ui.state.UiState
import com.llego.multiplatform.ui.state.isError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.async

class HomeViewModel(
    private val productRepository: ProductRepository = ProductRepository(),
    private val categoryRepository: CategoryRepository = CategoryRepository(),
    private val storeRepository: StoreRepository = StoreRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())
    val state: StateFlow<HomeScreenState> = _state.asStateFlow()

    init {
        loadData()
    }

    /**
     * Handle user events
     */
    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.Refresh -> loadData()
            is HomeScreenEvent.SearchQueryChanged -> updateSearchQuery(event.query)
            is HomeScreenEvent.CategorySelected -> selectCategory(event.categoryIndex)
            is HomeScreenEvent.IncrementProduct -> incrementProduct(event.productId)
            is HomeScreenEvent.DecrementProduct -> decrementProduct(event.productId)
            is HomeScreenEvent.SeeMoreClicked -> handleSeeMoreClick()
            is HomeScreenEvent.CartClicked -> handleCartClick()
            is HomeScreenEvent.RetryClicked -> retryLoadingData()
        }
    }

    /**
     * Load initial data (products, categories, and stores) concurrently
     */
    private fun loadData() {
        viewModelScope.launch {
            // Set all states to loading immediately
            _state.update { 
                it.copy(
                    productsState = UiState.Loading,
                    categoriesState = UiState.Loading,
                    storesState = UiState.Loading
                )
            }
            
            // Load all data sets concurrently
            val productsDeferred = async { loadProductsData() }
            val categoriesDeferred = async { loadCategoriesData() }
            val storesDeferred = async { loadStoresData() }
            
            // Wait for products and update state
            try {
                val products = productsDeferred.await()
                val productCounts = products.associate { it.id to 0 }
                _state.update { 
                    it.copy(
                        productsState = UiState.Success(products),
                        productCounts = productCounts
                    )
                }
            } catch (e: Exception) {
                _state.update { 
                    it.copy(
                        productsState = UiState.Error(e, "Failed to load products")
                    )
                }
            }
            
            // Wait for categories and update state
            try {
                val categories = categoriesDeferred.await()
                _state.update { 
                    it.copy(categoriesState = UiState.Success(categories))
                }
            } catch (e: Exception) {
                _state.update { 
                    it.copy(
                        categoriesState = UiState.Error(e, "Failed to load categories")
                    )
                }
            }
            
            // Wait for stores and update state
            try {
                val stores = storesDeferred.await()
                _state.update { 
                    it.copy(storesState = UiState.Success(stores))
                }
            } catch (e: Exception) {
                _state.update { 
                    it.copy(
                        storesState = UiState.Error(e, "Failed to load stores")
                    )
                }
            }
        }
    }

    /**
     * Load products data (suspending function for concurrent loading)
     */
    private suspend fun loadProductsData(): List<Product> {
        // Simulate network delay
        delay(500)
        return productRepository.getProducts()
    }
    
    /**
     * Load categories data (suspending function for concurrent loading)
     */
    private suspend fun loadCategoriesData(): List<CategoryData> {
        // Simulate network delay
        delay(300)
        return categoryRepository.getCategories()
    }
    
    /**
     * Load stores data (suspending function for concurrent loading)
     */
    private suspend fun loadStoresData(): List<Store> {
        // Simulate network delay
        delay(400)
        return storeRepository.getStores()
    }

    /**
     * Load products from repository
     */
    private fun loadProducts() {
        viewModelScope.launch {
            _state.update { it.copy(productsState = UiState.Loading) }
            
            try {
                // Simulate network delay
                delay(500)
                
                val products = productRepository.getProducts()
                
                // Initialize product counts
                val productCounts = products.associate { it.id to 0 }
                
                _state.update { 
                    it.copy(
                        productsState = UiState.Success(products),
                        productCounts = productCounts
                    )
                }
            } catch (e: Exception) {
                _state.update { 
                    it.copy(
                        productsState = UiState.Error(e, "Failed to load products")
                    )
                }
            }
        }
    }

    /**
     * Load categories from repository
     */
    private fun loadCategories() {
        viewModelScope.launch {
            _state.update { it.copy(categoriesState = UiState.Loading) }
            
            try {
                // Simulate network delay
                delay(300)
                
                val categories = categoryRepository.getCategories()
                _state.update { 
                    it.copy(categoriesState = UiState.Success(categories))
                }
            } catch (e: Exception) {
                _state.update { 
                    it.copy(
                        categoriesState = UiState.Error(e, "Failed to load categories")
                    )
                }
            }
        }
    }

    /**
     * Load stores from repository
     */
    private fun loadStores() {
        viewModelScope.launch {
            _state.update { it.copy(storesState = UiState.Loading) }
            
            try {
                // Simulate network delay
                delay(400)
                
                val stores = storeRepository.getStores()
                _state.update { 
                    it.copy(storesState = UiState.Success(stores))
                }
            } catch (e: Exception) {
                _state.update { 
                    it.copy(
                        storesState = UiState.Error(e, "Failed to load stores")
                    )
                }
            }
        }
    }

    /**
     * Update search query
     */
    private fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    /**
     * Select a category
     */
    private fun selectCategory(categoryIndex: Int) {
        _state.update { it.copy(selectedCategoryIndex = categoryIndex) }
    }

    /**
     * Increment product count
     */
    private fun incrementProduct(productId: Int) {
        _state.update { currentState ->
            val currentCount = currentState.productCounts[productId] ?: 0
            val updatedCounts = currentState.productCounts.toMutableMap()
            updatedCounts[productId] = currentCount + 1
            
            currentState.copy(productCounts = updatedCounts)
        }
    }

    /**
     * Decrement product count
     */
    private fun decrementProduct(productId: Int) {
        _state.update { currentState ->
            val currentCount = currentState.productCounts[productId] ?: 0
            if (currentCount > 0) {
                val updatedCounts = currentState.productCounts.toMutableMap()
                updatedCounts[productId] = currentCount - 1
                currentState.copy(productCounts = updatedCounts)
            } else {
                currentState
            }
        }
    }

    /**
     * Handle "See More" button click
     */
    private fun handleSeeMoreClick() {
        // TODO: Implement navigation to full product list
        // For now, just log or show a message
        println("See More clicked - Navigate to full product list")
    }

    /**
     * Handle cart button click
     */
    private fun handleCartClick() {
        // TODO: Implement navigation to cart screen
        // For now, just log or show cart summary
        val totalItems = _state.value.totalCartItems
        println("Cart clicked - Total items: $totalItems")
    }

    /**
     * Retry loading data after error
     */
    private fun retryLoadingData() {
        val currentState = _state.value
        
        if (currentState.productsState.isError()) {
            loadProducts()
        }
        
        if (currentState.categoriesState.isError()) {
            loadCategories()
        }
        
        if (currentState.storesState.isError()) {
            loadStores()
        }
    }

    /**
     * Get product count for a specific product
     */
    fun getProductCount(productId: Int): Int {
        return _state.value.productCounts[productId] ?: 0
    }

    /**
     * Check if a product has items in cart
     */
    fun hasProductInCart(productId: Int): Boolean {
        return getProductCount(productId) > 0
    }
}