package com.beaconfire.travel.repo

import com.beaconfire.travel.AppContainer
import com.beaconfire.travel.repo.data.ReviewData
import com.beaconfire.travel.repo.model.Destination
import com.beaconfire.travel.repo.model.Review
import com.beaconfire.travel.repo.model.User
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class ReviewRepository(private val appContainer: AppContainer) {

    suspend fun getAllReviewsOnCurrentDestination(destination: Destination): List<Review> {
        val destinationId = destination.destinationId
        return queryReviewsByCondition(
            "destination", destinationId!!
        ).map {
              it.toReview()
        }
    }

    suspend fun getAllReviewsOfCurrentUser(): List<Review> {
        val userId = getUserInfo()?.userId
        return queryReviewsByCondition(
            "owner", userId!!
        ).map {
            it.toReview()
        }
    }

    suspend fun addNewReview(reviewData: ReviewData) = callbackFlow{

        val userId = getUserInfo()?.userId
//        val reviewData = ReviewData(
//            reviewId = "3",
//            destination = "Jh37jrgWHfvFNO3chG9t",
//            score = 5.0,
//            title = "Fantastic Place",
//            description = "Highly recommend this destination!",
//            timestamp = "2022-03-10T15:45:00",
//            owner = "fbZxm70A90ORSi5D86cC"
//        )

        appContainer.firebaseStore.collection("review")
            .add(reviewData)
            .addOnSuccessListener { trySend(true) }
            .addOnFailureListener { trySend(false) }
            .await()
        awaitClose()
    }.first()

    suspend fun deleteReview(review: Review) = callbackFlow{
        appContainer.firebaseStore.collection("review").document(review.reviewId)
            .delete()
            .addOnSuccessListener { trySend(true) }
            .addOnFailureListener { trySend(false) }
            .await()
        awaitClose()
    }.first()

    private suspend fun queryReviewsByCondition(
        collectionName: String,
        conditionText: String
    ) = callbackFlow<List<ReviewData>>{
        appContainer.firebaseStore.collection("review")
            .whereEqualTo(collectionName, conditionText)
            .get()
            .addOnSuccessListener { documents -> trySend(documents.mapNotNull { it.toReviewData(it.id) }) }
            .addOnFailureListener { trySend(emptyList()) }
            .await()
        awaitClose()
    }.first()


    private suspend fun getUserInfo(): User? {
        return appContainer.userRepository.getLoginUser()
    }

    private fun DocumentSnapshot.toReviewData(id: String) =
        toObject(ReviewData::class.java)?.copy(reviewId = id)
}