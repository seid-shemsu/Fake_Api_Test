package seid.fakeapi.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import dagger.hilt.android.AndroidEntryPoint
import seid.fakeapi.databinding.ActivityLoginBinding
import seid.fakeapi.viewmodel.AuthViewModel

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            if (binding.usernameField.text.toString().isEmpty()) {
                binding.usernameField.error = "Required Field"
                return@setOnClickListener
            }
            if (binding.passwordField.text.toString().isEmpty()) {
                binding.passwordField.error = "Required Field"
                return@setOnClickListener
            }
            val user = binding.usernameField.text.toString()
            val pass = binding.passwordField.text.toString()
            viewModel.login(user, pass)
        }

        viewModel.checkLogin()

        viewModel.uiState.observe(this) { state ->
            when (state) {
                is AuthViewModel.UiState.Loading -> {
                    binding.loginBtn.isEnabled = false
                    binding.usernameField.isEnabled = false
                    binding.passwordField.isEnabled = false
                    binding.progress.isVisible = true
                }

                is AuthViewModel.UiState.Success -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                is AuthViewModel.UiState.Error -> {
                    binding.loginBtn.isEnabled = true
                    binding.progress.isVisible = false
                    binding.usernameField.isEnabled = true
                    binding.passwordField.isEnabled = true
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
                is AuthViewModel.UiState.Idle -> {
                    binding.progress.isVisible = false
                    binding.loginBtn.isEnabled = true
                }
            }
        }
    }
}
