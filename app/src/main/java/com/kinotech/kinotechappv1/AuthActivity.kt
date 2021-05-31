package com.kinotech.kinotechappv1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.HashMap

class AuthActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 9001
    }
    private lateinit var mAuth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var serverClientId: String
    private lateinit var signInButton: SignInButton
    private lateinit var mSignInClient: GoogleSignInClient
    private var currAcc: GoogleSignInAccount? = null
    private lateinit var idTokenAcc: String
    private lateinit var flipper: ViewFlipper
    private lateinit var animFlipInForward: Animation
    private lateinit var animFlipOutForward: Animation
    private lateinit var animFlipInBackward: Animation
    private lateinit var animFlipOutBackward: Animation
    private lateinit var rightDot: ImageButton
    private lateinit var leftDot: ImageButton

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
        mAuth = FirebaseAuth.getInstance()
        signInButton = findViewById(R.id.sign_in_button)
        signInButton.setOnClickListener {
            val intent: Intent = mSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }

        leftDot = findViewById(R.id.left_dot)
        leftDot.setOnClickListener {
            swipeLeft()
        }

        rightDot = findViewById(R.id.right_dot)
        rightDot.setOnClickListener {
            swipeRight()
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

    override fun onStart() {
        super.onStart()
        mAuth.currentUser
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("TAG", "Google sign in")
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                currAcc = task.result
                Log.d("cout2", "check2")
                firebaseAuthWithGoogle(currAcc?.idToken!!)
                saveUserInfo(currAcc?.displayName, currAcc?.email, currAcc?.photoUrl)
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
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("cout2", "signInWithCredential:success")
                    val user = mAuth.currentUser

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("cout2", "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun saveUserInfo(fullName: String?, email: String?, photo: Uri?) {
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("Users")
        Log.d("db", "saveUserInfo: $usersRef")
        val userMap = HashMap<String, Any?>()
        userMap["uid"] = currentUserID
        userMap["fullName"] = fullName?.toLowerCase()
        userMap["email"] = email
        userMap["photo"] = photo.toString()
        Log.d("db", "saveUserInfo: $userMap")
        usersRef.child(currentUserID).setValue(userMap)

    }
}
