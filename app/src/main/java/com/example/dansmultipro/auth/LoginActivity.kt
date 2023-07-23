package com.example.dansmultipro.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import bolts.Task
import com.example.dansmultipro.MainActivity
import com.example.dansmultipro.databinding.ActivityLoginBinding
import com.example.dansmultipro.model.User
import com.example.dansmultipro.sharedpreferences.SharedPreferences
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var gso: GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = SharedPreferences(this@LoginActivity)
        binding.facebook.setOnClickListener {
            var intent = Intent(this, FacebookActivity::class.java)
            startActivity(intent)
        }

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(this, gso)

        binding.google.setOnClickListener {
            signin()
        }
    }

    private fun signin() {
        var intent = gsc.signInIntent
        startActivityForResult(intent,1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            var task : com.google.android.gms.tasks.Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            var acct = GoogleSignIn.getLastSignedInAccount(this@LoginActivity)
            if (acct != null) {
                var user = User(acct.displayName.toString(), acct.email.toString())
                sharedPreferences.setPref(user)
                finish()
                var intent = Intent(this@LoginActivity, MainActivity::class.java)
                sharedPreferences.setLogin(true)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
            }
            try {
                task.getResult(ApiException::class.java)
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        }
    }
}