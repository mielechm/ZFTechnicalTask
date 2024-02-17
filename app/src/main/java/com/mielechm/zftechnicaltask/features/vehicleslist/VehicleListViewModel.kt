package com.mielechm.zftechnicaltask.features.vehicleslist

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mielechm.zftechnicaltask.data.model.Distance
import com.mielechm.zftechnicaltask.data.model.Proximity
import com.mielechm.zftechnicaltask.data.model.VehicleListItem
import com.mielechm.zftechnicaltask.repositories.VehiclesRepository
import com.mielechm.zftechnicaltask.util.Resource
import com.mielechm.zftechnicaltask.util.locationLausanne
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class VehicleListViewModel @Inject constructor(
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

    init {
        getVehicles()
    }

    fun getVehicles() {
        _vehiclesNearby.value = 0
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = repository.getVehicles()) {
                is Resource.Error -> {
                    _isLoading.value = false
                    _loadError.value = result.message.toString()
                }

                is Resource.Success -> {
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
                                    _vehiclesNearby.value++
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
                    _vehicles.value += vehicleItems
                }
            }
        }
    }

}