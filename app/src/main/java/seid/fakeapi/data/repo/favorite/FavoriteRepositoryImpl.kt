package seid.fakeapi.data.repo.favorite

import kotlinx.coroutines.flow.Flow
import seid.fakeapi.data.local.FavoriteDao
import seid.fakeapi.model.favorite.Favorite
import seid.fakeapi.model.product.Product
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val dao: FavoriteDao
): FavoriteRepository {
    override suspend fun addToFavorites(product: Product) {
        dao.insertFavorite(
            Favorite(
                id = product.id,
                title = product.title,
                price = product.price,
                image = product.image,
                description = product.description,
                category = product.category
            )
        )
    }

    override suspend fun removeFromFavorites(product: Product) {
        dao.deleteFavorite(
            Favorite(
                id = product.id,
                title = product.title,
                price = product.price,
                image = product.image,
                description = product.description,
                category = product.category
            )
        )
    }

    override fun isFavorite(id: Int): Flow<Boolean> = dao.isFavorite(id)


    override fun getAllFavorites(): Flow<List<Favorite>> = dao.getAllFavorites()
}