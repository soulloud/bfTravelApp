package com.beaconfire.travel.repo

import com.beaconfire.travel.repo.model.Destination
import com.google.firebase.firestore.FirebaseFirestore

class HomeRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getDestinations(completion: (List<Destination>?) -> Unit){
        db.collection("destination")
            //.whereArrayContains("location", searchText)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    completion(documents.toObjects(Destination::class.java))
                } else {
                    completion(null)
                }
            }
            .addOnFailureListener{
                completion(null)
            }
    }

    suspend fun searchDestination(searchText: String, completion: (Destination?) -> Unit){
        db.collection("destination")
            .whereEqualTo("name", searchText)
            //.whereArrayContains("location", searchText)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val destination = documents.first().toObject(Destination::class.java)
                    completion(destination)
                } else {
                    completion(null)
                }
            }
            .addOnFailureListener{
                completion(null)
            }
    }

}