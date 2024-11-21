package com.group34.cooked

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class AuthenticationActivity : AppCompatActivity() {
    private lateinit var firstCode: EditText
    private lateinit var secondCode: EditText
    private lateinit var thirdCode: EditText
    private lateinit var fourthCode: EditText
    private lateinit var fifthCode: EditText
    private lateinit var sixthCode: EditText

    private lateinit var firebaseAuth: FirebaseAuth
    private var userEmail : String? = null
    private var userPassword : String? = null

    var isVerified = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        firebaseAuth = FirebaseAuth.getInstance()

        userEmail = intent.getStringExtra("EMAIL")
        userPassword = intent.getStringExtra("PASSWORD")
        val verificationCode = intent.getStringExtra("CODE")

        firstCode = findViewById(R.id.first_code)
        secondCode = findViewById(R.id.second_code)
        thirdCode = findViewById(R.id.third_code)
        fourthCode = findViewById(R.id.fourth_code)
        fifthCode = findViewById(R.id.fifth_code)
        sixthCode = findViewById(R.id.sixth_code)

        addTextWatcher(firstCode, secondCode)
        addTextWatcher(secondCode, thirdCode)
        addTextWatcher(thirdCode, fourthCode)
        addTextWatcher(fourthCode, fifthCode)
        addTextWatcher(fifthCode, sixthCode)
        addTextWatcher(sixthCode)

        setFocusListener(firstCode)
        setFocusListener(secondCode)
        setFocusListener(thirdCode)
        setFocusListener(fourthCode)
        setFocusListener(fifthCode)
        setFocusListener(sixthCode)

        val back: Button = findViewById(R.id.back_authentication)
        back.setOnClickListener {
            finish()
        }

        val confirm: Button = findViewById(R.id.confirm_authentication)
        confirm.setOnClickListener {
            if (validateCode()) {
                val inputCode = firstCode.text.toString() + secondCode.text.toString() + thirdCode.text.toString() + fourthCode.text.toString() + fifthCode.text.toString() + sixthCode.text.toString()

                if (inputCode == verificationCode) {
                    isVerified = true
                    Toast.makeText(this, "Email verified!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, LoginActivity::class.java)
                    // Clear all activities, preventing navigation to Authentication and CreateAccount Activities
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                }

                else {
                    firstCode.setBackgroundResource(R.drawable.error_border)
                    secondCode.setBackgroundResource(R.drawable.error_border)
                    thirdCode.setBackgroundResource(R.drawable.error_border)
                    fourthCode.setBackgroundResource(R.drawable.error_border)
                    fifthCode.setBackgroundResource(R.drawable.error_border)
                    sixthCode.setBackgroundResource(R.drawable.error_border)
                    Toast.makeText(this, "Verification code is incorrect", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isVerified == false) {
            firebaseAuth.signInWithEmailAndPassword(userEmail!!, userPassword!!).addOnCompleteListener {
                val user = firebaseAuth.currentUser

                user!!.delete().addOnCompleteListener {
                    if(it.isSuccessful) {
                        Log.e("FirebaseAuth", "$userEmail has been deleted due to unverified email")
                    }
                }
            }
        }
    }

    private fun addTextWatcher(editText: EditText, nextEditText: EditText? = null) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.length == 1 && nextEditText != null) {
                    nextEditText.requestFocus()
                }
                if (s.isNullOrEmpty()) {
                    editText.setBackgroundResource(R.drawable.error_border)
                } else {
                    editText.setBackgroundResource(R.drawable.rounded_edittext)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (before > 0 && s.isNullOrEmpty()) {
                    moveFocusToPreviousField(editText)
                }
            }
        })
    }

    private fun setFocusListener(editText: EditText) {
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if (areAllFieldsEmpty()) {
                    firstCode.requestFocus()
                }
            }
        }
    }

    private fun moveFocusToPreviousField(currentField: EditText) {
        when (currentField) {
            sixthCode -> fifthCode.requestFocus()
            fifthCode -> fourthCode.requestFocus()
            fourthCode -> thirdCode.requestFocus()
            thirdCode -> secondCode.requestFocus()
            secondCode -> firstCode.requestFocus()
        }
    }

    private fun areAllFieldsEmpty(): Boolean {
        return firstCode.text.isEmpty() && secondCode.text.isEmpty() &&
                thirdCode.text.isEmpty() && fourthCode.text.isEmpty() &&
                fifthCode.text.isEmpty() && sixthCode.text.isEmpty()
    }

    private fun validateCode(): Boolean {
        return when {
            firstCode.text.isEmpty() -> {
                firstCode.setBackgroundResource(R.drawable.error_border)
                false
            }
            secondCode.text.isEmpty() -> {
                secondCode.setBackgroundResource(R.drawable.error_border)
                false
            }
            thirdCode.text.isEmpty() -> {
                thirdCode.setBackgroundResource(R.drawable.error_border)
                false
            }
            fourthCode.text.isEmpty() -> {
                fourthCode.setBackgroundResource(R.drawable.error_border)
                false
            }
            fifthCode.text.isEmpty() -> {
                fifthCode.setBackgroundResource(R.drawable.error_border)
                false
            }
            sixthCode.text.isEmpty() -> {
                sixthCode.setBackgroundResource(R.drawable.error_border)
                false
            }
            else -> {
                resetCodeFields()
                true
            }
        }
    }

    private fun resetCodeFields() {
        firstCode.setBackgroundResource(R.drawable.rounded_edittext)
        secondCode.setBackgroundResource(R.drawable.rounded_edittext)
        thirdCode.setBackgroundResource(R.drawable.rounded_edittext)
        fourthCode.setBackgroundResource(R.drawable.rounded_edittext)
        fifthCode.setBackgroundResource(R.drawable.rounded_edittext)
        sixthCode.setBackgroundResource(R.drawable.rounded_edittext)
    }
}