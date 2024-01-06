package com.example.attendence

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.attendence.databinding.ActivityMainBinding
import com.example.attendence.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var auth=FirebaseAuth.getInstance()


        binding.buttonSignUp.setOnClickListener{
            val emailText=binding.editSignupId.text.toString()
            val passText=binding.editSinUpPass.text.toString()
            val CpassText=binding.editSignUpCpass.text.toString()

            if(emailText.isEmpty()||passText.isEmpty()||CpassText.isEmpty()){
                Toast.makeText(this,"PLease enter the valid input",Toast.LENGTH_LONG).show()
            }
            if(!passText.equals(CpassText)){
                Toast.makeText(this,"Password do not match.",Toast.LENGTH_LONG).show()

            }else{
                auth.createUserWithEmailAndPassword(emailText,passText)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            val intent=Intent(this,LoginActivity::class.java)
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
        binding.buttonSignIn.setOnClickListener{
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}