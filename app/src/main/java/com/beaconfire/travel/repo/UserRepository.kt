package com.beaconfire.travel.repo

import com.beaconfire.travel.repo.model.Profile
import com.beaconfire.travel.repo.model.User
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {

    private val db = FirebaseFirestore.getInstance()
    suspend fun login(email: String, password: String, completion: (User?) -> Unit) {
        db.collection("user")
            .whereEqualTo("email", email)
            .whereEqualTo("password", password) // Note: This is not secure for real applications
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val user = documents.first().toObject(User::class.java)
                    completion(user)
                } else {
                    completion(null)
                }
            }
            .addOnFailureListener {
                completion(null)
            }
    }
    suspend fun register(user: User, profile: Profile, completion: (Boolean) -> Unit) {
        // First, create a Profile document
        val profileRef = db.collection("profile").document()
        profileRef.set(profile)
            .addOnSuccessListener {
                // Now, create a User document with the Profile reference
                val updatedUser = user.copy(profile = profileRef.path)
                db.collection("user")
                    .add(updatedUser)
                    .addOnSuccessListener { completion(true) }
                    .addOnFailureListener { completion(false) }
            }
            .addOnFailureListener { completion(false) }
    }



}
