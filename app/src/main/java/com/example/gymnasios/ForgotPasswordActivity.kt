package com.example.gymnasios

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.gymnasios.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.BackToSignInForgotPassword.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnForgotPassword.setOnClickListener{
            val email = binding.etEmail.text.toString()
            if(checkEmail()){
                auth.sendPasswordResetEmail(email).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this, "Email sent!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, SignInActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }

        }
    }
    private fun checkEmail(): Boolean {
        val email = binding.etEmail.text.toString()
        if (binding.etEmail.text.toString() == "") {
            binding.textInputLayoutEmail.error = "This is required field!"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.textInputLayoutEmail.error = "Check email format"
            return false
        }
        return true
    }
}