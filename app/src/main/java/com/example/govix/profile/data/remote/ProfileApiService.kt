package com.example.govix.profile.data.remote

import com.example.govix.profile.data.dto.ProfileResponseDto
import com.example.govix.profile.data.dto.UpdateProfileRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface ProfileApiService {
    @GET("api/users/me")
    suspend fun getProfile(): ProfileResponseDto

    @PUT("api/users/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequestDto): ProfileResponseDto
}