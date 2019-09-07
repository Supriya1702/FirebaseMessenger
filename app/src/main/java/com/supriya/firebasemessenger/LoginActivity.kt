package com.supriya.firebasemessenger

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity :AppCompatActivity(){

    val  auth by lazy{
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_btn.setOnClickListener {

            val email = email_edt.text.toString()
            val password = password_edt.text.toString()


            if (email_edt.text.isNullOrEmpty())
                Toast.makeText(applicationContext, "Cannot be Empty", Toast.LENGTH_LONG).show()
            if (password_edt.text.isNullOrEmpty())
                Toast.makeText(this, "cannot be empty", Toast.LENGTH_LONG).show()
            else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (!it.isSuccessful)
                            Log.d("MAINACTIVITY", "NOT WORKING")
                        if (it.isSuccessful)
                            Log.d("MAINACTIVITY", "Successfully created user with uid ${it.result?.user?.uid}")
                    }
                    .addOnSuccessListener {
                        Toast.makeText(this, "Account logged in successfully", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                    }
            }
        }





        back_to_register_tv.setOnClickListener {
            val intent=Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

    }

}