package com.kinotech.kinotechappv1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
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
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale
import kotlin.collections.HashMap

class AuthActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var mAuth: FirebaseAuth
    private lateinit var serverClientId: String
    private lateinit var signInButton: SignInButton
    private lateinit var mSignInClient: GoogleSignInClient
    private var currAcc: GoogleSignInAccount? = null
    private lateinit var idTokenAcc: String
    private lateinit var flipper: ViewFlipper
    private lateinit var newGestureDetector: NewGestureDetector
    private lateinit var gestureDetector: GestureDetector
    private lateinit var rightDot: ImageButton
    private lateinit var leftDot: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_screens)

        supportActionBar!!.hide()

        flipper = findViewById(R.id.flipper)
        newGestureDetector = NewGestureDetector(flipper, this)
        gestureDetector = GestureDetector(baseContext, newGestureDetector)
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
            newGestureDetector.swipeLeft()
        }

        rightDot = findViewById(R.id.right_dot)
        rightDot.setOnClickListener {
            newGestureDetector.swipeRight()
        }
    }

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
                Log.d("count2", "check2")
                firebaseAuthWithGoogle(currAcc?.idToken!!)
                checkUserInDb(currAcc!!)
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

    private fun checkUserInDb(currAcc: GoogleSignInAccount) {
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("Users")
        usersRef.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.child(currentUserID).exists()) {
                        saveUserInfo(currAcc.displayName, currAcc.email, currAcc.photoUrl)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("dbError", "$error")
                }
            }
        )
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("count2", "signInWithCredential:success")
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("count2", "signInWithCredential:failure", task.exception)
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
        userMap["fullName"] = fullName?.toLowerCase(Locale.getDefault())
        userMap["email"] = email
        userMap["photo"] = photo.toString()
        Log.d("db", "saveUserInfo: $userMap")
        usersRef.child(currentUserID).setValue(userMap)
    }
}
