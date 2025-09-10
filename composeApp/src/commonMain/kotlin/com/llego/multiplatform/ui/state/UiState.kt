package com.llego.multiplatform.ui.state

/**
 * Represents the UI state for data loading operations
 */
sealed interface UiState<out T> {
    /**
     * Loading state - data is being fetched
     */
    data object Loading : UiState<Nothing>
    
    /**
     * Success state - data has been successfully loaded
     */
    data class Success<T>(val data: T) : UiState<T>
    
    /**
     * Error state - an error occurred while loading data
     */
    data class Error(
        val exception: Throwable,
        val message: String = exception.message ?: "Unknown error"
    ) : UiState<Nothing>
}

/**
 * Extension functions for UiState
 */
fun <T> UiState<T>.isLoading(): Boolean = this is UiState.Loading
fun <T> UiState<T>.isSuccess(): Boolean = this is UiState.Success
fun <T> UiState<T>.isError(): Boolean = this is UiState.Error

fun <T> UiState<T>.getDataOrNull(): T? = when (this) {
    is UiState.Success -> data
    else -> null
}

fun <T> UiState<T>.getErrorOrNull(): Throwable? = when (this) {
    is UiState.Error -> exception
    else -> null
}