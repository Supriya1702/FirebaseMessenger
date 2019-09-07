package com.supriya.firebasemessenger

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {
    val auth by lazy {
        FirebaseAuth.getInstance()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        register_btn.setOnClickListener {
            register()
        }

        already_have_account_tv.setOnClickListener {
            login()
        }

        selectphoto_button_register.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

    }

    private fun register() {
        val email = email_edt.text.toString()
        val password = password_edt.text.toString()


        Log.d("MAINACTIVITY", "email :" + email)
        Log.d("MAINACTIVITY", "passsword: $password")


        if (email_edt.text.isNullOrEmpty()) {

            email_edt.error = "Cannot be Empty"
        } else if (password_edt.text.isNullOrEmpty()) {

            password_edt.error = "Cannot be Empty"
        } else {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful)
                        Log.d("MAINACTIVITY", "NOT WORKING")
                    if (it.isSuccessful)
                        Log.d("MAINACTIVITY", "Successfully created user with uid ${it.result?.user?.uid}")

                }
                .addOnSuccessListener {
                    Toast.makeText(this, "Account created successfully", Toast.LENGTH_LONG).show()

                    uploadImageToFirebaseStorage()

                }
                .addOnFailureListener {
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                }

        }

    }

    private fun uploadImageToFirebaseStorage() {
        if(selectedPhotoUrl==null)return

        val filename=UUID.randomUUID().toString()
        val ref=FirebaseStorage.getInstance().getReference("/images/$filename")


        ref.putFile(selectedPhotoUrl!!)
            .addOnSuccessListener {
                Log.d("Register", "Successfully uploaded image:${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("ABD","djif")
                    saveUserToFireBaseDataBase(it.toString())
                }


            }
            .addOnFailureListener {

            }





//        ref.downloadUrl.addOnSuccessListener {
//            Log.d("Register Activity","File Location : $it")
//
//           saveUserToFireBaseDatabase(it.toString())
//        }


          //  saveUserToFireBaseDataBase()

    }

    private fun saveUserToFireBaseDataBase(profileImageUrl:String) {
        val uid = FirebaseAuth.getInstance().uid?:""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user=User(uid,username_btn.text.toString(),profileImageUrl)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Register Activity","FInally saved to firebase DAtaase")
                val intent = Intent(this, LatestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener{
                Log.d("Register ACtivity","ERRROR")
            }

    }

    private fun login() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }


    var selectedPhotoUrl: Uri?=null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("IMAGE", "djkgsk")


            selectedPhotoUrl = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUrl)

            selectphoto_image_register.setImageBitmap(bitmap)
            selectphoto_button_register.alpha = 0f
        }


    }
}


