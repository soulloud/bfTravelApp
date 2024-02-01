import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.beaconfire.travel.mallApplication
import com.beaconfire.travel.profile.ProfileUiModel
import com.beaconfire.travel.profile.ProfileUiModelStatus
import com.beaconfire.travel.repo.ProfileRepository
import com.beaconfire.travel.repo.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    // Change the type from User? to Profile?
    private val _profileUiModel =
        MutableStateFlow(ProfileUiModel(status = ProfileUiModelStatus.None, null))
    val profileUiModel: StateFlow<ProfileUiModel> = _profileUiModel.asStateFlow()

    init {
        fetchUserProfile()
    }

    private fun fetchUserProfile() {
        _profileUiModel.update { it.copy(ProfileUiModelStatus.Loading) }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val user = userRepository.getLoginUser()
                if (user != null) {
                    _profileUiModel.update {
                        it.copy(
                            status = ProfileUiModelStatus.LoadSucceed,
                            profile = user.profile
                        )
                    }
                } else {
                    _profileUiModel.update { it.copy(ProfileUiModelStatus.LoadFailed) }
                }
            }
        }
    }

    fun updateFullName(newFullName: String) {
        viewModelScope.launch {
            val profileId = userRepository.getLoginUser()?.profile?.profileId
            if (profileId != null) {
                profileRepository.updateFullName(profileId, newFullName)
                fetchUserProfile() // Refresh the profile data
            } else {
                Log.e(TAG, "Profile ID is null")
            }
        }
    }

    fun updateLocation(newLocation: String) {
        viewModelScope.launch {
            val profileId = userRepository.getLoginUser()?.profile?.profileId
            if (profileId != null) {
                profileRepository.updateLocation(profileId, newLocation)
                fetchUserProfile() // Refresh the profile data
            } else {
                Log.e(TAG, "Profile ID is null")
            }
        }
    }

    fun updateAboutYou(newAboutYou: String) {
        viewModelScope.launch {
            val profileId = userRepository.getLoginUser()?.profile?.profileId
            if (profileId != null) {
                profileRepository.updateAboutYou(profileId, newAboutYou)
                fetchUserProfile() // Refresh the profile data
            } else {
                Log.e(TAG, "Profile ID is null")
            }
        }
    }



    companion object {
        private val TAG = ProfileViewModel::class.java.simpleName

        val Factory = viewModelFactory {
            initializer {
                ProfileViewModel(mallApplication().container.userRepository,
                    mallApplication().container.profileRepository)
            }
        }
    }
}