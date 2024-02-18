package com.mielechm.zftechnicaltask.features.vehicledetails

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mielechm.zftechnicaltask.data.model.Distance
import com.mielechm.zftechnicaltask.data.model.Proximity
import com.mielechm.zftechnicaltask.data.model.VehicleDetailsItem
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
class VehicleDetailsViewModel @Inject constructor(
    private val repository: VehiclesRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _loadError = MutableStateFlow("")
    val loadError = _loadError.asStateFlow()

    private val _vehicleDetails = MutableStateFlow(VehicleDetailsItem())
    val vehicleDetails = _vehicleDetails.asStateFlow()

    fun getVehicleDetails(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
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
                        _vehicleDetails.value.apply {
                        this.id = result.data.vehicleId
                        this.proximity = Proximity(
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
                        )
                            this.location = result.data.location
                    }
                    _isLoading.value = false
                    _loadError.value = ""
                }
            }
        }
    }

}