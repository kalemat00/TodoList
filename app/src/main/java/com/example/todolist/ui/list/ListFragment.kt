package com.example.todolist.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.MyApplication
import com.example.todolist.databinding.FragmentListBinding

class ListFragment() : Fragment() {

    companion object {
        fun newInstance() = ListFragment()
    }

    private lateinit var binding: FragmentListBinding
    private lateinit var viewModel: ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (requireActivity().application as MyApplication).listViewModel.create(ListViewModel::class.java)
        viewModel.send(ViewModelEvents.SharedPreferencesResult)     //RETRIEVE SAVED LIST AS SOON AS
                                                                    //THE FRAGMENT IS CREATED
        binding = FragmentListBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding.list.layoutManager = LinearLayoutManager(requireContext())
        viewModel.listToDoes.observe(viewLifecycleOwner) { setupAdapter(it) }   //LIVEDATA OBSERVER

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