package com.example.todolist

import android.app.Application
import com.example.todolist.di.appModule
import com.example.todolist.di.viewModels
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@MyApplication)
            modules(listOf(appModule, viewModels))
        }
    }
}