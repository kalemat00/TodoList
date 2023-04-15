package com.example.todolist

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class MyApplication: Application() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var listViewModel: ViewModelFactory
    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences("app", Context.MODE_PRIVATE)
        listViewModel = ViewModelFactory(sharedPreferences)
    }
}