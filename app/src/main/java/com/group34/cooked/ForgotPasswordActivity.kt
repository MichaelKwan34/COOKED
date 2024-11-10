package com.group34.cooked

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var email: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        email = findViewById(R.id.email_address_forgot_password)

        addEmailTextChangedListener(email, "Email Address is required", "Invalid email format")

        val back: Button = findViewById(R.id.back_forgot_password)
        back.setOnClickListener {
            finish()
        }

        val resetPassword: Button = findViewById(R.id.reset_password)
        resetPassword.setOnClickListener {
            val isEmailValid = validateEmail(email, "Invalid email format")

            if (isEmailValid) {
                val intent = Intent(this, AuthenticationActivity::class.java)
                startActivity(intent)
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
