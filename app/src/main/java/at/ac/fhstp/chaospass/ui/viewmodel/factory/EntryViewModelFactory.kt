package at.ac.fhstp.chaospass.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import at.ac.fhstp.chaospass.data.repository.EntryRepository
import at.ac.fhstp.chaospass.viewmodel.EntryViewModel

class EntryViewModelFactory(private val repository: EntryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EntryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EntryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}