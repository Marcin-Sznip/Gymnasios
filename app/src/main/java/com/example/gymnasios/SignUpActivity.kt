package com.example.gymnasios

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.gymnasios.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var  binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.BackToSignIn.setOnClickListener{
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSignUp.setOnClickListener{
            val email =  binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if(checkAllField()){
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if(it.isSuccessful){
                        auth.signOut()
                        Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, SignInActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Log.e("error:", it.exception.toString())
                    }
                }
            }
        }
    }

    private fun checkAllField(): Boolean {
        val email = binding.etEmail.text.toString()
        if(binding.etEmail.text.toString() == ""){
            binding.textInputLayoutEmail.error = "This is required field!"
            return false
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.textInputLayoutEmail.error = "Check email format"
            return false
        }
        if(binding.etPassword.text.toString() == ""){
            binding.textInputLayoutPassword.error = "This is required field!"
            binding.textInputLayoutPassword.errorIconDrawable = null
            return false
        }
        if(binding.etPassword.length() <= 6){
            binding.textInputLayoutPassword.error = "Password should be over six letters!"
            binding.textInputLayoutPassword.errorIconDrawable = null
            return false
        }
        if(binding.etConfirmPassword.text.toString() == ""){
            binding.textInputLayoutConfirmPassword.error = "This is required field!"
            binding.textInputLayoutConfirmPassword.errorIconDrawable = null
        }
        if(binding.etPassword.text.toString() != binding.etConfirmPassword.text.toString()){
            binding.textInputLayoutPassword.error = "Password do not match"
            return false
        }
        return true
    }
}