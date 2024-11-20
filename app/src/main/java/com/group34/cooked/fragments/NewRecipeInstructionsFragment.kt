package com.group34.cooked.fragments

import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewRecipeInstructionsBinding.bind(view)

        btnNewInstruction = binding.newRecipeInstructionAdd
        listInstructions = binding.newRecipeInstructionList

        // Shared view model with activity
        newRecipeViewModel = ViewModelProvider(requireActivity())[NewRecipeViewModel::class.java]

        val instructionListAdapter = ListAdapter(requireContext(), arrayListOf<Instruction>())
        listInstructions.adapter = instructionListAdapter

        newRecipeViewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            instructionListAdapter.setList(recipe.instructions)
        }

        btnNewInstruction.setOnClickListener {
            // TODO: Validate input and implement
            val instruction = Instruction(1, "Preheat oven to 350°F")
            newRecipeViewModel.addInstruction(instruction)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}