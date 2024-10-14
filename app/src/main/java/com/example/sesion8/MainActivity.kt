package com.example.sesion8

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sesion8.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout.TabGravity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    val TAG = "FIREBASE_APP"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()


        binding.login.setOnClickListener {

            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            if(validateForm(email, password)){
                //API FIREBASE
                loginUser(email,password)

            }
        }
    }

    override fun onStart() {
        super.onStart()
        updateUI(auth.currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser!= null){
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("email", currentUser.email)
            startActivity(intent)
        }

    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
            if(it.isSuccessful){
                updateUI(auth.currentUser) //nunca nulo si la autenticación es exitosa
            }else{
                val message = it.exception?.message
                Toast.makeText(baseContext, message, Toast.LENGTH_SHORT)
                Log.e(TAG, "Sign in failed : $message")
                binding.email.text.clear()
                binding.password.text.clear()
            }
        }
    }


    private fun validateForm(email : String, password: String) : Boolean {
        var valid = false
        if (email.isEmpty()) {
            binding.email.setError("Required!")
        } else if (!validEmailAddress(email)) {
            binding.email.setError("Invalid email address")
        } else if (password.isEmpty()) {
            binding.password.setError("Required!")
        } else if (password.length < 6){
            binding.password.setError("Password should be at least 6 characters long!")
        }else {
            valid = true
        }
        return valid
    }

    private fun validEmailAddress(email:String):Boolean{
        val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
        return email.matches(regex.toRegex())
    }
}