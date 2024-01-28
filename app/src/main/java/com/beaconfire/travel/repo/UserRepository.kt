package com.beaconfire.travel.repo

import android.util.Log
import com.beaconfire.travel.repo.model.Profile
import com.beaconfire.travel.repo.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun login(email: String, password: String) = callbackFlow {
        db.collection("user")
            .whereEqualTo("email", email)
            .whereEqualTo(
                "password",
                password
            ) // Note: This is not secure for real applications
            .get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    // TODO: convert the result data into the actual user.
                    // val loginUser = it.documents.first()
                    val loginUser = User(email = email, displayName = email)
                    trySend(loginUser)
                } else {
                    trySend(User.INVALID_USER)
                }
            }
            .addOnFailureListener { trySend(User.INVALID_USER) }
            .await()
        awaitClose()
    }.first()

    // TODO: Make sure register and login methods either both to take a User object,
    //       or both to take individual components.
    suspend fun register(user: User, profile: Profile) = callbackFlow {
        val userWithProfile = user.copy(profile = createProfile(profile))
        db.collection("user").add(userWithProfile)
            .addOnSuccessListener {
                Log.d(TAG, "Register succeed")
                // TODO: convert the result data into the actual user.
                // val registeredUser = it.get().result...
                val registeredUser = userWithProfile
                trySend(registeredUser)
            }
            .addOnFailureListener {
                Log.d(TAG, "Register failed with error $it")
                trySend(User.INVALID_USER)
            }
            .await()
        awaitClose()
    }.first()

    private suspend fun createProfile(profile: Profile) = callbackFlow {
        val profileRef = db.collection("profile").document()
        profileRef.set(profile)
            .addOnSuccessListener {
                trySend(profileRef.path)
            }
            .addOnFailureListener {
                trySend(null)
            }
            .await()
        awaitClose()
    }.first()

    companion object {
        val TAG = UserRepository::javaClass::class.simpleName
    }

}
