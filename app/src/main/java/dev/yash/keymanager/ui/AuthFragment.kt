package dev.yash.keymanager.ui

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import app.yash.keymanager.R
import app.yash.keymanager.databinding.AuthFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.yash.keymanager.utils.AuthConfig
import net.openid.appauth.AuthorizationService

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.auth_fragment) {
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var binding: AuthFragmentBinding
    private lateinit var authService: AuthorizationService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AuthFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginButton = binding.signinButton
        authService = AuthorizationService(requireContext())

        val getAuthCodeFromResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    result.data?.let {
                        authViewModel.getAccessToken(
                            authService,
                            it,
                            requireContext(),
                        )
                    }
                }
            }

        loginButton.setOnClickListener {
            val authIntent = authService.getAuthorizationRequestIntent(AuthConfig.authRequest)
            getAuthCodeFromResult.launch(authIntent)
        }
    }
}
