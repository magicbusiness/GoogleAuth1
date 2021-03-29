package com.example.sampleauthentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignInActivity : AppCompatActivity()
{
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object
    {
        const val GOOGLE_SIGN_IN = 1
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // initialize var id
        var sign_in_button = findViewById<Button>(R.id.sign_in_btn)

        // firebase authentication
        auth = FirebaseAuth.getInstance()

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // button section
        sign_in_button.setOnClickListener {
            signIn()
        }
    }

    // Need Parameter on configuration Sign In and const val
    private fun signIn()
    {
        val googleSignIn = googleSignInClient.signInIntent
        startActivityForResult(googleSignIn, GOOGLE_SIGN_IN) // Pass data to onActivityResult
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN)
        {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception

            if (task.isSuccessful)
            {
                try {
                    val account = task.getResult(ApiException::class.java)
                    Log.d("SignInActivity", "Firebase Authentication With Google: " + account?.id)
                    firebaseAuthWithGoogle(account?.idToken)
                }

                catch (e: ApiException){
                    Log.w("SignInActivity", "Google Sign In Failed", e)
                }
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?)
    {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful)
                {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SignInActivity", "signInWithCredential:success")

                    //
                    val intentDashboard = Intent(this, DashboardActivity::class.java)
                    startActivity(intentDashboard)
                    finishAffinity()
                }

                else
                {
                    // If sign in fails, display a message to the user.
                    Log.w("SignInActivity", "signInWithCredential:failure", task.exception)
                }
            }
    }
}