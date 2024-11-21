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
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var verificationCode : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        firebaseAuth = FirebaseAuth.getInstance()

        email = findViewById(R.id.email_address_create_account)
        password = findViewById(R.id.password_create_account)
        confirmPassword = findViewById(R.id.confirm_password_create_account)

        addTextChangedListener(email, "Email Address is required")
        addPasswordTextChangedListener(password, "Password is required and must contains:\n• At least one alphabet\n• At least one number\n• At least one special characer\n• At least 8 digits long")
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
                val emailValue = email.text.toString()
                val passwordValue = password.text.toString()

                firebaseAuth.createUserWithEmailAndPassword(emailValue, passwordValue).addOnCompleteListener{
                    if (it.isSuccessful) {
                        sendEmail(emailValue)
                        val intent = Intent(this, AuthenticationActivity::class.java)
                        intent.putExtra("EMAIL", emailValue)
                        intent.putExtra("PASSWORD", passwordValue)
                        intent.putExtra("CODE", verificationCode)
                        startActivity(intent)
                    }
                    else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // email = cookedgroup34@gmail.com
    // pass = Group34cooked!!
    private fun sendEmail(to: String) {
        verificationCode = generateCode()

        val apiKey = "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"
        val fromEmail = "cookedgroup34@gmail.com"
        val subject = "Verification Code for COOKED"
        val toEmail = to
        val contentText = "$verificationCode is your verification code for COOKED."

        // Create JSON payload for SendGrid API
        val json = JSONObject()
        json.put("from", JSONObject().put("email", fromEmail))
        json.put("subject", subject)
        json.put("personalizations", JSONArray().put(
            JSONObject().put("to", JSONArray().put(JSONObject().put("email", toEmail)))
        ))
        json.put("content", JSONArray().put(
            JSONObject().put("type", "text/plain").put("value", contentText)
        ))

        val client = OkHttpClient()

        val requestBody = RequestBody.create(
            "application/json".toMediaTypeOrNull(),
            json.toString()
        )

        val request = Request.Builder()
            .url("https://api.sendgrid.com/v3/mail/send")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody)
            .build()

        // Execute the request asynchronously
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.e("SendGrid", "Email sent")
                } else {
                    Log.e("SendGrid", "Failed to send email")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                // Handle failure in sending request
                Log.e("SendGrid", "Error sending email: ${e.message}")
                e.printStackTrace()
            }
        })
    }

    private fun generateCode(): String {
        val characters = "0123456789"
        return (1..6)
            .map { characters.random() }
            .joinToString("")
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
