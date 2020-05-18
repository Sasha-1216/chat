package com.example.chat

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var fireBaseAuth: FirebaseAuth
    private lateinit var loadingDialog: LoadingDialog

    private lateinit var buttonRegister: Button
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var textViewSignIn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        fireBaseAuth = FirebaseAuth.getInstance()

        if (fireBaseAuth.currentUser != null) {
            finish()
            startActivity(Intent(Intent(this, NavHostActivity::class.java)))
        }


        loadingDialog = LoadingDialog(this)

        buttonRegister = findViewById(R.id.buttonRegister)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        textViewSignIn = findViewById(R.id.textViewSignIn)

        buttonRegister.setOnClickListener(this)
        textViewSignIn.setOnClickListener(this)

    }


    private fun registerUser() {
        val email = editTextEmail.getText().toString().trim()
        val password = editTextPassword.getText().toString().trim()
        val nickname = editNickName.getText().toString().trim()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
            return
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
            return
        }

        if (TextUtils.isEmpty(nickname)) {
            Toast.makeText(this, "Please enter nickname", Toast.LENGTH_SHORT).show()
            return
        }

        loadingDialog.startLoadingDialog()
        fireBaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    finish()
                    startActivity(Intent(Intent(this, NavHostActivity::class.java)))

                    val database = Firebase.database.reference
                    var fireBaseAuth = FirebaseAuth.getInstance()
                    val fireBaseUser = fireBaseAuth.currentUser
                    if (fireBaseUser?.uid != null) {
                        database.child("users").child(fireBaseUser?.uid.toString())
                            .child("nickname").setValue(nickname)
                        database.child("users").child(fireBaseUser?.uid.toString())
                            .child("email").setValue(email)
                        database.child("users").child(fireBaseUser?.uid.toString())
                            .child("uid").setValue(fireBaseUser?.uid.toString())
                    }


                    Toast.makeText(
                        this,
                        "Registered Successfully!",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Toast.makeText(
                        this,
                        "Could not register. Please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                loadingDialog.dismissDialog()
            }
    }

    override fun onClick(view: View?) {
        if (view == buttonRegister) {
            registerUser()
        } else if (view == textViewSignIn) {
            startActivity(Intent(Intent(this, LoginActivity::class.java)))
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }


}













