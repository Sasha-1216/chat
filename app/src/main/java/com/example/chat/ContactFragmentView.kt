package com.example.chat


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.my_contact_row.*

/**
 * A simple [Fragment] subclass.
 *
 *
 */

data class Contact(val nickname: String, val email: String)

val testContactList =  arrayOf(
    Contact("Bob", "bob@bob.com)"),
    Contact("Nick", "nick@nick.com"),
    Contact("Ann", "ann@ann.com"),
    Contact("Steve", "steve@steve.com"),
    Contact("Amy", "amy@amy.com"),
    Contact("Jerry", "jerry@jerry.com"),
    Contact("Geek", "geek@geek.com"),
    Contact("Andy", "andy@andy.com"),
    Contact("Sting", "string@string.com")
)

class ContactFragmentView : Fragment(){

    lateinit var buttonAddContact: FloatingActionButton
    lateinit var recyclerView: RecyclerView
    lateinit var myContactAdapter: MyContactAdapter
    lateinit var linearLayoutManager : LinearLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_contact_view,
            container,
            false
        )



        recyclerView = view.findViewById(R.id.myContactList)

        val context: Context = view.context

        myContactAdapter = MyContactAdapter(context, testContactList)

        recyclerView.adapter = myContactAdapter

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



