package com.example.govix.core.data.remote

import com.example.govix.data.remote.dto.AuthWrappedResponse
import com.example.govix.data.remote.dto.ChangePasswordRequest
import com.example.govix.data.remote.dto.LoginRequest
import com.example.govix.data.remote.dto.RegisterRequest
import com.example.govix.data.remote.dto.SocialLoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface GovixAuthApi {

    @POST("api/auth/login")
    suspend fun login(@Body body: LoginRequest): Response<AuthWrappedResponse>

    @POST("api/auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<AuthWrappedResponse>

    @POST("api/auth/social-login")
    suspend fun socialLogin(@Body body: SocialLoginRequest): Response<AuthWrappedResponse>

    @PUT("api/auth/change-password")
    suspend fun changePassword(@Body body: ChangePasswordRequest): Response<AuthWrappedResponse>

    @POST("api/auth/logout")
    suspend fun logout(): Response<AuthWrappedResponse>
}
