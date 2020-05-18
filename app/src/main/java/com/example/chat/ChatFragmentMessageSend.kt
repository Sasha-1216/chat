package com.example.chat

import com.example.chat.MsgBubbleAdapter
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_setting.*
import android.inputmethodservice.AbstractInputMethodService
import android.os.IBinder
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_chat_message_send.*

/**
 * A simple [Fragment] subclass.
 */
class ChatFragmentMessageSend : Fragment(), ChatCommunicator {

    lateinit var buttonSendMsg: FloatingActionButton
    lateinit var recyclerView: RecyclerView
    lateinit var myMsgBubbleAdapter: MsgBubbleAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var inputMessage: EditText
    lateinit var chatId: String
    lateinit var recipientId: String
    lateinit var senderId: String
    lateinit var recipient: TextView
    lateinit var comm: ChatCommunicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(
            R.layout.fragment_chat_message_send,
            container,
            false
        )

        recipient = view.findViewById(R.id.recipient)

        view.setOnTouchListener { _, event ->

            if (event.action == MotionEvent.ACTION_DOWN) {
                val view = activity?.currentFocus
                if (view is EditText) {

                    val outRect = Rect()
                    view.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                        view.clearFocus()
                        val imm =
                            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                        imm!!.hideSoftInputFromWindow(view.getWindowToken(), 0)
                    }
                }
            }
            false
        }

        val context: Context = view.context

        val database = Firebase.database.reference
        val fireBaseAuth = FirebaseAuth.getInstance()
        val fireBaseUser = fireBaseAuth.currentUser
        val messageList_ = mutableListOf<ChatMessage>()
        var recipientNickname = ""
        var senderNickname = ""

        val senderNicknameListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                senderNickname = dataSnapshot.value.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(
                    "FIREBASE",
                    "Error fetching your nickname!",
                    databaseError.toException()
                )
            }

        }

        val recipientNicknameListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                recipientNickname = dataSnapshot.value.toString()
                recipient.text = recipientNickname
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(
                    "FIREBASE",
                    "Error fetching recipient nickname!",
                    databaseError.toException()
                )
            }

        }


        val chatSessionListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                messageList_.clear()
                dataSnapshot.children.forEach() {
                    var msgToAdd: String = ""
                    var senderToAdd: String = ""
                    var recipientToAdd: String = ""
                    it.children.forEach() {

                        if (it.key.toString() == "msg") {
                            msgToAdd = it.value.toString()
                        } else if (it.key.toString() == "sender") {
                            senderToAdd = it.value.toString()
                        } else if (it.key.toString() == "recipient") {
                            recipientToAdd = it.value.toString()
                        }
                    }

                    if (msgToAdd != "" && recipientToAdd != "" && senderToAdd != "") {
                        val messageToAdd = ChatMessage(msgToAdd, recipientToAdd, senderToAdd)
                        messageList_.add(messageToAdd)
                    }
                }

                Log.i("messageList", messageList_.toString())
                // recyclerView = view.findViewById(R.id.messageViewBubbleList)
                // myMsgBubbleAdapter = MsgBubbleAdapter(context, messageList)
                myMsgBubbleAdapter = MsgBubbleAdapter(context, messageList_.toTypedArray())
                recyclerView.adapter = myMsgBubbleAdapter
                linearLayoutManager = LinearLayoutManager(context)
                recyclerView.layoutManager = linearLayoutManager
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(
                    "FIREBASE",
                    "Loading user last name cancelled",
                    databaseError.toException()
                )
            }
        }

        database.child("chats").child(chatId)
            .addValueEventListener(chatSessionListener)

        database.child("users").child(senderId).child("nickname")
            .addListenerForSingleValueEvent(senderNicknameListener)
        database.child("users").child(recipientId).child("nickname")
            .addListenerForSingleValueEvent(recipientNicknameListener)

        recyclerView = view.findViewById(R.id.messageViewBubbleList)
        linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        buttonSendMsg = view.findViewById(R.id.buttonSendMsg)
        inputMessage = view.findViewById(R.id.inputMessage)

        recipient.text = recipientNickname

        buttonSendMsg.setOnClickListener {

            val newMsg = mutableMapOf<String, String>()
            newMsg["recipient"] = recipientId
            newMsg["sender"] = fireBaseUser?.uid.toString()
            newMsg["msg"] = inputMessage.text.toString()
            newMsg["senderNickname"] = senderNickname
            newMsg["recipientNickname"] = recipientNickname

            if (chatId == "") {
                val newChat = database.child("chats").push()
                val newChatId = newChat.getKey()
                database.child("chats").child(newChatId.toString()).push().setValue(newMsg)
                database.child("chatinfo").child(recipientId).child(fireBaseUser?.uid.toString())
                    .child("chatid").setValue(newChatId)
                database.child("chatinfo").child(fireBaseUser?.uid.toString()).child(recipientId)
                    .child("chatid").setValue(newChatId)
                database.child("chatinfo").child(recipientId).child(fireBaseUser?.uid.toString())
                    .child("uid").setValue(recipientId)
                database.child("chatinfo").child(fireBaseUser?.uid.toString()).child(recipientId)
                    .child("uid").setValue(fireBaseUser?.uid.toString())
                database.child("users").child(recipientId).child("chats")
                    .child(newChatId.toString()).setValue(true)
                database.child("users").child(fireBaseUser?.uid.toString()).child("chats")
                    .child(newChatId.toString()).setValue(true)

                val chatSendFragment: Fragment = ChatFragmentMessageSend()
                comm = chatSendFragment as ChatCommunicator
                comm.passChatSessionInfo(
                    newChatId.toString(),
                    recipientId,
                    fireBaseUser?.uid.toString()
                )
                val manager = (context as FragmentActivity).supportFragmentManager
                val transaction = manager?.beginTransaction()
                transaction?.replace(R.id.fragmentParentViewGroup, chatSendFragment)
                transaction?.commit()

            } else {
                database.child("chats").child(chatId).push().setValue(newMsg)
            }

            inputMessage.text.clear()
        }

        return view
    }

    override fun passChatSessionInfo(chatId: String, recipientId: String, senderId: String) {
        Log.i("chatId: ", chatId)
        this.chatId = chatId
        this.recipientId = recipientId
        this.senderId = senderId
    }


}
