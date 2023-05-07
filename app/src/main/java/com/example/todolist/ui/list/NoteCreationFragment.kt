package com.example.todolist.ui.list

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.fragment.findNavController
import com.example.todolist.R
import com.example.todolist.databinding.FragmentNoteCreationBinding
import org.koin.android.ext.android.inject

class NoteCreationFragment : Fragment() {
    private lateinit var binding: FragmentNoteCreationBinding
    private val viewModel: NoteCreationViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentNoteCreationBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.editText.setSelection(0)
        binding.doneButton.setOnClickListener{ addNote() }
        binding.returnButton.setOnClickListener{
            addNote()
            findNavController().navigate(R.id.action_noteCreationFragment_to_listFragment)
        }
        return binding.root
    }

    private fun addNote() {
        // ADD A NEW ITEM TO THE LIST
        if (binding.editText.text.isNotEmpty()) {
            viewModel.send(
                NoteCreationEvent.NoteCreatedSuccessfully(
                    binding.editText.text.toString()//SEND WHAT'S INSIDE EDITTEXT TO VIEW MODEL
                )
            )
            binding.editText.text.clear()       //CLEAR EDITTEXT
            binding.root.hideKeyboard()
        } else {
            Toast.makeText(     //TOAST IF NOTHING ON EDITTEXT
                requireContext(),
                "Insert Something!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}