package com.beaconfire.travel.profile

import com.beaconfire.travel.repo.model.Profile

enum class ProfileUiModelStatus {
    Loading,
    LoadSucceed,
    LoadFailed,
    None
}

data class ProfileUiModel(
    val status: ProfileUiModelStatus,
    val profile: Profile?
)