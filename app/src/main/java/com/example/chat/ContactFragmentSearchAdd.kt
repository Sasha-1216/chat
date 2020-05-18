package com.example.chat


import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


/**
 * A simple [Fragment] subclass.
 */
class ContactFragmentSearchAdd : Fragment() {

    lateinit var buttonSearch: FloatingActionButton
    lateinit var recyclerView: RecyclerView
    lateinit var myContactAdapter: MyContactAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    lateinit var fireBaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    lateinit var searchField: EditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(
            R.layout.fragment_contact_search_add,
            container,
            false
        )

        searchField = view.findViewById(R.id.searchField)

        buttonSearch = view.findViewById(R.id.buttonSearch)
//        textViewUserEmail = view.findViewById(R.id.textViewUserEmail)
//        textViewNickname = view.findViewById(R.id.textViewNickName)

        recyclerView = view.findViewById(R.id.myContactList)
//        viewTextUserEmail = view.findViewById(R.id.textViewUserEmail)

        var results: Array<Contact>
        val context: Context = view.context

        database = Firebase.database.reference
        fireBaseAuth = FirebaseAuth.getInstance()
        val fireBaseUser = fireBaseAuth.currentUser
        var dataResults = mutableListOf<Contact>()

        fun searchContacts() {

            val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

            val searchTerm = searchField.text.toString().trim().toLowerCase()

            val contactsListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val unfilteredDataResults = mutableListOf<Contact>()
                    unfilteredDataResults.clear()
                    dataResults.clear()
                    dataSnapshot.children.mapNotNullTo(unfilteredDataResults) {
                        it.getValue<Contact>(Contact::class.java)
                    }

                    val filteredDataResults = unfilteredDataResults.filter {
                        it.email.toLowerCase().contains(searchTerm) ||
                            it.nickname.toLowerCase().contains(searchTerm)
                    }


                    Log.i("result: ", "")
                    val iterator = filteredDataResults.listIterator()
                    while (iterator.hasNext()) {
                        val currentContact = iterator.next()
                        dataResults.add(currentContact)
                    }
                    myContactAdapter = MyContactAdapter(context, dataResults.toTypedArray())
                    recyclerView.adapter = myContactAdapter
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

            database.child("users").addValueEventListener(contactsListener)
        }

//        searchField.setOnEditorActionListener { v, actionId, event ->
//            if(actionId == EditorInfo.IME_ACTION_SEARCH){
//                searchContacts()
//                true
//            } else {
//                false
//            }
//        }

        buttonSearch.setOnClickListener {
            searchContacts()
        }


        //dispatch keyboard touch event

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

        return view
    }

}
