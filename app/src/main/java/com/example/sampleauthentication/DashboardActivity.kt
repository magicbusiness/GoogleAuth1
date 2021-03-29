package com.example.sampleauthentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity()
{
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // variable Initialization
        val id_text = findViewById<TextView>(R.id.id_txt)
        val name_text = findViewById<TextView>(R.id.name_txt)
        val email_text = findViewById<TextView>(R.id.email_txt)
        val profile_image = findViewById<ImageView>(R.id.profile_image)
        val sign_out_button = findViewById<Button>(R.id.sign_out_btn)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        val userCurrent = auth.currentUser

        // Implement from Database
        id_text.text = userCurrent?.uid
        name_text.text = userCurrent?.displayName
        email_text.text = userCurrent?.email
        // also can add another element in firebase authentication

        Glide.with(this)
            .load(userCurrent?.photoUrl)
            .into(profile_image)

        sign_out_button.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }
}