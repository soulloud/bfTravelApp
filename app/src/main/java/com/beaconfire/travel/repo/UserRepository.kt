package com.beaconfire.travel.repo

import android.util.Log
import com.beaconfire.travel.AppContainer
import com.beaconfire.travel.repo.model.Profile
import com.beaconfire.travel.repo.model.User
import com.beaconfire.travel.utils.SessionManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class UserRepository(val appContainer: AppContainer) {

    private val db = FirebaseFirestore.getInstance()

    // The login function returns a Flow<User?>
    suspend fun login(email: String, password: String) = callbackFlow {
        db.collection("user")
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents.first()
                    val loginUser = document.toObject(User::class.java)
                    if (loginUser != null) {
                        // Store the userId in SessionManager
                        SessionManager.setUserId(document.id)
                        loginUser.userId = document.id // Only if you need to set it in the User object
                    }
                    trySend(loginUser)
                } else {
                    trySend(User.INVALID_USER)
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "Login failed", it)
                trySend(User.INVALID_USER)
            }
        awaitClose()
    }

    // TODO: Make sure register and login methods either both to take a User object,
    //       or both to take individual components.
    suspend fun register(user: User, profile: Profile) = callbackFlow {
        val newUserRef = db.collection("user").document()
        val userProfileRef = db.collection("profile").document()

        // Set the profile first
        userProfileRef.set(profile).await()
        val userWithProfile = user.copy(profile = userProfileRef.path)

        // Include the userId in the user document
        val userWithId = userWithProfile.copy(userId = newUserRef.id)

        // Set the user with the profile reference and userId
        newUserRef.set(userWithId).await()

        // Store the userId in SessionManager
        SessionManager.setUserId(newUserRef.id)

        trySend(userWithId)
        awaitClose()
    }.first()

    suspend fun fetchUser(documentId: String): User {
        // TODO: @David
        // fetch from user table and document.toObject(UserData::class.java) and then convert into User object
        // see example at DestinationRepository.searchDestination()
        return User.INVALID_USER
    }

    companion object {
        val TAG = UserRepository::javaClass::class.simpleName

    }
}
