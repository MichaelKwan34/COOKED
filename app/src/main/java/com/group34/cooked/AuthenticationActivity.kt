package com.group34.cooked

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher

class AuthenticationActivity : AppCompatActivity() {
    private lateinit var firstCode: EditText
    private lateinit var secondCode: EditText
    private lateinit var thirdCode: EditText
    private lateinit var forthCode: EditText
    private lateinit var fifthCode: EditText
    private lateinit var sixthCode: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        firstCode = findViewById(R.id.first_code)
        secondCode = findViewById(R.id.second_code)
        thirdCode = findViewById(R.id.third_code)
        forthCode = findViewById(R.id.forth_code)
        fifthCode = findViewById(R.id.fifth_code)
        sixthCode = findViewById(R.id.sixth_code)

        addTextWatcher(firstCode, secondCode)
        addTextWatcher(secondCode, thirdCode)
        addTextWatcher(thirdCode, forthCode)
        addTextWatcher(forthCode, fifthCode)
        addTextWatcher(fifthCode, sixthCode)
        addTextWatcher(sixthCode)

        setFocusListener(firstCode)
        setFocusListener(secondCode)
        setFocusListener(thirdCode)
        setFocusListener(forthCode)
        setFocusListener(fifthCode)
        setFocusListener(sixthCode)

        val back: Button = findViewById(R.id.back_authentication)
        back.setOnClickListener {
            finish()
        }

        val confirm: Button = findViewById(R.id.confirm_authentication)
        confirm.setOnClickListener {
            if (validateCode()) {

            } else {

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
            fifthCode -> forthCode.requestFocus()
            forthCode -> thirdCode.requestFocus()
            thirdCode -> secondCode.requestFocus()
            secondCode -> firstCode.requestFocus()
        }
    }

    private fun areAllFieldsEmpty(): Boolean {
        return firstCode.text.isEmpty() && secondCode.text.isEmpty() &&
                thirdCode.text.isEmpty() && forthCode.text.isEmpty() &&
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
            forthCode.text.isEmpty() -> {
                forthCode.setBackgroundResource(R.drawable.error_border)
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
        forthCode.setBackgroundResource(R.drawable.rounded_edittext)
        fifthCode.setBackgroundResource(R.drawable.rounded_edittext)
        sixthCode.setBackgroundResource(R.drawable.rounded_edittext)
    }

}