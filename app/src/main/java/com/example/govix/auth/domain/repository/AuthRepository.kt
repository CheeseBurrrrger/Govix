package com.example.govix.auth.domain.repository

import com.example.govix.core.data.TokenDataStore
import com.example.govix.core.data.remote.GovixAuthApi
import com.example.govix.data.remote.dto.AuthWrappedResponse
import com.example.govix.data.remote.dto.ChangePasswordRequest
import com.example.govix.data.remote.dto.LoginRequest
import com.example.govix.data.remote.dto.RegisterRequest
import com.example.govix.data.remote.dto.SimpleErrorBody
import com.example.govix.data.remote.dto.SocialLoginRequest
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class AuthRepository(
    private val api: GovixAuthApi,
    private val tokenDataStore: TokenDataStore,
    private val gson: Gson = Gson(),
) {

    suspend fun login(email: String, password: String): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val response = api.login(LoginRequest(email.trim(), password))
            persistLoginLikeResponse(response, requireToken = true).getOrThrow()
            Unit
        }
    }

    suspend fun register(body: RegisterRequest): Result<Boolean> = withContext(Dispatchers.IO) {
        runCatching {
            val response = api.register(body)
            persistLoginLikeResponse(response, requireToken = false).getOrThrow()
        }
    }

    suspend fun socialLogin(provider: String, idToken: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                val response = api.socialLogin(SocialLoginRequest(provider, idToken))
                persistLoginLikeResponse(response, requireToken = true).getOrThrow()
                Unit
            }
        }

    suspend fun changePassword(oldPassword: String, newPassword: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                val response = api.changePassword(
                    ChangePasswordRequest(oldPassword = oldPassword, newPassword = newPassword),
                )
                ensureHttpOk(response).getOrThrow()
            }
        }

    suspend fun logout(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            runCatching { api.logout() }
            tokenDataStore.clearToken()
            Result.success(Unit)
        } catch (e: Exception) {
            tokenDataStore.clearToken()
            Result.failure(e)
        }
    }

    private suspend fun persistLoginLikeResponse(
        response: Response<AuthWrappedResponse>,
        requireToken: Boolean,
    ): Result<Boolean> {
        if (!response.isSuccessful) {
            return Result.failure(Exception(errorMessage(response)))
        }
        val body = response.body()
            ?: return Result.failure(Exception("Respons kosong."))
        if (body.success == false) {
            return Result.failure(Exception(body.message ?: "Permintaan ditolak."))
        }
        val token = body.data?.token?.takeIf { it.isNotBlank() }
        return when {
            token != null -> {
                tokenDataStore.saveToken(token)
                Result.success(true)
            }
            requireToken -> Result.failure(Exception("Respons tidak berisi token."))
            else -> Result.success(false)
        }
    }

    private fun ensureHttpOk(response: Response<AuthWrappedResponse>): Result<Unit> {
        if (response.isSuccessful) return Result.success(Unit)
        return Result.failure(Exception(errorMessage(response)))
    }

    private fun errorMessage(response: Response<*>): String {
        val err = response.errorBody()?.string()
        if (!err.isNullOrBlank()) {
            try {
                val parsed = gson.fromJson(err, SimpleErrorBody::class.java)
                val m = parsed.message ?: parsed.error
                if (!m.isNullOrBlank()) return m
            } catch (_: Exception) {  }
            return err
        }
        return "HTTP ${response.code()}"
    }
}