package com.example.govix.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "govix_auth")

class TokenDataStore(context: Context) {

    private val dataStore = context.applicationContext.authDataStore
    private val tokenKey = stringPreferencesKey("jwt_token")

    companion object {
        @Volatile
        private var sharedMemoryToken: String? = null
    }

    private var memoryToken: String?
        get() = sharedMemoryToken
        set(value) {
            sharedMemoryToken = value
        }

    suspend fun hydrate() {
        val token = dataStore.data.map { prefs -> prefs[tokenKey] }.first()
        memoryToken = token
    }

    suspend fun saveToken(token: String) {
        memoryToken = token
        dataStore.edit { it[tokenKey] = token }
    }

    suspend fun clearToken() {
        memoryToken = null
        dataStore.edit { it.remove(tokenKey) }
    }

    fun bearerOrNull(): String? = memoryToken
}
