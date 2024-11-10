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
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var signIn: Button
    private lateinit var register: Button
    private lateinit var forgotPassword: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.email_address_input)
        password = findViewById(R.id.password_input)
        signIn = findViewById(R.id.sign_in)
        register = findViewById(R.id.register)
        forgotPassword = findViewById(R.id.forgot_password)

        email.addTextChangedListener(EmailTextWatcher())
        password.addTextChangedListener(PasswordTextWatcher())

        signIn.setOnClickListener {
            if (validateInputs()) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }else {
             // error message
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