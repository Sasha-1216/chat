package com.example.chat

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MyContactAdapter : RecyclerView.Adapter<MyContactAdapter.MyViewHolder> {

    private val contacts: Array<Contact>
    var context: Context

    constructor(context: Context, contacts:  Array<Contact>) : super() {
        this.context = context
        this.contacts = contacts
    }

    class MyViewHolder : RecyclerView.ViewHolder {

      var nickName : TextView
      var email : TextView
      var firstLetter : TextView


        constructor(view: View) : super(view) {
            nickName = view.findViewById(R.id.textViewNickName)
            email = view.findViewById(R.id.textViewLastMsg)
            firstLetter = view.findViewById(R.id.textViewFirstLetter)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.my_contact_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentContact: Contact = contacts[position]

        val currentNickname = currentContact.nickname
        val currentEmail = currentContact.email
        val currentFirstLetter = currentNickname.first().toString()

        val database = Firebase.database.reference
        val fireBaseAuth = FirebaseAuth.getInstance()
        val fireBaseUser = fireBaseAuth.currentUser

        holder.firstLetter.text = currentFirstLetter
        holder.nickName.text = currentNickname
        holder.email.text = currentEmail

        holder.itemView.setOnClickListener {

            database.child("contacts").child(fireBaseUser?.uid as String).child(currentContact.uid).setValue(true)
            Toast.makeText(context, "Contact added!", Toast.LENGTH_SHORT).show()
        }

        // alternate cards background color
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.grayTint))
        }
    }




    override fun getItemCount(): Int {
        return contacts.size
    }
}