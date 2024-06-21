package com.salya.savorcapstone.data.response

import com.google.gson.annotations.SerializedName

data class CreateFridgeItemRequest(
    @field:SerializedName("category")
    val category: String
)