package com.example.chat

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text


class LoginActivity : AppCompatActivity(), View.OnClickListener {

//    private lateinit var backButton: ImageView


    lateinit var fireBaseAuth: FirebaseAuth
    private lateinit var loadingDialog: LoadingDialog

    private lateinit var buttonSignIn: Button
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var textViewSignUp: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        fireBaseAuth = FirebaseAuth.getInstance()

        if (fireBaseAuth.currentUser != null) {
            finish()
            startActivity(Intent(Intent(this, NavHostActivity::class.java)))
        }

        loadingDialog = LoadingDialog(this)
        loadingDialog

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonSignIn = findViewById(R.id.buttonSignIn)
        textViewSignUp = findViewById(R.id.textViewSignUp)

        buttonSignIn.setOnClickListener(this)
        textViewSignUp.setOnClickListener(this)

    }

    private fun userLogin() {
        val email = editTextEmail.getText().toString().trim()
        val password = editTextPassword.getText().toString().trim()

        if (TextUtils.isEmpty(email)) {
            // email is empty
            Toast.makeText(this, "please enter email", Toast.LENGTH_SHORT).show()
            return
        }

        if (TextUtils.isEmpty(password)) {
            // password is empty
            Toast.makeText(this, "please enter password", Toast.LENGTH_SHORT).show()
            return
        }

        loadingDialog.startLoadingDialog()
        fireBaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    finish()
                    startActivity(Intent(Intent(this, NavHostActivity::class.java)))
                } else {
                    Toast.makeText(
                        this,
                        "Could not sign in. Please try again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                loadingDialog.dismissDialog()
            }
    }

    override fun onClick(view: View) {
        if (view == buttonSignIn) {
            userLogin()
        } else if (view == textViewSignUp) {
            finish()
            val loginIntent = Intent(this, MainActivity::class.java)
            startActivity(loginIntent)

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
