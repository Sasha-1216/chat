package com.example.chat


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.my_contact_row.*

/**
 * A simple [Fragment] subclass.
 *
 *
 */

val testContactList = arrayOf(
    Contact("Bob", "bob@bob.com", "abc123"),
    Contact("Nick", "nick@nick.com", "abc123"),
    Contact("Ann", "ann@ann.com", "abc123"),
    Contact("Steve", "steve@steve.com", "abc123"),
    Contact("Amy", "amy@amy.com", "abc123"),
    Contact("Jerry", "jerry@jerry.com", "abc123"),
    Contact("Geek", "geek@geek.com", "abc123"),
    Contact("Andy", "andy@andy.com", "abc123"),
    Contact("Sting", "string@string.com", "abc123")
)

class ContactFragmentView : Fragment() {

    lateinit var buttonAddContact: FloatingActionButton
    lateinit var recyclerView: RecyclerView
    lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(
            R.layout.fragment_contact_view,
            container,
            false
        )


        recyclerView = view.findViewById(R.id.myContactList)

        val context: Context = view.context

        val database = Firebase.database.reference
        val fireBaseAuth = FirebaseAuth.getInstance()
        val fireBaseUser = fireBaseAuth.currentUser
        val uidsOfContacts = mutableListOf<String>()
        val contacts = mutableMapOf<String, Contact>()

        val existingChatListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.i("existChatListener", dataSnapshot.toString())
                if (dataSnapshot.value != null) {
                    var uid = ""
                    var chatid = ""
                    dataSnapshot.children.forEach() {
                        if (it.key.toString() == "uid") {
                            uid = it.value.toString()
                        } else if (it.key.toString() == "chatid") {
                            chatid = it.value.toString()
                        }
                    }
                    if (uid != "" && chatid != "") {
                        contacts[uid]?.chatId = chatid
                    }
                    Log.i("existChatListener", "Have existing chat data!")
                } else {
                    Log.i("existChatListener", "No existing chat data.")
                }

                val contactsMapIterator = contacts.iterator()
                val contactsList = mutableListOf<Contact>()

                while (contactsMapIterator.hasNext()) {
                    val currentContactMap = contactsMapIterator.next()
                    contactsList.add(currentContactMap.value)
                }

                val myContactViewAdapter = MyContactViewAdapter(context, contactsList.toTypedArray())
                recyclerView.adapter = myContactViewAdapter
                linearLayoutManager = LinearLayoutManager(context)
                recyclerView.layoutManager = linearLayoutManager
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(
                    "FIREBASE",
                    "Error loading existing chat info",
                    databaseError.toException()
                )
            }
        }

        val contactsListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var uidToAdd: String = ""
                var emailToAdd: String = ""
                var nicknameToAdd: String = ""
                var chatIdToAdd: String = ""
                var childrenIterator = dataSnapshot.children.iterator()
                while (childrenIterator.hasNext()) {
                    val currentChild = childrenIterator.next()
                    if (currentChild.key.toString() == "email") {
                        emailToAdd = currentChild.value.toString()
                    } else if (currentChild.key.toString() == "uid") {
                        uidToAdd = currentChild.value.toString()
                    } else if (currentChild.key.toString() == "nickname") {
                        nicknameToAdd = currentChild.value.toString()
                    }
                    Log.i("current child value", currentChild.value.toString())
                }

                val contactToAdd = Contact(nicknameToAdd, emailToAdd, uidToAdd, chatIdToAdd)
                contacts.put(uidToAdd, contactToAdd)

                database.child("chatinfo").child(uidToAdd).child(fireBaseUser?.uid.toString()).addValueEventListener(existingChatListener)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(
                    "FIREBASE",
                    "Error loading contact info",
                    databaseError.toException()
                )
            }
        }

        val contactsUidsListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach() {
                    if (it.value == true) {
                        uidsOfContacts.add(it.key.toString())
                    }
                    Log.i("contact uids: ", uidsOfContacts.toString())
                }

                uidsOfContacts.forEach() {
                    database.child("users").child(it.toString())
                        .addValueEventListener(contactsListener)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(
                    "FIREBASE",
                    "Loading user last name cancelled",
                    databaseError.toException()
                )
            }
        }

        database.child("contacts").child(fireBaseUser?.uid as String)
            .addValueEventListener(contactsUidsListener)

        linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager

        buttonAddContact = view.findViewById(R.id.buttonAddContact)

        buttonAddContact.setOnClickListener {
            val searchAddFragment: Fragment = ContactFragmentSearchAdd()
            val manager = activity?.supportFragmentManager
            val transaction = manager?.beginTransaction()
            transaction?.replace(R.id.fragmentParentViewGroup, searchAddFragment)
            transaction?.commit()
        }

        return view

    }

}



