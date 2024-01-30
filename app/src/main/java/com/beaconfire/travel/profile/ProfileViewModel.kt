import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beaconfire.travel.repo.ProfileRepository
import com.beaconfire.travel.repo.model.Profile // Make sure this is imported
import com.beaconfire.travel.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    // Change the type from User? to Profile?
    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile.asStateFlow()

    init {
        fetchUserProfile()
    }

    private fun fetchUserProfile() {
        SessionManager.userId?.let { userId ->
            viewModelScope.launch {
                val profile = repository.getProfileForUser(userId)
                _profile.value = profile
            }
        }
    }
}
