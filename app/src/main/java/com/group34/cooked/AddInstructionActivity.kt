package com.group34.cooked

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.group34.cooked.models.Instruction

class AddInstructionActivity : AppCompatActivity(R.layout.activity_add_instruction) {
    private lateinit var etInstruction: EditText
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        etInstruction = findViewById(R.id.add_instruction)
        btnSave = findViewById(R.id.save_instruction)

        btnSave.setOnClickListener {
            val instructionText = etInstruction.text.toString().trim()
            val stepNumber = intent.getIntExtra("stepNumber", 1)

            if (instructionText.isNotBlank()) {
                val instruction = Instruction(stepNumber, instructionText)
                val intent = Intent()
                intent.putExtra("instruction", instruction)
                setResult(RESULT_OK, intent)
                finish()
            } else {
                etInstruction.error = "Instruction is required"
            }
        }
    }
}