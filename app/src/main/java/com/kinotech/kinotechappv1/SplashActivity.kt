package com.kinotech.kinotechappv1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH = 300
    var gsa: GoogleSignInAccount? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gsa = GoogleSignIn.getLastSignedInAccount(this)

        if (gsa != null) {
            Handler().postDelayed({
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }, SPLASH_DISPLAY_LENGTH.toLong())
        } else {
            Handler().postDelayed({
                startActivity(Intent(this@SplashActivity, AuthActivity::class.java))
                finish()
            }, SPLASH_DISPLAY_LENGTH.toLong())
        }

    }
}