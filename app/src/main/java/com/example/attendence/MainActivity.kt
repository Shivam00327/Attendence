package com.example.attendence

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendence.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import java.util.zip.Inflater

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var itemList: ArrayList<CounterItem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()
        val currentUser=auth.currentUser

        if(currentUser==null){
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        itemList= arrayListOf()

        databaseReference=FirebaseDatabase.getInstance().reference.child("User").child(currentUser!!.uid)


        binding.recyclerViewProject.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.recyclerViewProject.adapter=AdapterRV(itemList)

        binding.buttonLogOut.setOnClickListener{
            auth.signOut()
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.buttonSubjects.setOnClickListener{
            createDialog()

        }
        fetchDataFromFirebase()

    }

    private fun fetchDataFromFirebase( ){
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                itemList.clear()
                if(snapshot.exists()){
                    for(dataSnapshot in snapshot.children){
                        var item= dataSnapshot.getValue(CounterItem::class.java)
                        if(item!=null){
                            itemList.add(item)
                        }
                    }
                }

                binding.recyclerViewProject.adapter=AdapterRV(itemList)

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    fun createDialog() {
        var builder=AlertDialog.Builder(this)
        var inflater=getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var dialogView=inflater.inflate(R.layout.dialog_input,null)


        var alertDialog=builder.setView(dialogView).create()
        var inputName:EditText=dialogView.findViewById(R.id.edit_text_subject)
        var sveBtn:Button=dialogView.findViewById(R.id.button_add_subjects)

        sveBtn.setOnClickListener {
            var name=inputName.text.toString()
            if(name.isEmpty()){
                Toast.makeText(this,"Enter the name of the Subject",Toast.LENGTH_LONG).show()
            }else{
                var store=databaseReference.push()
                var item=CounterItem(store.key,name,0,0)
                store.setValue(item)
                alertDialog.dismiss()
            }
        }
        alertDialog.show()


    }
}