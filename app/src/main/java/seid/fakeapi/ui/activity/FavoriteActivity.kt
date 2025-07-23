package seid.fakeapi.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import seid.fakeapi.databinding.ActivityFavoritesBinding
import seid.fakeapi.ui.adapter.ProductAdapter
import seid.fakeapi.viewmodel.FavoriteViewModel
import seid.fakeapi.viewmodel.ProductViewModel

@AndroidEntryPoint
class FavoriteActivity : AppCompatActivity() {

    private var _binding: ActivityFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.back.setOnClickListener { finish() }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ProductAdapter(
            onItemClick = { product ->
                viewModel.navigateToDetail(product)
            },
            onFavoriteClick = { id ->
                viewModel.toggleFavorite(id, true)
            }
        )

        binding.recyclerView.adapter = adapter
        lifecycleScope.launch {
            viewModel.getAllFavorites().collect { list ->
                if (list.isEmpty()) {
                    binding.empty.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                } else {
                    binding.empty.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    adapter.submitList(list.map { it.toProduct() })
                }
            }
        }

        productViewModel.favorites.observe(this) {
            adapter.updateFavorites(it.toMutableList())
        }
        productViewModel.loadFavorites()

        viewModel.uiEvent.observe(this) {
            when (it) {
                is FavoriteViewModel.UiEvent.NavigateToDetail -> {
                    val intent = Intent(this, DetailActivity::class.java).apply {
                        putExtra("product", Gson().toJson(it.product))
                    }
                    startActivity(intent)
                }

            }
        }
    }
}
