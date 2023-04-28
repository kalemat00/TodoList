package com.example.todolist.di

import android.content.Context
import com.example.todolist.ui.list.ListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { androidContext().getSharedPreferences("app", Context.MODE_PRIVATE) }
}

val viewModels = module {
    viewModel{ListViewModel(preferences = get())}
}