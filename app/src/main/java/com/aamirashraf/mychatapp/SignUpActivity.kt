package com.aamirashraf.mychatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SignUpActivity : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etName:EditText
    private lateinit var btnSignUp: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var mdbRef:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()
        etEmail=findViewById(R.id.etEmail)
        etPassword=findViewById(R.id.etPassword)
        etName=findViewById(R.id.etName)
        btnSignUp=findViewById(R.id.btnSignUp)
        FirebaseApp.initializeApp(this)
        auth= FirebaseAuth.getInstance()
        btnSignUp.setOnClickListener {
            val email=etEmail.text.toString()
            val password=etPassword.text.toString()
            val name=etName.text.toString()
            signUp(name,email,password)
        }
    }

    private fun signUp(name:String,email:String,password:String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign up success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    addToDataBase(name,email,user?.uid!!)
//                    val user = auth.currentUser
                    val intent=Intent(this@SignUpActivity,MainActivity::class.java)

                    startActivity(intent)
                    finish()
                    // Launch main activity or navigate to next screen
                } else {
                    // Sign up fails, display a message to the user.
                    Toast.makeText(this@SignUpActivity, "Authentication failed.", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun addToDataBase(name: String, email: String, uid: String?) {
        mdbRef=FirebaseDatabase.getInstance().reference
        if (uid != null) {
            mdbRef.child("user").child(uid).setValue(User(name,email,uid))
        }
    }
}