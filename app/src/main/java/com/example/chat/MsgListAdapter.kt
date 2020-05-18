package com.example.chat

import com.example.chat.ChatFragmentMessageSend
import com.example.chat.ChatFragment

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth


class MsgListAdapter : RecyclerView.Adapter<MsgListAdapter.MyViewHolder> {

    private val lastMessage: Array<LastMsg>
    var context: Context
    lateinit var comm: ChatCommunicator

    constructor(context: Context, lastMsg: Array<LastMsg>) : super() {
        this.context = context
        this.lastMessage = lastMsg
    }

    class MyViewHolder : RecyclerView.ViewHolder {

        var nickName: TextView
        var lastMessage: TextView
        var firstLetter: TextView


        constructor(view: View) : super(view) {
            nickName = view.findViewById(R.id.textViewNickNameChat)
            lastMessage = view.findViewById(R.id.textViewLastMsg)
            firstLetter = view.findViewById(R.id.textViewFirstLetterChat)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.chat_list_row, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val fireBaseAuth = FirebaseAuth.getInstance()
        val fireBaseUser = fireBaseAuth.currentUser

        val lastMsg: LastMsg = lastMessage[position]

        val lastMsgSenderId = lastMsg.senderId
        val lastMsgRecipientId = lastMsg.recipientId
        var currentNickname = ""
        if (lastMsgSenderId == fireBaseUser?.uid.toString()) {
            currentNickname = lastMsg.recipientNickname
        } else {
            currentNickname = lastMsg.senderNickname
        }

        val currentLastMsg = lastMsg.lastMsg
        var currentFirstLetter = ""
        if (currentNickname != "") {
            currentFirstLetter = currentNickname.first().toString()
        } else {
            currentFirstLetter = "*"
        }

        val currentChatId = lastMsg.chatId
        val currentChatRecipientId = lastMsg.recipientId
        val currentChatSenderId = lastMsg.senderId

        holder.firstLetter.text = currentFirstLetter.toString()
        holder.nickName.text = currentNickname
        holder.lastMessage.text = currentLastMsg

        holder.itemView.setOnClickListener {
            val chatSendFragment: Fragment = ChatFragmentMessageSend()
            comm = chatSendFragment as ChatCommunicator
            if (currentChatSenderId == fireBaseUser?.uid.toString()) {
                comm.passChatSessionInfo(currentChatId, currentChatRecipientId, currentChatSenderId)
            } else {
                // if we weren't the last ones to send a message, swap recipient and sender
                comm.passChatSessionInfo(currentChatId, currentChatSenderId, currentChatRecipientId)
            }
            val manager = (context as FragmentActivity).supportFragmentManager
            val transaction = manager?.beginTransaction()
            transaction?.replace(R.id.fragmentParentViewGroup, chatSendFragment)
            transaction?.commit()
        }

        // alternate cards background color
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.blueTint))
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.pinkTint))
        }

    }

    override fun getItemCount(): Int {
        return lastMessage.size
    }
}