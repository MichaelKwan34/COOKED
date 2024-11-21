package com.group34.cooked

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var signIn: Button
    private lateinit var register: Button
    private lateinit var forgotPassword: TextView

    private lateinit var firebaseAuth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()

        // If the user has signed in, open the Home page directly instead of log in
        // Note: LOGOUT implementation is needed
//        val currentUser = firebaseAuth.currentUser
//        if (currentUser != null) {
//            // If user is already signed in, redirect to HomeActivity
//            val intent = Intent(this, HomeActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivity(intent)
//            finish()
//            return // No need to proceed with the rest of the onCreate code
//        }

        email = findViewById(R.id.email_address_input)
        password = findViewById(R.id.password_input)
        signIn = findViewById(R.id.sign_in)
        register = findViewById(R.id.register)
        forgotPassword = findViewById(R.id.forgot_password)

        email.addTextChangedListener(EmailTextWatcher())
        password.addTextChangedListener(PasswordTextWatcher())
        signIn.setOnClickListener {
            if (validateInputs()) {
                val emailValue = email.text.toString()
                val passwordValue = password.text.toString()

                firebaseAuth.signInWithEmailAndPassword(emailValue, passwordValue).addOnCompleteListener{
                    if (it.isSuccessful) {
                        // TO-DO: do we need to pass data?
                        val intent = Intent(this, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        finish()

                    } else {
                        Toast.makeText(this, "Email address and/or password might be incorrect. Please try again", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        register.setOnClickListener{
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }

        forgotPassword.setOnClickListener{
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun EmailTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    email.setBackgroundResource(R.drawable.error_border)
                    email.error = "Email cannot be empty"
                } else {
                    email.setBackgroundResource(R.drawable.rounded_edittext)
                    email.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
    }

    private fun PasswordTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    password.setBackgroundResource(R.drawable.error_border)
                    password.error = "Password cannot be empty"
                } else {
                    password.setBackgroundResource(R.drawable.rounded_edittext)
                    password.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
    }

    private fun validateInputs(): Boolean {
        val emailValue = email.text.toString().trim()
        val passwordValue = password.text.toString().trim()

        return when {
            emailValue.isEmpty() -> {
                email.setBackgroundResource(R.drawable.error_border)
                email.error = "Email cannot be empty"
                false
            }
            passwordValue.isEmpty() -> {
                password.setBackgroundResource(R.drawable.error_border)
                password.error = "Password cannot be empty"
                false
            }
            else -> true
        }
    }

    fun allowFocus(view: View) {
        view.isFocusable = true
        view.isFocusableInTouchMode = true
        view.requestFocus()

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}