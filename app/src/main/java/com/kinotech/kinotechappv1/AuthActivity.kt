package com.kinotech.kinotechappv1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.tasks.Task

class AuthActivity : AppCompatActivity() {

    private lateinit var serverClientId: String
    private lateinit var signInButton: SignInButton
    private lateinit var mSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
    var currAcc: GoogleSignInAccount? = null
    private lateinit var idTokenAcc: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_screen_2)
        supportActionBar!!.hide()

        serverClientId = getString(R.string.server_client_id)
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(serverClientId)
                .requestEmail()
                .build()
        mSignInClient = GoogleSignIn.getClient(this, gso)


        signInButton = findViewById(R.id.sign_in_button)
        signInButton.setOnClickListener {
            val intent: Intent = mSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                currAcc = task.result
                idTokenAcc = currAcc?.idToken.toString()
                Log.d("TAG", "firebaseAuthWithGoogle:" + currAcc?.id)
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            } catch (e: Exception) {
                Log.w("TAG", "Google sign in failed $e")
            }
        }
    }
}
