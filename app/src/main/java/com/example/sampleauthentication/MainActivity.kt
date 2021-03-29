package com.example.sampleauthentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity()
{
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        Handler().postDelayed({
            // if the data is in the database
            if (user != null)
            {
                val intentDashboard = Intent(this, DashboardActivity::class.java)
                startActivity(intentDashboard)
                finishAffinity()
            }

            // if the data is not in the database
            else
            {
                val intentSignIn = Intent(this, SignInActivity::class.java)
                startActivity(intentSignIn)
                finishAffinity()
            }
        }, 2000)
    }
}