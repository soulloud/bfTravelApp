package com.beaconfire.travel.repo

import com.beaconfire.travel.AppContainer
import com.beaconfire.travel.repo.data.DestinationData
import com.beaconfire.travel.repo.model.Destination
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class DestinationRepository(val appContainer: AppContainer) {

    suspend fun getAllDestinations() = callbackFlow<List<Destination>> {
        appContainer.firebaseStore.collection("destination")
            .get()
            .addOnSuccessListener { documents ->
                trySend(documents.mapNotNull { it.toDestination() })
            }
            .addOnFailureListener { trySend(emptyList()) }
            .await()
        awaitClose()
    }.first()

    suspend fun getAllDestinations(destinationIds: List<String>) =
        destinationIds.mapNotNull { getDestination(it) }

    suspend fun getDestination(destinationId: String) = callbackFlow {
        appContainer.firebaseStore.collection("destination")
            .document(destinationId)
            .get()
            .addOnSuccessListener {
                trySend(it.toDestination())
            }
            .addOnFailureListener {
                trySend(null)
            }
            .await()
        awaitClose()
    }.first()

    suspend fun searchDestination(searchText: String) =
        getAllDestinations().filter { it.name.contains(searchText, ignoreCase = true) }

    private fun DocumentSnapshot.toDestination() =
        toObject(DestinationData::class.java)?.toDestination()
            ?.copy(destinationId = id)
}
