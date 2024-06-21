package com.salya.savorcapstone.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salya.savorcapstone.data.api.ApiService
import com.salya.savorcapstone.data.response.CreateFridgeItemRequest
import com.salya.savorcapstone.data.response.FridgeItemResponse
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FridgeItemViewModel(private val apiService: ApiService, private val authToken: String) : ViewModel() {

    private val _fridgeItems = MutableLiveData<List<FridgeItemResponse>>()
    val fridgeItems: LiveData<List<FridgeItemResponse>> get() = _fridgeItems

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        fetchFridgeItems()
    }

    private fun fetchFridgeItems() {
        viewModelScope.launch {
            try {
                val response = apiService.getAllFridgeItems("Bearer $authToken")
                _fridgeItems.value = response
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun addFridgeItem(category: String) {
        viewModelScope.launch {
            try {
                val request = CreateFridgeItemRequest(category)
                val response = apiService.createFridgeItem("Bearer $authToken", request)
                fetchFridgeItems()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteFridgeItem(itemId: String) {
        viewModelScope.launch {
            try {
                apiService.deleteFridgeItem(itemId, "Bearer $authToken")
                fetchFridgeItems()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}

