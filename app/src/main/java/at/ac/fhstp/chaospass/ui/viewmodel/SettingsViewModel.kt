package at.ac.fhstp.chaospass.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.ac.fhstp.chaospass.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: SettingsRepository) : ViewModel() {
    private val _isChaosMode = MutableStateFlow(false)
    val isChaosMode: StateFlow<Boolean> = _isChaosMode

    init {
        viewModelScope.launch {
            repository.isChaosMode.collect { chaosMode ->
                _isChaosMode.value = chaosMode
            }
        }
    }

    fun toggleChaosMode(enabled: Boolean? = null) {
        viewModelScope.launch {
            repository.setChaosMode(enabled ?: !_isChaosMode.value)
        }
    }
}

