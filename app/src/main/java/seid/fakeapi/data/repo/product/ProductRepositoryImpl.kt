package seid.fakeapi.data.repo.product

import seid.fakeapi.data.remote.ApiService
import seid.fakeapi.model.product.Product
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ApiService
) : ProductRepository {
    override suspend fun getProducts(): List<Product> = api.getProducts()
}
