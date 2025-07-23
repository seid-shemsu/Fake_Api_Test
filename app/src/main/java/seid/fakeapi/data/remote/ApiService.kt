package seid.fakeapi.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import seid.fakeapi.model.auth.LoginRequest
import seid.fakeapi.model.auth.LoginResponse
import seid.fakeapi.model.product.Product

interface ApiService {
    @GET("products")
    suspend fun getProducts(): List<Product>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}