import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsDataStore(context: Context) {
    private val dataStore = context.dataStore

    companion object {
        private val CHAOS_MODE_KEY = booleanPreferencesKey("chaos_mode")
    }

    val isChaosMode: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[CHAOS_MODE_KEY] ?: false
    }

    suspend fun setChaosMode(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[CHAOS_MODE_KEY] = enabled
        }
    }
}
