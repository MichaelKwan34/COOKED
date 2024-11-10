package com.group34.cooked

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        email = findViewById(R.id.email_address_create_account)
        password = findViewById(R.id.password_create_account)
        confirmPassword = findViewById(R.id.confirm_password_create_account)

        addTextChangedListener(email, "Email Address is required")
        addPasswordTextChangedListener(password, "Password is required")
        addConfirmPasswordTextChangedListener(confirmPassword, "Confirm Password is required")

        val back: Button = findViewById(R.id.back_create_account)
        back.setOnClickListener {
            finish()
        }

        val createAccount: Button = findViewById(R.id.create_account_button)
        createAccount.setOnClickListener {
            val isEmailValid = validateEmail(email, "Invalid email format")
            val isPasswordValid = validatePassword(password, "Password must be at least 8 characters, include a number and a special character")
            val isConfirmPasswordValid = validateField(confirmPassword, "Confirm Password is required", password.text.toString())

            if (isEmailValid && isPasswordValid && isConfirmPasswordValid) {
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

    private fun validatePassword(field: EditText, errorMessage: String): Boolean {
        val passwordPattern = Regex("^(?=.*[0-9])(?=.*[!@#\$%^&*()_+{}:;'?/>.<,])(?=.*[a-zA-Z]).{8,}\$")
        return if (field.text.isEmpty() || !passwordPattern.containsMatchIn(field.text)) {
            field.setBackgroundResource(R.drawable.error_border)
            field.error = errorMessage
            false
        } else {
            field.setBackgroundResource(R.drawable.rounded_edittext)
            field.error = null
            true
        }
    }

    private fun validateField(field: EditText, errorMessage: String, matchText: String? = null): Boolean {
        return when {
            field.text.isEmpty() -> {
                field.setBackgroundResource(R.drawable.error_border)
                field.error = errorMessage
                false
            }
            matchText != null && field.text.toString() != matchText -> {
                field.setBackgroundResource(R.drawable.error_border)
                field.error = "Password and Confirm Password do not match"
                false
            }
            else -> {
                field.setBackgroundResource(R.drawable.rounded_edittext)
                field.error = null
                true
            }
        }
    }

    private fun addTextChangedListener(field: EditText, errorMessage: String) {
        field.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (field.text.isNotEmpty() && (field != email || Patterns.EMAIL_ADDRESS.matcher(field.text).matches())) {
                    field.setBackgroundResource(R.drawable.rounded_edittext)
                    field.error = null
                } else {
                    field.setBackgroundResource(R.drawable.error_border)
                    field.error = errorMessage
                }
            }
        })
    }

    private fun addPasswordTextChangedListener(field: EditText, errorMessage: String) {
        field.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val passwordPattern = Regex("^(?=.*[0-9])(?=.*[!@#\$%^&*()_+{}:;'?/>.<,])(?=.*[a-zA-Z]).{8,}\$")
                if (passwordPattern.containsMatchIn(field.text)) {
                    field.setBackgroundResource(R.drawable.rounded_edittext)
                    field.error = null
                } else {
                    field.setBackgroundResource(R.drawable.error_border)
                    field.error = errorMessage
                }
            }
        })
    }

    private fun addConfirmPasswordTextChangedListener(field: EditText, errorMessage: String) {
        val confirmPasswordWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (field.text.isEmpty()) {
                    field.setBackgroundResource(R.drawable.error_border)
                    field.error = errorMessage
                } else if (field.text.toString() != password.text.toString()) {
                    field.setBackgroundResource(R.drawable.error_border)
                    field.error = "Password and Confirm Password do not match"
                } else {
                    field.setBackgroundResource(R.drawable.rounded_edittext)
                    field.error = null
                }
            }
        }

        field.addTextChangedListener(confirmPasswordWatcher)
        password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                confirmPasswordWatcher.afterTextChanged(confirmPassword.text)
            }
        })
    }
}
