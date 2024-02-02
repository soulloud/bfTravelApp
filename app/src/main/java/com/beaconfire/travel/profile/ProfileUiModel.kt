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
        //Uri.parse("https://s2.loli.net/2024/02/03/eSk3p1TbN9ZrOWP.png"),
    val assetFileName: String? = null,
)
