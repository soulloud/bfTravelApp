package com.beaconfire.travel.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.beaconfire.travel.mallApplication
import com.beaconfire.travel.repo.DestinationRepository
import com.beaconfire.travel.utils.SortMethod
import com.beaconfire.travel.utils.sort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val destinationRepository: DestinationRepository
) : ViewModel() {

    var homeUiModel by mutableStateOf<HomeUiModel>(HomeUiModel.None)

    init {
        loadDestinations()
    }

    fun onTagUpdated(tags: List<String>) {
        Log.d(TAG, "onTagUpdated() $tags")
    }

    private fun loadDestinations() {
        homeUiModel = HomeUiModel.Loading
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                homeUiModel = try {
                    val destinations =
                        destinationRepository.getAllDestinations()
                            .sort(SortMethod.AlphabetAscending)
                    HomeUiModel.LoadSucceed(destinations)
                } catch (e: Exception) {
                    HomeUiModel.LoadFailed
                }
            }
        }
    }

    companion object {
        private val TAG = HomeViewModel::class.java.simpleName

        val Factory = viewModelFactory {
            initializer {
                HomeViewModel(mallApplication().container.destinationRepository)
            }
        }
    }
}