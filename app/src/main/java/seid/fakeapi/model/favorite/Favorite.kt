package seid.fakeapi.model.favorite

import androidx.room.Entity
import androidx.room.PrimaryKey
import seid.fakeapi.model.product.Product
import java.io.Serializable

@Entity(tableName = "favorites")
data class Favorite(
    @PrimaryKey val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String
): Serializable {
    fun toProduct(): Product {
        return Product(
            id = id,
            title = title,
            price = price,
            description = description,
            category = category,
            image = image)
    }
}

