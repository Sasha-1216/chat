package com.example.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass.
 */
class SettingFragmentEdit : Fragment() {


    private lateinit var database: DatabaseReference
    private lateinit var fireBaseAuth: FirebaseAuth
    private var fireBaseUser: FirebaseUser? = null


    lateinit var viewTextUserEmail: TextView
    lateinit var editTextNickname: EditText
    lateinit var buttonSave: Button


    private fun saveUserInformation() {

        val nickname = editTextNickname.text.toString().trim()
        database.child("users").child(fireBaseUser?.uid as String).child("nickname").setValue(nickname)

        Toast.makeText(activity, "Information Saved...", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_setting_edit, container, false)

        database = Firebase.database.reference
        fireBaseAuth = FirebaseAuth.getInstance()
        fireBaseUser = fireBaseAuth.currentUser

        viewTextUserEmail = view.findViewById(R.id.textViewUserEmail)
        viewTextUserEmail.text = fireBaseUser?.email

        editTextNickname = view.findViewById(R.id.editTextNickname)
        editTextNickname.hint = "TODO: Get nickname from database"

        buttonSave = view.findViewById(R.id.buttonSave)
        buttonSave.setOnClickListener {
            saveUserInformation()
            val fragment = SettingFragmentView()
            val manager = activity?.supportFragmentManager
            val transaction = manager?.beginTransaction()
            transaction?.replace(R.id.fragmentParentViewGroup, fragment)
            transaction?.commit()
        }

        return view

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        return
    }

}



