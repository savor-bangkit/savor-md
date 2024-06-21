package com.salya.savorcapstone.view

import FridgeItemAdapter
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.salya.savorcapstone.R
import com.salya.savorcapstone.data.api.ApiService
import com.salya.savorcapstone.view.FridgeItemViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ViewActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fridgeItemAdapter: FridgeItemAdapter

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://us-central1-savor-be.cloudfunctions.net/api")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)
    private lateinit var viewModel: FridgeItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        recyclerView = findViewById(R.id.recyclerView)

        // Retrieve auth token from SharedPreferences
        val sharedPreferences = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("auth_token", "") ?: ""

        val factory = FridgeItemViewModelFactory(apiService, authToken)
        viewModel = ViewModelProvider(this, factory).get(FridgeItemViewModel::class.java)

        // Initialize ViewModel only once with the factory
        viewModel = ViewModelProvider(this, FridgeItemViewModelFactory(apiService, authToken)).get(FridgeItemViewModel::class.java)

        fridgeItemAdapter = FridgeItemAdapter(emptyList()) { itemId ->
            viewModel.deleteFridgeItem(itemId)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = fridgeItemAdapter

        viewModel.fridgeItems.observe(this) { items ->
            fridgeItemAdapter.updateItems(items)
        }

        viewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                showToast(it)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
