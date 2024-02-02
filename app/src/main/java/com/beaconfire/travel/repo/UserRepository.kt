package com.beaconfire.travel.repo

import com.beaconfire.travel.AppContainer
import com.beaconfire.travel.constant.Constant
import com.beaconfire.travel.repo.data.UserData
import com.beaconfire.travel.repo.model.Profile
import com.beaconfire.travel.repo.model.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await

class UserRepository(private val appContainer: AppContainer) {
    private val _loginUserId = MutableStateFlow(queryLoginUserId())
    private val _loginUser = MutableStateFlow<User?>(null)

    val loginUserId: StateFlow<String?> = _loginUserId
    val loginUser: StateFlow<User?> = _loginUser

    suspend fun login(email: String, password: String) =
        queryUser(email, password)?.let { userData ->
            userData.profile?.let { appContainer.profileRepository.queryProfile(userData.profile) }
                ?.let { profile ->
                    userData.toUser(profile).also { persistUserId(it) }
                }
        }

    fun logout() = persistUserId(null)

    suspend fun register(email: String, displayName: String, password: String, profile: Profile) =
        appContainer.profileRepository.createProfile(profile)?.let { it ->
            createUser(
                UserData(
                    displayName = displayName,
                    email = email,
                    password = password,
                    profile = it.profileId
                )
            )?.toUser(profile)?.also { persistUserId(it) }
        }

    suspend fun getLoginUser() = _loginUser.value ?: _loginUserId.value?.let {
        queryUser(it)?.let { userData ->
            userData.profile?.let { appContainer.profileRepository.queryProfile(it) }
                ?.let { profile ->
                    userData.toUser(profile).also { _loginUser.update { it } }
                }
        }
    }

    private fun queryLoginUserId() =
        appContainer.userSharedPreferences.getString(Constant.SP_USER_KEY_USER_ID, null)

    private suspend fun createUser(userData: UserData) = callbackFlow {
        val userRef = appContainer.firebaseStore.collection("user").document()
        userRef.set(userData)
            .addOnSuccessListener { trySend(userData.copy(userId = userRef.id)) }
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

    suspend fun queryUser(userId: String) = callbackFlow {
        appContainer.firebaseStore.collection("user")
            .document(userId)
            .get()
            .addOnSuccessListener {
                trySend(it?.toObject(UserData::class.java)?.copy(userId = it.id))
            }
            .addOnFailureListener { trySend(null) }
        awaitClose()
    }.first()

    private fun persistUserId(user: User?) {
        if (user == null) {
            appContainer.userSharedPreferences.edit()
                .remove(Constant.SP_USER_KEY_USER_ID).apply()
        } else {
            appContainer.userSharedPreferences.edit()
                .putString(Constant.SP_USER_KEY_USER_ID, user.userId).apply()
        }
        _loginUserId.update { user?.userId }
        _loginUser.update { user }
    }

    companion object {
        val TAG = UserRepository::javaClass::class.simpleName
    }
}
