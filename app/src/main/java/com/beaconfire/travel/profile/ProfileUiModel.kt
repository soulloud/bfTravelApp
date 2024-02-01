package com.beaconfire.travel.profile

import android.net.Uri
import com.beaconfire.travel.repo.model.Profile

enum class ProfileUiModelStatus {
    Loading,
    LoadSucceed,
    LoadFailed,
    None
}

data class ProfileUiModel(
    val status: ProfileUiModelStatus,
    val profile: Profile?,
    val capturedImageUri: Uri? = Uri.EMPTY,
    val assetFileName: String? = null,
)
