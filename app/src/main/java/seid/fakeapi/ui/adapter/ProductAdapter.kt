package seid.fakeapi.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import seid.fakeapi.R
import seid.fakeapi.databinding.ItemProductBinding
import seid.fakeapi.model.product.Product

class ProductAdapter(
    val onItemClick: (Product) -> Unit,
    val onFavoriteClick: (Product) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(DIFF) {

    private val favList = mutableListOf<Int>()

    class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.title.text = item.title
        holder.binding.price.text = "$${item.price}"
        holder.binding.root.setOnClickListener { onItemClick(item) }
        holder.binding.fav.setOnClickListener { onFavoriteClick(item) }
        holder.binding.fav.setImageResource(if (favList.contains(item.id)) R.drawable.fav else R.drawable.non_fav)
        Glide.with(holder.binding.root.context)
            .load(item.image)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
            .into(holder.binding.image)
    }

    fun updateFavorites(list: MutableList<Int>) {
        favList.clear()
        favList.addAll(list)
        notifyDataSetChanged()
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem
        }
    }
}
