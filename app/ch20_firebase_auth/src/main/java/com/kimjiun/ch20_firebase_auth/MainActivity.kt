package com.kimjiun.ch20_firebase_auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kimjiun.ch20_firebase_auth.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val TAG = "JIUNKIM"
    lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth
    lateinit var requestLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        //login()
        googleAuth()

        binding.button.setOnClickListener { creatUser() }
        binding.button2.setOnClickListener {
            auth.currentUser?.let {
                Log.d(TAG, "${it.isEmailVerified} / ${it.email} / ${it.uid}")
            }
        }
    }

    fun googleAuth(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val signIntent = GoogleSignIn.getClient(this, gso).signInIntent

        requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            Log.d(TAG, "IT DATA ${it.data}")
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try{
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this){ task ->
                        if(task.isSuccessful){
                            Log.d(TAG, "signInWithCredential SUCCESS")
                        }
                        else{
                            Log.d(TAG, "signInWithCredential FAIL ${task.exception}")
                        }
                    }

            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        requestLauncher.launch(signIntent)
    }

    fun login(){
        val email = "ggidid@gmail.com"
        val password = "12345678"

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){ task ->
                if(task.isSuccessful){
                    Log.d(TAG, "signInWithEmailAndPassword SUCCESS")

                }
                else{
                    Log.d(TAG, "signInWithEmailAndPassword FAIL ${task.exception}")
                }
            }

    }

    fun creatUser(){

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