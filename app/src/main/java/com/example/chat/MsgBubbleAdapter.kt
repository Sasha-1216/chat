package com.example.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MsgBubbleAdapter : RecyclerView.Adapter<MsgBubbleAdapter.MyViewHolder> {

    private val messages: Array<ChatMessage>
    var context: Context

    constructor(context: Context, messages: Array<ChatMessage>) : super() {
        this.context = context
        this.messages = messages

    }

    class MyViewHolder : RecyclerView.ViewHolder {

        var message: TextView

        constructor(view: View) : super(view) {
            message = view.findViewById(R.id.messageViewBubble)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.message_bubble_view, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentMessage: ChatMessage = messages[position]

        val messageText = currentMessage.msg
        val sender = currentMessage.sender
        val recipient = currentMessage.recipient

        val database = Firebase.database.reference
        val fireBaseAuth = FirebaseAuth.getInstance()
        val fireBaseUser = fireBaseAuth.currentUser
        holder.message.text = messageText

        // alternate cards background color
        if (sender == fireBaseUser?.uid.toString()) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.pinkPrimary))
            holder.message.setTextColor(ContextCompat.getColor(context, R.color.grayLight))
            holder.itemView.setBackgroundResource(R.drawable.bubble_left)
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.grayLight))
            holder.message.setTextColor(ContextCompat.getColor(context, R.color.textDark))
            holder.itemView.setBackgroundResource(R.drawable.bubble_right)

            var setLayoutParamsForParent : LinearLayout
            var params = LinearLayout.LayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT))
//            paramsLinearLayout =  params.gravity =

//            var params: LinearLayout.LayoutParams? = LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//            params = setLayoutParamsForParent(params, item.isPosition())
//            holder.llParentLayout.setLayoutParams(params) //corresponding to id_llparent ID


        }
    }


    override fun getItemCount(): Int {
        return messages.size
    }
}