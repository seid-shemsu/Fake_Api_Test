package seid.fakeapi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import seid.fakeapi.data.repo.favorite.FavoriteRepository
import seid.fakeapi.model.favorite.Favorite
import seid.fakeapi.model.product.Product
import seid.fakeapi.viewmodel.ProductViewModel.UiEvent
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repo: FavoriteRepository
) : ViewModel() {

    private val _uiEvent = MutableLiveData<UiEvent>()
    val uiEvent: LiveData<UiEvent> = _uiEvent

    fun toggleFavorite(product: Product, isFav: Boolean) {
        viewModelScope.launch {
            if (isFav) {
                repo.removeFromFavorites(product)
            } else {
                repo.addToFavorites(product)
            }
        }
    }

    fun isFavorite(id: Int): Flow<Boolean> = repo.isFavorite(id)

    fun getAllFavorites(): Flow<List<Favorite>> = repo.getAllFavorites()

    fun navigateToDetail(product: Product) {
        _uiEvent.value = UiEvent.NavigateToDetail(product)
    }

    sealed class UiEvent {
        data class NavigateToDetail(val product: Product) : UiEvent()
    }
}
