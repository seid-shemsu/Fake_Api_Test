package seid.fakeapi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import seid.fakeapi.data.repo.favorite.FavoriteRepository
import seid.fakeapi.data.repo.product.ProductRepository
import seid.fakeapi.model.product.Product
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    private val _uiEvent = MutableLiveData<UiEvent>()
    val uiEvent: LiveData<UiEvent> = _uiEvent

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _favorites = MutableLiveData<MutableSet<Int>>(mutableSetOf())
    val favorites: LiveData<MutableSet<Int>> = _favorites

    fun loadProducts() {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            _products.value = repository.getProducts()
            try {
                _products.value = repository.getProducts()
                products.value?.let {
                    _uiState.value = UiState.Success(it)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to load products: ${e.message}")
            }
        }
    }

    fun toggleFavorite(product: Product) {
        val current = _favorites.value ?: mutableSetOf()
        if (current.contains(product.id)) {
            current.remove(product.id)
            viewModelScope.launch {
                favoriteRepository.removeFromFavorites(product)
            }
        } else {
            current.add(product.id)
            viewModelScope.launch {
                favoriteRepository.addToFavorites(product)
            }
        }
        _favorites.value = current

    }

    fun loadFavorites() {
        viewModelScope.launch {
            favoriteRepository.getAllFavorites().collectLatest {
                _favorites.value = it.map { it.id }.toMutableSet()
            }
        }

    }

    fun navigateToDetail(product: Product) {
        _uiEvent.value = UiEvent.NavigateToDetail(product)
    }

    fun navigateToFavorites() {
        _uiEvent.value = UiEvent.NavigateToFavorites
    }

    fun navigateToLogin() {
        _uiEvent.value = UiEvent.NavigateToLogin
    }

    sealed class UiState {
        object Loading : UiState()
        data class Success(val data: List<Product>) : UiState()
        data class Error(val message: String) : UiState()
    }

    sealed class UiEvent {
        data class NavigateToDetail(val product: Product) : UiEvent()
        object NavigateToFavorites : UiEvent()
        object NavigateToLogin: UiEvent()
    }
}
