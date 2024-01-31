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
    suspend fun login(email: String, password: String): User? = callbackFlow {
        db.collection("user")
            .whereEqualTo("email", email)
            .whereEqualTo("password", password) // Note: Consider using Firebase Auth for secure password handling
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()) {
                    val document = querySnapshot.documents.first()
                    val user = document.toObject(User::class.java)?.apply {
                        this.userId = document.id // Update the user object with the document ID
                    }
                    SessionManager.setUserId(document.id) // Update SessionManager with the userId
                    trySend(user).isSuccess
                } else {
                    trySend(null).isSuccess // No user found
                }
            }
            .addOnFailureListener {
                trySend(null).isSuccess
            }
        awaitClose()
    }.first()



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
