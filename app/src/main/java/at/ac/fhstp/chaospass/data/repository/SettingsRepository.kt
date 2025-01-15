package at.ac.fhstp.chaospass.data.repository

import SettingsDataStore
import kotlinx.coroutines.flow.Flow


class SettingsRepository(private val dataStore: SettingsDataStore) {
    val isChaosMode: Flow<Boolean> = dataStore.isChaosMode

    suspend fun setChaosMode(enabled: Boolean) {
        dataStore.setChaosMode(enabled)
    }
}
