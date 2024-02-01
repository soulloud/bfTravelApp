import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.beaconfire.travel.mallApplication
import com.beaconfire.travel.profile.ProfileUiModel
import com.beaconfire.travel.profile.ProfileUiModelStatus
import com.beaconfire.travel.repo.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    // Change the type from User? to Profile?
    private val _profileUiModel =
        MutableStateFlow(ProfileUiModel(status = ProfileUiModelStatus.None, null))
    val profileUiModel: StateFlow<ProfileUiModel> = _profileUiModel.asStateFlow()

    init {
        fetchUserProfile()
    }

    fun onImageCaptured(uri: Uri) {
        _profileUiModel.update { it.copy(capturedImageUri = uri) }
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


    companion object {
        private val TAG = ProfileViewModel::class.java.simpleName

        val Factory = viewModelFactory {
            initializer {
                ProfileViewModel(mallApplication().container.userRepository)
            }
        }
    }
}
