package com.example.todolist.ui.list.notelistscreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.databinding.FragmentListBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private val viewModel: ListViewModel by inject()
    private lateinit var adapter: Adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentListBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding.list.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            viewModel.listToDos.collect {
                setupAdapter(it)
                binding.list.adapter = adapter
            }
        }
        viewModel.send(ViewModelEvents.SharedPreferencesResult)

        setupTopBarMenu()
        setupAddNoteButton()

        return binding.root
    }

    private fun setupAddNoteButton() {
        binding.apply {
            addNoteOrDeleteAllSelectedItem.apply {
                setImageResource(R.drawable.baseline_add_24)
                setOnClickListener {
                    findNavController().navigate(R.id.action_listFragment_to_noteCreationFragment)
                }
            }
            selectOrDeselectAllButton.apply {
                visibility = View.GONE
            }
        }
    }

    private fun setupTopBarMenu() {
        binding.apply {
            burgerAndStopSelectionButton.apply {
                setImageResource(R.drawable.baseline_view_headline_24)
                setOnClickListener {
                    //todo insert logic for drawer menu
                }
            }
            squaresButton.apply {
                setImageResource(R.drawable.baseline_squares_24)
                visibility = View.VISIBLE
                setOnClickListener {
                    //todo insert logic to change adapter
                }
            }
            middleText.text = requireContext().getString(R.string.all_notes)
        }
    }

    private fun changeTopBarMenu() {
        binding.apply {
            burgerAndStopSelectionButton.setImageResource(R.drawable.baseline_clear_24)
            burgerAndStopSelectionButton.setOnClickListener {
                setupTopBarMenu()
                adapter.stopSelection()
                setupAddNoteButton()
            }
            squaresButton.visibility = View.INVISIBLE
            middleText.text = requireContext().getString(R.string.selected)
        }
    }

    private fun setupAdapter(list: List<TodoItem>) {
        adapter = Adapter(list, {
            setupSelectOrDeselectAllButton()
            changeTopBarMenu()
        }, { allChecked ->
            if (allChecked) {
                binding.selectOrDeselectAllButton.setImageResource(R.drawable.baseline_all_selected_24)
            } else {
                binding.selectOrDeselectAllButton.setImageResource(R.drawable.baseline_select_all_24)
            }
        }, { somethingChecked ->
            setupDeleteButton(somethingChecked)
        })
    }

    //SETUP HIDDEN BUTTON
    private fun setupSelectOrDeselectAllButton() {
        binding.selectOrDeselectAllButton.apply {
            visibility = View.VISIBLE
            isClickable = true
            isFocusable = true
            setOnClickListener {
                Log.e("selectAll", "${adapter.checkedItemsPosition().size}")
                if (adapter.checkedItemsPosition().size == adapter.itemCount) {
                    adapter.uncheckAllItems()
                    setImageResource(R.drawable.baseline_select_all_24)
                } else {
                    adapter.checkAllItems()
                    setImageResource(R.drawable.baseline_all_selected_24)
                }
            }
        }
    }

    //SETUP BUTTON THAT'S ALWAYS VISIBLE
    private fun setupDeleteButton(somethingChecked: Boolean) {
        binding.addNoteOrDeleteAllSelectedItem.apply {
            if (somethingChecked) {
                isEnabled = true
                alpha = 1f
                setImageResource(R.drawable.baseline_delete_24)
                setOnClickListener {
                    for (i: Int in 0..adapter.itemCount) {
                        Log.e("wewe", "$i")
                    }
                    adapter.checkedItemsPosition().let {
                        viewModel.send(ViewModelEvents.DeleteToDoItem(it))
                    }
                    setupTopBarMenu()
                    setupAddNoteButton()
                }
            } else {
                setImageResource(R.drawable.baseline_delete_24)
                alpha = 0.5f
                isEnabled = false
            }
        }
    }
}