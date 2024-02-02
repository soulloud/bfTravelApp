import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.beaconfire.travel.AppContainer
import com.beaconfire.travel.destination.ReviewUiModel
import com.beaconfire.travel.destination.ReviewUiState
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

    private val _profileUiModel =
        MutableStateFlow(ProfileUiModel(status = ProfileUiModelStatus.None, null))
    val profileUiModel: StateFlow<ProfileUiModel> = _profileUiModel.asStateFlow()

    private val _reviewUiModel =
        MutableStateFlow(ReviewUiModel(reviewUiState = ReviewUiState.None))
    val reviewUiModel = _reviewUiModel.asStateFlow()

    init {
        fetchUserProfile()
        loadReviews()
    }

    fun onImageCaptured(uri: Uri) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                appContainer.assetRepository.uploadImageAsset(uri)?.let { filename ->
                    _profileUiModel.update {
                        it.copy(assetFileName = filename)
                    }
                }
                _profileUiModel.value.assetFileName?.let { loadAssetForProfileImage(it) }
            }
        }
    }

    fun setAsProfilePhoto() {
        val uiModel = _profileUiModel.value
        uiModel.assetFileName?.let { assetFileName ->
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    uiModel.profile?.copy(photoImage = assetFileName)?.let { updatedProfile ->
                        val succeed = appContainer.profileRepository.updateProfile(updatedProfile)
                        if (succeed) {
                            appContainer.userRepository.getLoginUser()
                                ?.let { loadAssetForProfileImage(it.profile.photoImage) }
                        }
                    }
                }
            }
        }
    }

    fun updateName(newName: String){
        viewModelScope.launch {
            val profile = _profileUiModel.value.profile!!.copy(fullName = newName)
            appContainer.profileRepository.updateProfile(profile)
            fetchUserProfile()
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
                    if (user.profile.photoImage.isNotBlank()) {
                        loadAssetForProfileImage(user.profile.photoImage)
                    }
                } else {
                    _profileUiModel.update { it.copy(ProfileUiModelStatus.LoadFailed) }
                }
            }
        }
    }

    private fun loadReviews(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val user = appContainer.userRepository.getLoginUser()
                if (user != null){
                    val reviews = appContainer.reviewRepository.getAllReviewsOfCurrentUser()
                    _reviewUiModel.update {
                        it.copy(
                            user = user,
                            reviews = reviews,
                            reviewUiState = ReviewUiState.LoadSucceedByUser
                        )
                    }
                } else {
                    _reviewUiModel.update {
                        it.copy(reviewUiState = ReviewUiState.LoadFailed)
                    }
                }
            }
        }
    }

    private suspend fun loadAssetForProfileImage(filename: String) {
        appContainer.assetRepository.fetchImageAsset(filename)
            ?.let { assetUri -> _profileUiModel.update { it.copy(capturedImageUri = assetUri) } }
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
