package com.kinotech.kinotechappv1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.tasks.Task

class AuthActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var serverClientId: String
    private lateinit var signInButton: SignInButton
    private lateinit var mSignInClient: GoogleSignInClient
    var currAcc: GoogleSignInAccount? = null
    private lateinit var idTokenAcc: String
    private lateinit var flipper: ViewFlipper
    private lateinit var animFlipInForward: Animation
    private lateinit var animFlipOutForward: Animation
    private lateinit var animFlipInBackward: Animation
    private lateinit var animFlipOutBackward: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_screens)

        supportActionBar!!.hide()

        flipper = findViewById(R.id.flipper)

        animFlipInForward = AnimationUtils.loadAnimation(this, R.anim.flipin)
        animFlipOutForward = AnimationUtils.loadAnimation(this, R.anim.flipout)
        animFlipInBackward = AnimationUtils.loadAnimation(this, R.anim.flipin_reverse)
        animFlipOutBackward = AnimationUtils.loadAnimation(this, R.anim.flipout_reverse)

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
            startActivityForResult(intent, Companion.RC_SIGN_IN)
        }
    }

    private fun isFirst(): Boolean {
        return flipper.displayedChild == 0
    }

    private fun isLast(): Boolean {
        return flipper.displayedChild + 1 == flipper.childCount
    }

    private fun swipeLeft() {
        if (!isLast()) {
            flipper.inAnimation = animFlipInBackward
            flipper.outAnimation = animFlipOutBackward
            flipper.showPrevious()
        }
    }

    private fun swipeRight() {
        if (!isFirst()) {
            flipper.inAnimation = animFlipInForward
            flipper.outAnimation = animFlipOutForward
            flipper.showNext()
        }
    }

    private inner class NewGestureDetector : GestureDetector.SimpleOnGestureListener() {

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val sensitivity = 50
            if ((e1.x - e2.x) > sensitivity) {
                swipeLeft()
            } else if ((e2.x - e1.x) > sensitivity) {
                swipeRight()
            }
            return true
        }
    }

    private val newGestureDetector: NewGestureDetector = NewGestureDetector()

    private val gestureDetector = GestureDetector(baseContext, newGestureDetector)

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
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
