package seid.fakeapi.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import seid.fakeapi.databinding.ActivityProductDetailBinding
import seid.fakeapi.model.product.Product
import seid.fakeapi.viewmodel.FavoriteViewModel

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityProductDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteViewModel by viewModels()
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.back.setOnClickListener { finish() }

        val productJson = intent.getStringExtra("product")
        val product =  Gson().fromJson(productJson, Product::class.java)

        binding.title.text = product.title
        binding.price.text = "$${product.price}"
        binding.description.text = product.description
        Glide.with(this).load(product.image).into(binding.image)

        lifecycleScope.launch {
            viewModel.isFavorite(product.id).collect {
                isFavorite = it
                binding.favButton.text = if (isFavorite) "Remove From Favorite" else "Add To Favorite"
            }
        }

        binding.favButton.setOnClickListener {
            viewModel.toggleFavorite(product, isFavorite)
        }


    }
}
