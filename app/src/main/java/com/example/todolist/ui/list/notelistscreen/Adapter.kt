package com.example.todolist.ui.list.notelistscreen

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ListItemBinding

class Adapter(
    private val list: List<TodoItem>,
    private val longListener: () -> Unit,
    private val allCheckedListener: (allChecked: Boolean) -> Unit,
    private val deleteListener: (somethingChecked: Boolean) -> Unit
) : RecyclerView.Adapter<Adapter.ListViewHolder>() {
    private var selectionActive: Boolean = false
    private val _checkedItemsPosition: MutableList<Int> = mutableListOf()
    fun checkedItemsPosition(): List<Int> = _checkedItemsPosition
    @SuppressLint("NotifyDataSetChanged")
    fun stopSelection() {
        selectionActive = false
        uncheckAllItems()
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun uncheckAllItems() {
        _checkedItemsPosition.clear()
        deleteListener(false)
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun checkAllItems(){
        _checkedItemsPosition.clear()
        _checkedItemsPosition.addAll((0 until this@Adapter.itemCount).toMutableList())
        deleteListener(true)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bind(item: TodoItem) {
            binding.apply {
                listText.text = item.string
                checkBox.visibility = View.GONE
                checkBox.setOnClickListener {
                    addToCheckedList()
                }
            }
            itemView.setOnLongClickListener {
                selectionActive = true
                binding.checkBox.isChecked = true
                addToCheckedList()
                notifyDataSetChanged()
                longListener()
                true
            }
        }
        fun getCheckBoxShowed() = binding.checkBox.apply {
            visibility = View.VISIBLE
        }
        fun setChecked(checked: Boolean) = binding.checkBox.apply {
            isChecked = checked
            jumpDrawablesToCurrentState()
        }
        private fun addToCheckedList() {
            if (!(_checkedItemsPosition.contains(this@ListViewHolder.adapterPosition))) {
                _checkedItemsPosition.add(this@ListViewHolder.adapterPosition)
                if (_checkedItemsPosition.size == this@Adapter.itemCount) allCheckedListener(true)
                deleteListener(true)
            } else if (_checkedItemsPosition.contains(this@ListViewHolder.adapterPosition)) {
                _checkedItemsPosition.remove(this@ListViewHolder.adapterPosition)
                if (_checkedItemsPosition.size != this@Adapter.itemCount) {
                    allCheckedListener(false)
                    if(_checkedItemsPosition.isEmpty()) deleteListener(false)
                    else deleteListener(true)
                }
            }
            //Log.e("CheckedItems:", "$_checkedItemsPosition")
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        longListener
        return ListViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        if (selectionActive) {
            holder.bind(list[position])
            holder.getCheckBoxShowed()
        } else {
            holder.bind(list[position])
        }
    }
    override fun getItemCount(): Int = list.size
    override fun onViewAttachedToWindow(holder: ListViewHolder) {
        if (selectionActive) {
            holder.getCheckBoxShowed()
            holder.setChecked(_checkedItemsPosition.contains(holder.adapterPosition))
        }
        super.onViewAttachedToWindow(holder)
    }
    override fun onViewRecycled(holder: ListViewHolder) {
        Log.e("recycled:", "${holder.adapterPosition}")
        super.onViewRecycled(holder)
    }
}