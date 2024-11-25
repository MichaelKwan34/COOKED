package com.group34.cooked.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.group34.cooked.AddInstructionActivity
import com.group34.cooked.ListAdapter
import com.group34.cooked.NewRecipeViewModel
import com.group34.cooked.R
import com.group34.cooked.databinding.FragmentNewRecipeInstructionsBinding
import com.group34.cooked.models.Instruction

class NewRecipeInstructionsFragment : Fragment(R.layout.fragment_new_recipe_instructions) {
    private var _binding: FragmentNewRecipeInstructionsBinding? = null
    private val binding get() = _binding!!

    private lateinit var btnNewInstruction: TextView
    private lateinit var listInstructions: ListView

    private lateinit var newRecipeViewModel: NewRecipeViewModel

    private lateinit var addInstructionLauncher: ActivityResultLauncher<Intent>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewRecipeInstructionsBinding.bind(view)

        btnNewInstruction = binding.newRecipeInstructionAdd
        listInstructions = binding.newRecipeInstructionList

        // Shared view model with activity
        newRecipeViewModel = ViewModelProvider(requireActivity())[NewRecipeViewModel::class.java]

        val instructionListAdapter = ListAdapter(requireContext(), arrayListOf<Instruction>(), newRecipeViewModel)
        listInstructions.adapter = instructionListAdapter

        newRecipeViewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            instructionListAdapter.setList(recipe.instructions)
        }

        btnNewInstruction.setOnClickListener {
            val intent = Intent(requireContext(), AddInstructionActivity::class.java)

            // Add step number to intent
            val stepNumber = newRecipeViewModel.recipe.value?.instructions?.size?.plus(1) ?: 1
            intent.putExtra("stepNumber", stepNumber)

            addInstructionLauncher.launch(intent)
        }

        // Register the launcher to handle the result
        addInstructionLauncher = registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.getParcelableExtra<Instruction>("instruction")?.let { instruction ->
                    newRecipeViewModel.addInstruction(instruction)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        addInstructionLauncher.unregister()
    }
}