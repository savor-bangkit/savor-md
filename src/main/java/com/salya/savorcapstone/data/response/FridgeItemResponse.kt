package com.salya.savorcapstone.data.response

import com.google.gson.annotations.SerializedName

data class FridgeItemResponse(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("category")
    val category: String,

    @field:SerializedName("daysCountExpire")
    val daysCountExpire: Int,

    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("createdAt")
    val createdAt: String
)

data class GetAllFridgeItemsResponse(

    @field:SerializedName("items")
    val items: List<FridgeItemResponse>
)
