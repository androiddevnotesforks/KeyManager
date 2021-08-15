package dev.yash.gisthub

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import app.yash.gisthub.databinding.ActivityMainBinding
import dev.yash.gisthub.util.AuthConfig
import dev.yash.gisthub.util.Secrets
import dev.yash.gisthub.util.SharedPrefs
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientSecretBasic

class MainActivity : AppCompatActivity() {
    private lateinit var authService: AuthorizationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginButton = binding.signinButton
        authService = AuthorizationService(this)

        loginButton.setOnClickListener {
            val authIntent = authService.getAuthorizationRequestIntent(AuthConfig.authRequest)
            getAuthCodeFromResult.launch(authIntent)
        }
    }

    private val getAuthCodeFromResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let { getAccessToken(it) }
            } else {
                Toast.makeText(this, "Error Retrieving Auth Token", Toast.LENGTH_LONG).show()
            }
        }

    private fun getAccessToken(authIntent: Intent) {
        val resp = AuthorizationResponse.fromIntent(authIntent)
        val clientAuth = ClientSecretBasic(Secrets.CLIENT_SECRET)

        if (resp != null) authService.performTokenRequest(
            resp.createTokenExchangeRequest(),
            clientAuth,
        ) { response, exception ->
            if (response != null) {
                response.accessToken?.let {
                    val prefs = SharedPrefs.getEncryptedSharedPreferences(this)
                    prefs.edit().putString("ACCESS_TOKEN", it).apply()
                }
            } else {
                Log.e("Error", exception.toString())
            }
        } else Toast.makeText(this, "Error Retrieving Access Token", Toast.LENGTH_LONG).show()
    }
}
