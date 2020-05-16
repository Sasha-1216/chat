package com.example.chat

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_setting.*
import android.R





/**
 * A simple [Fragment] subclass.
 */
class SettingFragment : Fragment(){

    lateinit var fireBaseAuth: FirebaseAuth
    lateinit var textViewUserEmail: TextView
    lateinit var buttonLogOut: Button
    private lateinit var database: DatabaseReference

    lateinit var buttonSave: Button
    lateinit var buttonEdit: Button

    lateinit var editTextFirstName: EditText
    lateinit var editTextLastName: EditText



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment

        val view: View = inflater.inflate(
            com.example.chat.R.layout.fragment_setting,
            fragmentParentViewGroup,
            false
        )



        fireBaseAuth = FirebaseAuth.getInstance()
        if (fireBaseAuth.currentUser == null) {

            val intent = Intent(view.context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            activity?.finish()
            startActivity(intent)
        }




        database = Firebase.database.reference
        val fireBaseUser = fireBaseAuth.currentUser

//        textViewUserEmail = view.findViewById(com.example.chat.R.id.textViewUserEmail)
//        textViewUserEmail.setText(fireBaseUser?.email)


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


    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)

        val viewFragment: Fragment = SettingFragmentView()
        val manager = activity?.supportFragmentManager
        val transaction = manager?.beginTransaction()
        transaction?.replace(com.example.chat.R.id.fragmentParentViewGroup, viewFragment)
        transaction?.commit()
        return
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        editTextFirstName = view.findViewById(com.example.chat.R.id.editTextFirstName)
//        editTextLastName = view.findViewById(com.example.chat.R.id.editTextLastName)
//        buttonSave = view.findViewById(com.example.chat.R.id.buttonSave)
//        buttonEdit = view.findViewById(com.example.chat.R.id.buttonEdit)
//       buttonLogOut = view.findViewById(com.example.chat.R.id.buttonLogOut)


    }


//    override fun onClick(view: View) {
//
//
//
//        if (view == buttonSave) {
//            saveUserInformation()
//
//        } else if (view == buttonLogOut) {
//        fireBaseAuth.signOut()
//        val intent = Intent(view.context, LoginActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        activity?.finish()
//        startActivity(intent)
//
//    }  else if (view == buttonEdit) {
//
//        }
//
//    }



}







