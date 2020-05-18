package com.example.chat


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

data class ChatMessage(val msg: String, val recipient: String, val sender: String)
data class ChatMessage_(val side: String, val msg: String)

data class LastMsg(var chatId: String, var senderNickname: String, var recipientNickname: String, var lastMsg: String, var recipientId: String, var senderId: String)

/**
 * A simple [Fragment] subclass.
 */
class ChatFragmentMessageView : Fragment() {

    //
    lateinit var recyclerView: RecyclerView
    lateinit var myMsgListAdapter: MsgListAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var nickName: TextView
    lateinit var firstLetter: TextView
    lateinit var lastMsg: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(
            R.layout.fragment_chat_message_view,
            container,
            false
        )

        val database = Firebase.database.reference
        val fireBaseAuth = FirebaseAuth.getInstance()
        val fireBaseUser = fireBaseAuth.currentUser
        val myChatIds = mutableMapOf<String, Boolean>()
        val lastMsgMap = mutableMapOf<String, LastMsg>()

        val lastMessageListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.i("lastMessageListener", dataSnapshot.toString())
                val chatId = dataSnapshot.key.toString()
                var msgId: String = ""
                dataSnapshot.children.forEach() {
                    msgId = it.key.toString()
                    var msgToAdd: String = ""
                    var recipientToAdd: String = ""
                    var senderToAdd: String = ""
                    var senderNickname = ""
                    var recipientNickname = ""

                    it.children.forEach() {

                        if (it.key.toString() == "msg") {
                            msgToAdd = it.value.toString()
                        } else if (it.key.toString() == "recipient") {
                            recipientToAdd = it.value.toString()
                        } else if (it.key.toString() == "sender") {
                            senderToAdd = it.value.toString()
                        } else if (it.key.toString() == "senderNickname") {
                            senderNickname = it.value.toString()
                        } else if (it.key.toString() == "recipientNickname") {
                            recipientNickname = it.value.toString()
                        }

                    }

                    val lastMessageToAdd = LastMsg(chatId, senderNickname, recipientNickname, msgToAdd, recipientToAdd, senderToAdd)
                    lastMsgMap[msgId] = lastMessageToAdd
                }

                val context: Context = view.context
                val lastMsgList_ = mutableListOf<LastMsg>()

                val lastMsgMapIterator = lastMsgMap.iterator()

                while (lastMsgMapIterator.hasNext()) {
                    val currentMsg = lastMsgMapIterator.next().value
                    lastMsgList_.add(currentMsg)
                }

                Log.i("lastMsgList", lastMsgList_.toString())

                recyclerView = view.findViewById(R.id.myChatList)
                myMsgListAdapter = MsgListAdapter(context, lastMsgList_.toTypedArray())
                recyclerView.adapter = myMsgListAdapter
                linearLayoutManager = LinearLayoutManager(context)
                recyclerView.layoutManager = linearLayoutManager
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(
                    "FIREBASE",
                    "Error loading last message",
                    databaseError.toException()
                )
            }
        }

        val availableChatsListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach() {
                    if (it.value.toString() == "true") {
                        val chatId = it.key.toString()
                        myChatIds[chatId] = true
                    }
                }

                myChatIds.forEach() {
                    database.child("chats").child(it.key.toString()).orderByKey().limitToLast(1)
                        .addListenerForSingleValueEvent(lastMessageListener)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(
                    "FIREBASE",
                    "Error loading available chats",
                    databaseError.toException()
                )
            }
        }

        database.child("users").child(fireBaseUser?.uid.toString()).child("chats")
            .addValueEventListener(availableChatsListener)


//        val data = mutableMapOf<String, String>()
//        data["recipient"] = "FtW6VEC0K3SDna6jM3DKUA0ZX8B2"
//        data["sender"] = "MSR3rFnxzuYwmV5Q0SnrEIENUSZ2"
//        data["msg"] = "I pooped in my bed!"
//        database.child("chats").child("abc123").push().setValue(data)

//        val context: Context = view.context
//
//        recyclerView = view.findViewById(R.id.myChatList)
//        myMsgListAdapter = MsgListAdapter(context, lastMsgList)
//        recyclerView.adapter = myMsgListAdapter
//        linearLayoutManager = LinearLayoutManager(context)
//        recyclerView.layoutManager = linearLayoutManager

        return view
    }


}
