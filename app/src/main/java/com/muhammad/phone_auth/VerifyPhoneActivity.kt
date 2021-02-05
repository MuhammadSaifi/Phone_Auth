package com.muhammad.phone_auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_verify_phone.*
import java.util.concurrent.TimeUnit

class VerifyPhoneActivity : AppCompatActivity() {
    private var verificationId: String? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var progressBar: ProgressBar? = null
    private var editText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_phone)

        firebaseAuth = FirebaseAuth.getInstance()
        progressBar = findViewById(R.id.progressbar)
        editText =findViewById(R.id.editTextCode)

        val phonenumber = intent.getStringExtra("phone")
        sendCode(phonenumber)
 btnSignIn.setOnClickListener {
     val code = editText!!.text.toString()
     if(code.isEmpty() || code.length<6){
         Toast.makeText(this,"Enter Code", Toast.LENGTH_SHORT).show()
     }
     verifyCode(code)
 }

    }

    private fun sendCode(number: String){
        progressBar!!.visibility = View.VISIBLE
        PhoneAuthProvider.getInstance()
            .verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
            )
    }
    private val mCallBack =  object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

        override fun onCodeSent(p0: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
            super.onCodeSent(p0, p1)
            verificationId = p0
        }
        override fun onVerificationCompleted(p0: PhoneAuthCredential?) {
             val code = p0!!.smsCode
            if(code!=null){
                editText!!.setText(code)
                verifyCode(code)
            }
        }

        override fun onVerificationFailed(p0: FirebaseException?) {
        }

    }

    private fun verifyCode(code: String){
        val credential = PhoneAuthProvider.getCredential(verificationId!!,code)
        signIn(credential)
    }
    private fun signIn(credential: PhoneAuthCredential ){
        firebaseAuth!!.signInWithCredential(credential).addOnCompleteListener { task ->
            if(task.isSuccessful){
                startActivity(Intent(this,ProfileActivity::class.java))
            }
        }
    }

}