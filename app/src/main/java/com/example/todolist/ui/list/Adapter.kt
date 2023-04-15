package com.example.todolist.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ListItemBinding

class Adapter(
    private val list: List<TodoItem>,
    private val listener: (Int) -> Unit
) : RecyclerView.Adapter<Adapter.ListViewHolder>() {


    inner class ListViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TodoItem) {
            binding.listText.text = item.string
            binding.listButton.setOnClickListener {
                listener(layoutPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}