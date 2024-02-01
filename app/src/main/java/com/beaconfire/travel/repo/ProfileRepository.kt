package com.beaconfire.travel.repo

import com.beaconfire.travel.AppContainer
import com.beaconfire.travel.repo.data.ProfileData
import com.beaconfire.travel.repo.data.toProfileData
import com.beaconfire.travel.repo.model.Profile
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class ProfileRepository(private val appContainer: AppContainer) {

    suspend fun queryProfile(profileId: String) = callbackFlow {
        appContainer.firebaseStore.collection("profile").document(profileId)
            .get()
            .addOnSuccessListener { trySend(it.toProfile()) }
            .addOnFailureListener { trySend(null) }
            .await()
        awaitClose()
    }.first()

    suspend fun createProfile(profile: Profile): Profile? = callbackFlow {
        val profileRef = appContainer.firebaseStore.collection("profile").document()
        profileRef.set(profile.toProfileData())
            .addOnSuccessListener { trySend(profile.copy(profileId = profileRef.id)) }
            .addOnFailureListener { trySend(null) }
            .await()
        awaitClose()
    }.first()

    suspend fun updateProfile(profile: Profile) =
        appContainer.userRepository.getLoginUser()?.profile?.profileId?.let { profileId ->
            callbackFlow {
                val profileRef =
                    appContainer.firebaseStore.collection("profile").document(profileId)
                profileRef.set(profile.toProfileData())
                    .addOnSuccessListener { trySend(true) }
                    .addOnFailureListener { trySend(false) }
                    .await()
                awaitClose()
            }.first()
        } ?: false

    private fun DocumentSnapshot.toProfile() =
        toObject(ProfileData::class.java)?.toProfile()
            ?.copy(profileId = id)
}
