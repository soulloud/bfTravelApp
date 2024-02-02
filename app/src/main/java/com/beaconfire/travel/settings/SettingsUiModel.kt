package com.beaconfire.travel.settings

import android.net.Uri
import com.beaconfire.travel.repo.model.User

data class SettingsUiModel(
    val user: User? = null,
    val profilePhotoUri: Uri? = null,
)
