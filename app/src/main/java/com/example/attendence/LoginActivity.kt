package com.example.attendence

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.attendence.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding

    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()
        val currentUser=auth.currentUser
        if(currentUser!=null){
            var intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.buttonLogin.setOnClickListener{

            val emailText=binding.editSignInId.text.toString()
            val passText=binding.editSignInPass.text.toString()

            if(emailText.isEmpty() || passText.isEmpty()){
                Toast.makeText(this,"Please the valid details",Toast.LENGTH_LONG).show()
            }
            else{
                auth.signInWithEmailAndPassword(emailText,passText)
                    .addOnCompleteListener{
                        if (it.isSuccessful){
                            val intent= Intent(this,MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else{
                            var message=it.exception?.message?:"Error occurred"
                            Toast.makeText(this,message,Toast.LENGTH_LONG).show()
                        }
                    }
            }

        }
        binding.textViewCreateAccount.setOnClickListener{
            val intent=Intent(this,SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}