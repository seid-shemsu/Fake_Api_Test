package seid.fakeapi.data.repo.favorite

import kotlinx.coroutines.flow.Flow
import seid.fakeapi.model.favorite.Favorite
import seid.fakeapi.model.product.Product

interface FavoriteRepository {
    suspend fun addToFavorites(product: Product)

    suspend fun removeFromFavorites(product: Product)

    fun isFavorite(id: Int): Flow<Boolean>

    fun getAllFavorites(): Flow<List<Favorite>>
}
