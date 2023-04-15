package com.example.todolist

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.ui.list.ListViewModel

class ViewModelFactory(
    private val sharedPreferences: SharedPreferences
):  ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            return modelClass
                .getConstructor(SharedPreferences::class.java)
                .newInstance(sharedPreferences)
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
