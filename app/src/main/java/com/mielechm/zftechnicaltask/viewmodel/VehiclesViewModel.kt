package com.mielechm.zftechnicaltask.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mielechm.zftechnicaltask.data.model.Distance
import com.mielechm.zftechnicaltask.data.model.Proximity
import com.mielechm.zftechnicaltask.data.model.VehicleDetailsItem
import com.mielechm.zftechnicaltask.data.model.VehicleListItem
import com.mielechm.zftechnicaltask.repositories.VehiclesRepository
import com.mielechm.zftechnicaltask.util.Resource
import com.mielechm.zftechnicaltask.util.locationLausanne
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class VehiclesViewModel @Inject constructor(
    private val repository: VehiclesRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _loadError = MutableStateFlow("")
    val loadError = _loadError.asStateFlow()

    private val _vehicles = MutableStateFlow<List<VehicleListItem>>(emptyList())
    val vehicles = _vehicles.asStateFlow()

    private val _vehiclesNearby = MutableStateFlow(0)
    val vehiclesNearby = _vehiclesNearby.asStateFlow()

    private val _vehicleDetails = MutableStateFlow(VehicleDetailsItem())
    val vehicleDetails = _vehicleDetails.asStateFlow()

    init {
        getVehicles()
    }

    fun getVehicles() {
        _isLoading.value = true
        viewModelScope.launch {
            while (isActive) {
                when (val result = repository.getVehicles()) {
                    is Resource.Error -> {
                        _isLoading.value = false
                        _loadError.value = result.message.toString()
                    }

                    is Resource.Success -> {
                        var counter = 0
                        val vehicleItems = result.data!!.map {
                            val details = repository.getVehicleDetails(it.vehicleId).data!!

                            val distanceResults = FloatArray(2)
                            Location.distanceBetween(
                                locationLausanne.latitude,
                                locationLausanne.longitude,
                                details.location.latitude,
                                details.location.longitude,
                                distanceResults
                            )
                            val distance = distanceResults[0].toDouble()
                            VehicleListItem(
                                id = it.vehicleId,
                                proximity = Proximity(
                                    distance, if (distance < 1000) {
                                        counter++
                                        "${Distance.CLOSE.literal} (%.2f m)".format(
                                            Locale.ENGLISH,
                                            distance
                                        )
                                    } else {
                                        "${Distance.FAR_AWAY.literal} (%.2f m)".format(
                                            Locale.ENGLISH,
                                            distance
                                        )
                                    }
                                )
                            )
                        }

                        _isLoading.value = false
                        _loadError.value = ""
                        _vehicles.value = vehicleItems
                        _vehiclesNearby.value = counter
                    }
                }
                delay(5000)
            }
        }

    }

    fun getVehicleDetails(id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            while (isActive) {
                when (val result = repository.getVehicleDetails(id)) {
                    is Resource.Error -> {
                        _isLoading.value = false
                        _loadError.value = result.message.toString()
                    }

                    is Resource.Success -> {
                        val distanceResults = FloatArray(2)
                        Location.distanceBetween(
                            locationLausanne.latitude,
                            locationLausanne.longitude,
                            result.data!!.location.latitude,
                            result.data.location.longitude,
                            distanceResults
                        )
                        val distance = distanceResults[0].toDouble()
                        _vehicleDetails.value = VehicleDetailsItem(
                            id = result.data.vehicleId,
                            proximity = Proximity(
                                distance, if (distance < 1000) {
                                    "${Distance.CLOSE.literal} (%.2f m)".format(
                                        Locale.ENGLISH,
                                        distance
                                    )
                                } else {
                                    "${Distance.FAR_AWAY.literal} (%.2f m)".format(
                                        Locale.ENGLISH,
                                        distance
                                    )
                                }
                            ),
                            location = result.data.location
                        )
                        _isLoading.value = false
                        _loadError.value = ""
                    }
                }
                delay(5000)
            }
        }
    }

}