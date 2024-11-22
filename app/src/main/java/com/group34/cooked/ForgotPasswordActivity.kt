package com.group34.cooked

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var email: EditText

    private lateinit var firebaseAuth : FirebaseAuth

    private lateinit var verificationCode : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        firebaseAuth = FirebaseAuth.getInstance()

        email = findViewById(R.id.email_address_forgot_password)

        addEmailTextChangedListener(email, "Email Address is required", "Invalid email format")

        val back: Button = findViewById(R.id.back_forgot_password)
        back.setOnClickListener {
            finish()
        }

        val resetPassword: Button = findViewById(R.id.reset_password)
        resetPassword.setOnClickListener {
            val isEmailValid = validateEmail(email, "Invalid email format")
            val emailValue = email.text.toString()

            if (isEmailValid) {
                firebaseAuth.sendPasswordResetEmail(emailValue).addOnCompleteListener{
                    if (it.isSuccessful) {
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        Toast.makeText(this, "Check your email to reset password", Toast.LENGTH_SHORT).show()
                        startActivity(intent)

                    } else {
                        Toast.makeText(this, "Email address might be incorrect. Please try again", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun validateEmail(field: EditText, errorMessage: String): Boolean {
        return if (field.text.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(field.text).matches()) {
            field.setBackgroundResource(R.drawable.error_border)
            field.error = errorMessage
            false
        } else {
            field.setBackgroundResource(R.drawable.rounded_edittext)
            field.error = null
            true
        }
    }

    private fun addEmailTextChangedListener(field: EditText, requiredMessage: String, formatErrorMessage: String) {
        field.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (field.text.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(field.text).matches()) {
                    field.setBackgroundResource(R.drawable.rounded_edittext)
                    field.error = null
                } else if (field.text.isEmpty()) {
                    field.setBackgroundResource(R.drawable.error_border)
                    field.error = requiredMessage
                } else {
                    field.setBackgroundResource(R.drawable.error_border)
                    field.error = formatErrorMessage
                }
            }
        })
    }
}
