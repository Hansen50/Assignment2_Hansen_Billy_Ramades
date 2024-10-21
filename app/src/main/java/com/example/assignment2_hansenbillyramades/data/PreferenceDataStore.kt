package com.example.assignment2_hansenbillyramades.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

// konteks untuk mengakses DataStore dari konteks mana pun dengan mudah, menggunakan nama "user_preference"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preference")


// Di siini objek berfungsi sebagai menyimpan status apakah user sudah melihat onboard atau belum
object DataStoreConstant {
    val IS_WELCOME = booleanPreferencesKey("IS_WELCOME")
}

class PreferenceDataStore private constructor(private val dataStore: DataStore<Preferences>) {

    // Mengaset is welcome Jika isWelcome bernilai true, itu berarti pengguna telah melihat sambutan.
    suspend fun setIsWelcome(isWelcome: Boolean) {
        dataStore.edit { preferences ->
            preferences[DataStoreConstant.IS_WELCOME] = isWelcome
        }
    }


    // Mengambil onboard dari DataStore. Jika belum ada, maka mengembalikan false.
    suspend fun getIsWelcome(): Boolean {
        return withContext(Dispatchers.IO) {
            dataStore.data.first()[DataStoreConstant.IS_WELCOME] ?: false
        }
    }

    // memastikan bahwa perubahan pada INSTANCE terlihat oleh semua thread, sehingga mencegah masalah konsistensi
    companion object {
        @Volatile
        private var INSTANCE: PreferenceDataStore? = null

        fun getInstance(dataStore: DataStore<Preferences>): PreferenceDataStore {
            return INSTANCE ?: synchronized(this) {
                val instance = PreferenceDataStore(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}

// menggunakan DataStore untuk menyimpan status sambutan user.
// Dengan menggunakan lifecycle scope (coroutines) dan pola singleton,