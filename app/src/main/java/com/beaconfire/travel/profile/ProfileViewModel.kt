import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.beaconfire.travel.AppContainer
import com.beaconfire.travel.mallApplication
import com.beaconfire.travel.profile.ProfileUiModel
import com.beaconfire.travel.profile.ProfileUiModelStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(
    private val appContainer: AppContainer
) : ViewModel() {

    // Change the type from User? to Profile?
    private val _profileUiModel =
        MutableStateFlow(ProfileUiModel(status = ProfileUiModelStatus.None, null))
    val profileUiModel: StateFlow<ProfileUiModel> = _profileUiModel.asStateFlow()

    init {
        fetchUserProfile()
    }

    fun onImageCaptured(uri: Uri) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                appContainer.assetRepository.uploadImageAsset(uri)?.let { filename ->
                    appContainer.assetRepository.fetchImageAsset(filename)
                        ?.let { assetUri ->
                            _profileUiModel.update {
                                it.copy(capturedImageUri = assetUri)
                            }
                        }
                }
            }
        }
    }

    private fun fetchUserProfile() {
        _profileUiModel.update { it.copy(ProfileUiModelStatus.Loading) }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val user = appContainer.userRepository.getLoginUser()
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
                ProfileViewModel(mallApplication().container)
            }
        }
    }
}
