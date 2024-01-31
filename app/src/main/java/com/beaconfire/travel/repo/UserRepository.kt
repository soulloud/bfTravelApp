package com.beaconfire.travel.repo

import com.beaconfire.travel.AppContainer
import com.beaconfire.travel.constant.Constant
import com.beaconfire.travel.repo.data.UserData
import com.beaconfire.travel.repo.model.Profile
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class UserRepository(private val appContainer: AppContainer) {
    suspend fun login(email: String, password: String) =
        queryUser(email, password)?.let { userData ->
            userData.profile?.let { appContainer.profileRepository.queryProfile(userData.profile) }
                ?.let { profile ->
                    userData.toUser(profile).also { persistUserId(userData.userId) }
                }
        }

    suspend fun register(email: String, displayName: String, password: String, profile: Profile) =
        appContainer.profileRepository.createProfile(profile)?.let { it ->
            createUser(
                UserData(
                    displayName = displayName,
                    email = email,
                    password = password,
                    profile = it.profileId
                )
            )?.toUser(profile)?.also { persistUserId(it.userId) }
        }

    suspend fun getLoginUser() =
        appContainer.userSharedPreferences.getString(Constant.SP_USER_KEY_USER_ID, null)?.let {
            queryUser(it)?.let { userData ->
                userData.profile?.let { appContainer.profileRepository.queryProfile(userData.profile) }
                    ?.let { profile ->
                        userData.toUser(profile)
                    }
            }
        }

    private suspend fun createUser(userData: UserData) = callbackFlow {
        val userRef = appContainer.firebaseStore.collection("user").document()
        userRef.set(userData)
            .addOnSuccessListener {
                trySend(userData)
            }
            .addOnFailureListener { trySend(null) }
            .await()
        awaitClose()
    }.first()

    private suspend fun queryUser(email: String, password: String) = callbackFlow {
        appContainer.firebaseStore.collection("user")
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get()
            .addOnSuccessListener {
                val document = it.documents.firstOrNull()
                trySend(document?.toObject(UserData::class.java)?.copy(userId = document.id))
            }
            .addOnFailureListener { trySend(null) }
        awaitClose()
    }.first()

    private suspend fun queryUser(userId: String) = callbackFlow {
        appContainer.firebaseStore.collection("user")
            .document(userId)
            .get()
            .addOnSuccessListener {
                trySend(it?.toObject(UserData::class.java)?.copy(userId = it.id))
            }
            .addOnFailureListener { trySend(null) }
        awaitClose()
    }.first()

    private fun persistUserId(userId: String) {
        appContainer.userSharedPreferences.edit()
            .putString(Constant.SP_USER_KEY_USER_ID, userId).apply()
    }

    companion object {
        val TAG = UserRepository::javaClass::class.simpleName
    }
}
