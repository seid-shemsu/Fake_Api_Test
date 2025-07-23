package seid.fakeapi.model.product

import java.io.Serializable

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val image: String,
    val price: Double,
): Serializable
