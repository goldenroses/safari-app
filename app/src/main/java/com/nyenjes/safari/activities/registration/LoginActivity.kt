package com.nyenjes.safari.activities.registration

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.nyenjes.safari.R
import com.nyenjes.safari.activities.MainActivity
import com.nyenjes.safari.managers.FirebaseManager
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val TAG: String = "LoginActivity"
    private lateinit var googleSignInClient: GoogleSignInClient
    private var _firebaseManager: FirebaseManager = FirebaseManager()
    private var _authListener: FirebaseAuth.AuthStateListener? = null
    private var _user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        _firebaseManager = FirebaseManager()

        setupFirebaseAuth()

        _firebaseManager._authListener = FirebaseAuth.AuthStateListener {
            val user = _firebaseManager._user
            if (user != null) {
                // email verification - disabled user.isEmailVerified()
                if (true) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(this, "Authenticated with: " + user.getEmail(), Toast.LENGTH_SHORT)
                        .show()

                    val uid: String = user.uid
                    _firebaseManager.isAdmin(uid)

                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Email is not Verified\nCheck your Inbox", Toast.LENGTH_SHORT)
                        .show()
                    _firebaseManager._firebaseAuth!!.signOut()
                }
            }
        }

        buttonSignIn.setOnClickListener {

            //check if the fields are filled out
            if (!isEmpty(editTextEmail!!.text.toString()) && !isEmpty(editTextPassword!!.text.toString())) {
                Log.d(TAG, "onClick: attempting to authenticate.")

                //check if the fields are filled out
                showDialog()

                _firebaseManager._firebaseAuth!!.signInWithEmailAndPassword(
                    editTextEmail.text.toString(),
                    editTextPassword.text.toString()
                )
                    .addOnCompleteListener { task ->
                        hideDialog()
                        if(task.isSuccessful) {
                            val uid = task.result!!.user.uid
                            val intent: Intent = Intent(this, MainActivity::class.java)
                            Toast.makeText(this, "Successfully authenticated", Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                        }
                        if(task.isCanceled) {
                            Toast.makeText(this, "Login/Username Invalid", Toast.LENGTH_SHORT).show()

                        }
                    }.addOnFailureListener {
                        fun onFailure(e: Exception) {
                            hideDialog()
                            Toast.makeText(this, "Login/Username Invalid: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "You didn't fill in all the fields.", Toast.LENGTH_SHORT).show();
            }
        }
        buttonSignInWithGoogle.setOnClickListener {
           signIn()
        }

            link_register_with_email.setOnClickListener {
                val intent: Intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }

            link_forgot_password.setOnClickListener {
                val dialog: PasswordResetDialog = PasswordResetDialog()
                dialog.show(supportFragmentManager, "PasswordResetDialog")
            }

            link_resend_verification_email.setOnClickListener {
                val dialog: ResendVerificationDialog = ResendVerificationDialog()
                dialog.show(supportFragmentManager, "ResendVerificationDialog")
            }

        }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent,
            RC_SIGN_IN
        )


    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        _firebaseManager._firebaseAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = _firebaseManager._firebaseAuth!!.currentUser

                    Log.d(TAG, "signInWithCredential:success : ${user}")
                    goToHomeScreen()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun goToHomeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun isEmpty(string: String): Boolean {
        return string.equals("");
    }


    private fun showDialog() {
        progressBar!!.setVisibility(View.VISIBLE);

    }

    private fun hideDialog() {
        if (progressBar!!.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private fun hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    private fun setupFirebaseAuth() {
        val loggin = _firebaseManager.authenticateUser()

        if(loggin == true) {
            Toast.makeText(this, "Authenticated with: " + _firebaseManager._user!!.getEmail(), Toast.LENGTH_SHORT)
                .show();

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT)
                .show()
            hideDialog()
        }
    }

    override fun onStart() {
        super.onStart()
        _firebaseManager
            ._firebaseAuth!!
            .addAuthStateListener(_firebaseManager
                ._authListener!!)
    }

    override fun onStop() {
        super.onStop()
        _firebaseManager._firebaseAuth!!.removeAuthStateListener(_firebaseManager._authListener!!)
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
}
