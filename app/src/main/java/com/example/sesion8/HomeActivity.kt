package com.example.sesion8

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sesion8.databinding.ActivityHomeBinding
import com.example.sesion8.model.MyUser
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

class HomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    private lateinit var auth: FirebaseAuth
    val database = Firebase.database
    var myRef = database.getReference("message")
    val USERS = "users/"
    lateinit var vel : ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        val email = intent.getStringExtra("email")
        binding.emailUser.text = email
        binding.submit.setOnClickListener {
            val name = binding.username.text.toString()
            val lastname = binding.lastname.text.toString()
            val age = binding.userAge.text.toString().toInt()
            saveUser(name, lastname,age)
            //binding.submit.isEnabled = false
        }

        readUserOne()



    }

    private fun readUserOne() {
        myRef = database.getReference(USERS)

        myRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //snapshot trae tod0 pero toda iterar cada uno de los usuarios
                for(child in snapshot.children){
                    val myUser = child.getValue<MyUser>()
                    val tv = TextView(baseContext)
                    tv.text = myUser.toString()
                    binding.listUsers.addView(tv)
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


    }

    override fun onResume() {
        super.onResume()
        subscribeToChangesusers()
    }

    override fun onPause() {
        super.onPause()
    }

    private fun subscribeToChangesusers() {
        myRef = database.getReference(USERS)
        vel = myRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(child in snapshot.children){
                    val myUser = child.getValue<MyUser>()
                    val tv = TextView(baseContext)
                    tv.text = myUser.toString()
                    binding.listUsers.addView(tv)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }


    private fun saveUser(name: String, lastname: String, age: Int) {

        var myuser = MyUser(name,lastname,age)
        /*
        val uid = auth.currentUser?.uid
        myRef = database.getReference(USERS+uid)
        myRef.setValue(myuser)

         */

        val key = myRef.push().key
        myRef = database.getReference(USERS+key)
        myRef.setValue(myuser)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.homenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val selection = item.itemId
        if(selection == R.id.signOut){
            auth.signOut()
            val i = Intent(this, MainActivity::class.java)
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(i)
        }
        return true
    }
}