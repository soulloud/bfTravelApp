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
                    Log.d("LoginSuccess", "User ID: ${user?.userId}") // Log the userId
                    SessionManager.setUserId(document.id) // Update SessionManager with the userId
                    trySend(user).isSuccess
                } else {
                    Log.d("LoginFail", "No user found") // Log no user found
                    trySend(null).isSuccess // No user found
                }
            }
            .addOnFailureListener { e ->
                Log.e("LoginError", "Login failed", e) // Log the error
                trySend(null).isSuccess
            }
        awaitClose()
    }.first()





    // TODO: Make sure register and login methods either both to take a User object,
    //       or both to take individual components.
    suspend fun register(user: User, profile: Profile): User? {
        // Create a new document reference for the user
        val newUserRef = db.collection("user").document()

        // Prepare the profile and save it
        val profileRef = db.collection("profile").document()
        profileRef.set(profile).await()

        // Prepare the user object with the document ID and profile reference
        val userWithIdAndProfile = user.copy(
            userId = newUserRef.id,
            profile = profileRef.id
        )

        // Save the user object to Firestore
        newUserRef.set(userWithIdAndProfile).await()

        // Update SessionManager with the new userId
        SessionManager.setUserId(newUserRef.id)

        return userWithIdAndProfile
    }


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
