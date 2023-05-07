package com.example.todolist.ui.list

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.todolist.ui.list.notelistscreen.KEY_TODO_LIST
import com.example.todolist.ui.list.notelistscreen.TodoItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val KEY_TODO_LIST = "TodoList"
const val KEY_FIRST_TIME_USER = "firstTimeUser"

sealed class NoteCreationEvent {      //EVENT LIST
    data class NoteCreatedSuccessfully(val string: String): NoteCreationEvent()
    object DeleteNote: NoteCreationEvent()

  //object AddTag: NoteCreationEvent()
  //object AddImage: NoteCreationEvent()
  //object AddPhoto: NoteCreationEvent()
  //object ChangeFormat: NoteCreationEvent()
  //object ShareNote: NoteCreationEvent()
  //object AddToFavoriteNote: NoteCreationEvent()
  //object PrintNote: NoteCreationEvent()
}

class NoteCreationViewModel(private val preferences: SharedPreferences): ViewModel() {
    fun send(event: NoteCreationEvent){
        when(event){
            is NoteCreationEvent.NoteCreatedSuccessfully -> addNewNote(TodoItem(event.string))
            NoteCreationEvent.DeleteNote -> TODO()
        }
    }

    private fun addNewNote(itemToAdd: TodoItem) {
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