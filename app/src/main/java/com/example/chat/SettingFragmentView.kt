package com.example.chat


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
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

class SettingFragmentView : Fragment(), View.OnClickListener {

    lateinit var fireBaseAuth: FirebaseAuth
    lateinit var viewTextUserEmail: TextView
    private lateinit var database: DatabaseReference
    lateinit var textViewNickname: TextView
    lateinit var textViewFirstLetter: TextView
    lateinit var buttonEdit: Button
    lateinit var buttonLogout: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_setting_view, container, false)


        buttonEdit = view.findViewById(R.id.buttonEdit)
        buttonEdit.setOnClickListener(this)

        buttonLogout = view.findViewById(R.id.buttonLogOut)
        buttonLogout.setOnClickListener(this)

        textViewFirstLetter = view.findViewById(R.id.textViewFirstLetter)

        database = Firebase.database.reference
        fireBaseAuth = FirebaseAuth.getInstance()
        val fireBaseUser = fireBaseAuth.currentUser

        if (fireBaseAuth.currentUser == null) {

            val intent = Intent(view.context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            activity?.finish()
            startActivity(intent)
        }

        textViewNickname = view.findViewById(R.id.textViewNickname)
        viewTextUserEmail = view.findViewById(R.id.textViewUserEmail)
        viewTextUserEmail.setText(fireBaseUser?.email)

        val nicknameListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val nickname = dataSnapshot.value
                textViewNickname.text = nickname.toString()
                val nicknameFirstLetter = nickname.toString().first().toString()
                textViewFirstLetter.text = nicknameFirstLetter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("FIREBASE", "Loading user last name cancelled", databaseError.toException())
            }
        }

        database.child("users").child(fireBaseUser?.uid as String).child("nickname")
            .addListenerForSingleValueEvent(nicknameListener)

        return view
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        return
    }


    override fun onClick(view: View) {

        if (view == buttonLogout) {
            fireBaseAuth.signOut()
            val intent = Intent(view.context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            activity?.finish()
            startActivity(intent)
        } else if (view == buttonEdit) {
            val fragment = SettingFragmentEdit()
            val manager = activity?.supportFragmentManager
            val transaction = manager?.beginTransaction()
            transaction?.replace(R.id.fragmentParentViewGroup, fragment)
            transaction?.commit()
        }
    }
}


