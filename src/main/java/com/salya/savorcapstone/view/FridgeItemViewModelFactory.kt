package com.salya.savorcapstone.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.salya.savorcapstone.data.api.ApiService

class FridgeItemViewModelFactory(private val apiService: ApiService, private val authToken: String) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FridgeItemViewModel::class.java)) {
            return FridgeItemViewModel(apiService, authToken) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

