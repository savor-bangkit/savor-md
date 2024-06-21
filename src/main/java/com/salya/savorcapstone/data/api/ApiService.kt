package com.salya.savorcapstone.data.api

import com.salya.savorcapstone.data.response.CreateFridgeItemRequest
import com.salya.savorcapstone.data.response.CreateFridgeItemResponse
import com.salya.savorcapstone.data.response.DeleteFridgeItemResponse
import com.salya.savorcapstone.data.response.FridgeItemResponse
import com.salya.savorcapstone.data.response.GetByIdFridgeItemResponse
import com.salya.savorcapstone.data.response.GetUpcomingExpirationResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("fridge-item")
    suspend fun createFridgeItem(
        @Header("Authorization") token: String,
        @Body request: CreateFridgeItemRequest
    ): CreateFridgeItemResponse

    @GET("fridge-item")
    suspend fun getAllFridgeItems(
        @Header("Authorization") token: String
    ): List<FridgeItemResponse>

    @GET("fridge-item/{id}")
    suspend fun getFridgeItemById(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): GetByIdFridgeItemResponse

    @GET("/fridge-item?upcoming=true")
    suspend fun getUpcomingExpirations(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): GetUpcomingExpirationResponse

    @DELETE("fridge-item/{id}")
    suspend fun deleteFridgeItem(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): DeleteFridgeItemResponse
}