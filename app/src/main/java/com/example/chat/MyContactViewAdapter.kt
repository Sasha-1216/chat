package com.example.chat

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MyContactViewAdapter : RecyclerView.Adapter<MyContactViewAdapter.MyViewHolder> {

    private val contacts: Array<Contact>
    var context: Context
    lateinit var comm: ChatCommunicator

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
        val currentChatId = currentContact.chatId
        val currentRecipientId = currentContact.uid

        holder.firstLetter.text = currentFirstLetter
        holder.nickName.text = currentNickname
        holder.email.text = currentEmail

        val fireBaseAuth = FirebaseAuth.getInstance()
        val fireBaseUser = fireBaseAuth.currentUser

        holder.itemView.setOnClickListener {
            if (currentChatId != "") {
                Log.i("currentChatId", currentChatId)

            } else {
                Log.i("currentChatId", "missing!")
            }

            val chatSendFragment: Fragment = ChatFragmentMessageSend()
            comm = chatSendFragment as ChatCommunicator
            comm.passChatSessionInfo(currentChatId, currentRecipientId, fireBaseUser?.uid.toString())
            val manager = (context as FragmentActivity).supportFragmentManager
            val transaction = manager?.beginTransaction()
            transaction?.replace(R.id.fragmentParentViewGroup, chatSendFragment)
            transaction?.commit()

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