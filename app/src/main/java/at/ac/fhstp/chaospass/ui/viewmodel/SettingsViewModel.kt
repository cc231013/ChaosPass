package at.ac.fhstp.chaospass.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel : ViewModel() {
    private val _isChaosMode = MutableStateFlow(false)
    val isChaosMode: StateFlow<Boolean> = _isChaosMode

    fun toggleChaosMode(b: Boolean) {
        _isChaosMode.value = !_isChaosMode.value
    }
}
