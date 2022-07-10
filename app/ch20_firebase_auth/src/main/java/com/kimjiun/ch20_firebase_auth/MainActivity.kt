package com.kimjiun.ch20_firebase_auth

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kimjiun.ch20_firebase_auth.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val TAG = "JIUNKIM"
    lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val email = "ggidid@gmail.com"
        val password = "12345678"

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){
                task ->
                if(task.isSuccessful){
                    Log.d(TAG, "createUserWithEmailAndPassword SUCCESS")
                    auth.currentUser?.sendEmailVerification()?.addOnCompleteListener{
                        sendTask ->
                        if(sendTask.isSuccessful){
                            Log.d(TAG, "sendEmailVerification SUCCESS")
                        }
                        else{
                            Log.d(TAG, "sendEmailVerification FAIL ${sendTask.exception}")
                        }
                    }
                }
                else{
                    Log.d(TAG, "createUserWithEmailAndPassword FAIL ${task.exception}")
                }
            }
    }
}