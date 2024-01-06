package com.example.attendence

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AdapterRV( private val data:ArrayList<CounterItem>): RecyclerView.Adapter<AdapterRV.CardViewHolder>() {

    class CardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){


        private var currentUser=FirebaseAuth.getInstance().currentUser
        private var databaseReference=FirebaseDatabase.getInstance().reference.child("User").child(currentUser!!.uid)

        private var className:TextView=itemView.findViewById(R.id.edit_text_view_name)
        private var classAttended:TextView=itemView.findViewById(R.id.edit_text_view_present)
        private var classMissed:TextView=itemView.findViewById(R.id.edit_text_view_absent)
        private var attendBtn:Button=itemView.findViewById(R.id.button_present)
        private var missedBtn:Button=itemView.findViewById(R.id.button_absent)
        private var card:CardView=itemView.findViewById(R.id.card_view_data)


        fun bind(item: CounterItem){
            className.text=item.name
            classAttended.text=item.attendedClass.toString()
            classMissed.text=item.missedClass.toString()


            attendBtn.setOnClickListener{
                databaseReference.child(item.id!!).child("attendedClass").setValue(item.attendedClass!!+1)
            }
            missedBtn.setOnClickListener {
                databaseReference.child(item.id!!).child("missedClass").setValue(item.missedClass!!+1)


            }
            card.setOnClickListener{
                showDialog(itemView.context,item)

            }


        }

        private fun showDialog(context: Context,item: CounterItem) {
            val builder=AlertDialog.Builder(context)
            builder.setTitle("Delete the Subject")
                .setMessage("Do you want to delete this subject")
                .setPositiveButton("Delete"){_,_->
                    databaseReference.child(item.id!!).removeValue()
                    Toast.makeText(context,"Subject Deleted",Toast.LENGTH_SHORT).show()


                }.setNegativeButton("Cancel"){dialog,_->
                    dialog.dismiss()

                }.create().show()

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_project,parent,false)
        return CardViewHolder(view)

    }

    override fun getItemCount(): Int {
        return data.size

    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(data[position])

    }
}