package dev.yash.keymanager.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import app.yash.keymanager.R
import app.yash.keymanager.databinding.AuthFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.yash.keymanager.ui.viewmodels.AuthViewModel
import dev.yash.keymanager.utils.AuthConfig
import net.openid.appauth.AuthorizationService
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.auth_fragment) {
    private var _binding: AuthFragmentBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var preferences: SharedPreferences

    @Inject
    lateinit var authService: AuthorizationService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AuthFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val token = preferences.getString("ACCESS_TOKEN", null)
        if (!token.isNullOrEmpty()) {
            Navigation.findNavController(view).navigate(R.id.sshFragment)
            return
        }

        val loginButton = binding.signinButton

        val getAuthCodeFromResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    result.data?.let { authViewModel.getAccessToken(it) }
                }
            }

        loginButton.setOnClickListener {
            val authIntent = authService.getAuthorizationRequestIntent(AuthConfig.authRequest)
            getAuthCodeFromResult.launch(authIntent)
        }

        authViewModel.accessToken.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                Navigation.findNavController(view).navigate(R.id.sshFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
