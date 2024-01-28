package com.beaconfire.travel.repo

import com.beaconfire.travel.repo.model.User
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {

    private val db = FirebaseFirestore.getInstance()
    fun login(email: String, password: String, completion: (User?) -> Unit) {
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
    fun register(user: User, completion: (Boolean) -> Unit) {
        db.collection("user")
            .add(user)
            .addOnSuccessListener {
                completion(true)
            }
            .addOnFailureListener {
                completion(false)
            }
    }

}
