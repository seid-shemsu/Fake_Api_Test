package seid.fakeapi.data.repo.product

import seid.fakeapi.model.product.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>
}
