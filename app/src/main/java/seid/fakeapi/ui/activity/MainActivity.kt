package seid.fakeapi.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import seid.fakeapi.databinding.ActivityMainBinding
import seid.fakeapi.ui.adapter.ProductAdapter
import seid.fakeapi.viewmodel.AuthViewModel
import seid.fakeapi.viewmodel.ProductViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ProductAdapter(
            onItemClick = { product ->
                viewModel.navigateToDetail(product)
            },
            onFavoriteClick = { id ->
                viewModel.toggleFavorite(id)
            }
        )

        binding.recyclerView.adapter = adapter

        viewModel.products.observe(this) {
            adapter.submitList(it)
        }
        viewModel.favorites.observe(this) {
            adapter.updateFavorites(it.toMutableList())
        }

        viewModel.loadProducts()
        viewModel.loadFavorites()

        binding.favorites.setOnClickListener {
            viewModel.navigateToFavorites()
        }
        binding.logout.setOnClickListener {
            authViewModel.logout()
            viewModel.navigateToLogin()
        }

        viewModel.uiState.observe(this) { state ->
            when (state) {
                is ProductViewModel.UiState.Loading -> {
                    binding.progress.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                    binding.error.visibility = View.GONE
                }

                is ProductViewModel.UiState.Success -> {
                    binding.progress.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.error.visibility = View.GONE
                    adapter.submitList(state.data)
                    binding.favorites.isVisible = state.data.isNotEmpty()
                }

                is ProductViewModel.UiState.Error -> {
                    binding.progress.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                    binding.error.visibility = View.VISIBLE
                    binding.error.text = state.message
                }
            }
        }

        viewModel.uiEvent.observe(this) {
            when (it) {
                is ProductViewModel.UiEvent.NavigateToDetail -> {
                    val intent = Intent(this, DetailActivity::class.java).apply {
                        putExtra("product", Gson().toJson(it.product))
                    }
                    startActivity(intent)
                }

                ProductViewModel.UiEvent.NavigateToFavorites -> {
                    val intent = Intent(this, FavoriteActivity::class.java)
                    startActivity(intent)
                }

                ProductViewModel.UiEvent.NavigateToLogin -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}
