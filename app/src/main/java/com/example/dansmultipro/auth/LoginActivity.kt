package com.example.dansmultipro.auth

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.dansmultipro.MainActivity
import com.example.dansmultipro.R
import com.example.dansmultipro.databinding.ActivityLoginBinding
import com.example.dansmultipro.model.User
import com.example.dansmultipro.sharedpreferences.SharedPreferences
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var signingClient: SignInClient
    lateinit var beginsignInRequest: BeginSignInRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = SharedPreferences(this@LoginActivity)
        binding.facebook.setOnClickListener {
            var intent = Intent(this, FacebookActivity::class.java)
            startActivity(intent)
        }

        signingClient =  Identity.getSignInClient(this)
        beginsignInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(getString(R.string.client_web_id))
                .setFilterByAuthorizedAccounts(false)
                .build())
            .build()
        var intentsend = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult(), ActivityResultCallback {
            if (it.resultCode == Activity.RESULT_OK) {
                try {
                    val credential = signingClient.getSignInCredentialFromIntent(it.data)
                    val idToken = credential.googleIdToken
                    val username = credential.id
                    if (idToken != null) {
                        var email = credential.id
                        var user = User(credential.displayName.toString(), username)
                        sharedPreferences.setPref(user)
                        var intent = Intent(this@LoginActivity, MainActivity::class.java)
                        sharedPreferences.setLogin(true)
                        startActivity(intent)
                        Toast.makeText(this@LoginActivity, "Welcome $user", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } catch (e: ApiException) {
                    e.printStackTrace()
                }
            }
        })
        binding.google.setOnClickListener {
            signingClient.beginSignIn(beginsignInRequest)
                .addOnSuccessListener {
                    try {
                        var intentSender = IntentSenderRequest.Builder(it.pendingIntent.intentSender).build()
                        intentsend.launch(intentSender)
//                        startIntentSenderForResult(it.pendingIntent.intentSender, 2,null, 0, 0, 0, null)
                    } catch (e: IntentSender.SendIntentException) {
                        Log.d("TAG", "onCreate: $e")
                    }
                }
                .addOnFailureListener {
                    Log.d("TAG", "onCreate: ${it.localizedMessage}")
                }
        }
    }

}