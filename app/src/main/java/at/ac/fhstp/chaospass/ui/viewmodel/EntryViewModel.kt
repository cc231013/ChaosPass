package at.ac.fhstp.chaospass.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.ac.fhstp.chaospass.data.entities.Entry
import at.ac.fhstp.chaospass.data.repository.EntryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class EntryViewModel(private val repository: EntryRepository) : ViewModel() {

    private val _entries = MutableStateFlow<List<Entry>>(emptyList())
    val entries: StateFlow<List<Entry>> = _entries.asStateFlow()

    init {
        fetchEntries()
    }

    fun fetchEntries() {
        viewModelScope.launch {
            val updatedEntries = repository.getAllEntries()
            _entries.value = updatedEntries // This triggers recomposition
            Log.d("EntryViewModel", "Fetched entries: $updatedEntries") // Ensure entries are correct
        }
    }

    fun addEntry(siteName: String, username: String, password: String) {
        viewModelScope.launch {
            repository.addEntry(siteName, username, password)
            fetchEntries() // Automatically fetch the updated list
        }
    }


    fun updateEntry(entry: Entry) {
        viewModelScope.launch {
            repository.updateEntry(entry)
            fetchEntries()
        }
    }

    fun deleteEntry(entry: Entry) {
        viewModelScope.launch {
            repository.deleteEntry(entry)
            fetchEntries()
        }
    }
}
