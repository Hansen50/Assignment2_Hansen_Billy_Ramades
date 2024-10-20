package com.example.assignment2_hansenbillyramades

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preference")

object DataStoreConstant {
    val IS_WELCOME = booleanPreferencesKey("IS_WELCOME")
}
class PreferenceDataStore private  constructor(private val dataStore: DataStore<Preferences>){

    suspend fun setIsWelcome(isWelcome: Boolean) {
        dataStore.edit { preferences ->
            preferences[DataStoreConstant.IS_WELCOME] = isWelcome
        }
    }

    suspend fun getIsWelcome() : Boolean {
        return  withContext(Dispatchers.IO) {
            dataStore.data.first()[DataStoreConstant.IS_WELCOME] ?: false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: PreferenceDataStore? = null

        fun getInstance(dataStore: DataStore<Preferences>) : PreferenceDataStore {
            return INSTANCE ?: synchronized(this) {
                val instance = PreferenceDataStore(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}