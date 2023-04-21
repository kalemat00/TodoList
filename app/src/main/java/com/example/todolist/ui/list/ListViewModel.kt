package com.example.todolist.ui.list

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


const val KEY_TODO_LIST = "TodoList"
const val KEY_FIRST_TIME_USER = "firstTimeUser"

sealed class ViewModelEvents {      //EVENT LIST
    object SharedPreferencesResult : ViewModelEvents()
    data class WriteNewTodoItem(val itemString: String) : ViewModelEvents()
    data class DeleteToDoItem(val position: Int) : ViewModelEvents()
}

class ListViewModel(private val preferences: SharedPreferences) : ViewModel() {
    init {
        checkFirstTimeUser()
    }

    val listToDos = MutableSharedFlow<List<TodoItem>>()

    fun send(event: ViewModelEvents) {      //EVENT MANAGER
        when (event) {
            ViewModelEvents.SharedPreferencesResult -> getAdapterList()
            is ViewModelEvents.WriteNewTodoItem -> addNewItem(TodoItem(event.itemString))
            is ViewModelEvents.DeleteToDoItem -> deleteSelectedItem(event.position)
        }
    }

    private fun checkFirstTimeUser() {
        if (preferences.getBoolean(KEY_FIRST_TIME_USER, true)) {
            preferences.edit().putBoolean(KEY_FIRST_TIME_USER, false).apply()
        }
    }

    private fun getAdapterList() {      //RETRIEVE THE LIST FROM PREFERENCES
        viewModelScope.launch {
            preferences.getString(KEY_TODO_LIST, null).let {
                listToDos.emit(fromJson(it))
            }
        }
    }

    private fun addNewItem(itemToAdd: TodoItem) {       //ADD ITEM WHILE CHECKING IF THE PREFERENCES ARE EMPTY
        if (preferences.getString(KEY_TODO_LIST, null) != null) {
            preferences.getString(KEY_TODO_LIST, null)?.let {
                val oldList = fromJson(it)
                val newList = mutableListOf(itemToAdd)
                newList.addAll(oldList)
                preferences.edit()
                    .putString(KEY_TODO_LIST, toJson(newList))
                    .apply()
            }
        } else {
            preferences.edit()
                .putString(KEY_TODO_LIST, toJson(listOf(itemToAdd)))
                .apply()
        }
        getAdapterList()
    }

    private fun deleteSelectedItem(itemPosition: Int) {
        preferences.getString(KEY_TODO_LIST, null)?.let {
            val newList = fromJson(it)
            newList.removeAt(itemPosition)
            preferences.edit()
                .putString(KEY_TODO_LIST, toJson(newList))
                .apply()
        }
        getAdapterList()
    }
    //++++++++++ SERIALIZER FROM AND TO JSON ++++++++++
    private fun toJson(list: List<TodoItem>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
    private fun fromJson(json: String?): MutableList<TodoItem> {
        val gson = Gson()
        val type = object : TypeToken<MutableList<TodoItem?>?>() {}.type
        return gson.fromJson(json, type)
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++
}