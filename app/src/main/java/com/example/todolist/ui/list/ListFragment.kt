package com.example.todolist.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.FragmentListBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ListFragment : Fragment() {

    companion object {
        fun newInstance() = ListFragment()
    }

    private lateinit var binding: FragmentListBinding
    private val viewModel: ListViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentListBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding.list.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            viewModel.listToDos.collect { setupAdapter(it) }
        }
        viewModel.send(ViewModelEvents.SharedPreferencesResult)

        binding.addElementFab.setOnClickListener {      //ADD A NEW ITEM TO THE LIST
            if (binding.editText.text.isNotEmpty()) {
                viewModel.send(
                    ViewModelEvents.WriteNewTodoItem(
                        binding.editText.text.toString()//SEND WHAT'S INSIDE EDITTEXT TO VIEW MODEL
                    ))
                binding.editText.text.clear()       //CLEAR EDITTEXT
            } else {
                Toast.makeText(     //TOAST IF NOTHING ON EDITTEXT
                    requireContext(),
                    "Insert Something on Top!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return binding.root
    }

    private fun setupAdapter(list: List<TodoItem>) {
        binding.list.adapter = Adapter(list) {      //GIVE THE LIST FROM THE VIEW MODEL
            viewModel.send(ViewModelEvents.DeleteToDoItem(it))  //LAMBDA TO CANCEL ITEMS USED BY
        }                                                       //THE VIEW HOLDER
    }
}