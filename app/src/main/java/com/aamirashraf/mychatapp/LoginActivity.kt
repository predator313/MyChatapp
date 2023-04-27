package com.aamirashraf.mychatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var etEmail:EditText
    private lateinit var etPassword:EditText
    private lateinit var btnLogin:Button
    private lateinit var btnSignUp:Button
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        auth= FirebaseAuth.getInstance()
        etEmail=findViewById(R.id.etEmail)
        etPassword=findViewById(R.id.etPassword)
        btnLogin=findViewById(R.id.btnLogin)
        btnSignUp=findViewById(R.id.btnSignUp)
        btnSignUp.setOnClickListener {
            val intent=Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
        btnLogin.setOnClickListener {
            val email=etEmail.text.toString()
            val password=etPassword.text.toString()
            login(email,password)
        }
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
//                    val user = auth.currentUser
                    val intent=Intent(this@LoginActivity,MainActivity::class.java)

                    startActivity(intent)
                    finish()
                    // Launch main activity or navigate to next screen
                } else {
                    // Sign in fails, display a message to the user.
                    Toast.makeText(this@LoginActivity, "User doesn't exist.", Toast.LENGTH_LONG).show()
                }
            }
    }
}