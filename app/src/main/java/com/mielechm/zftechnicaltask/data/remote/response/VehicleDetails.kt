package com.mielechm.zftechnicaltask.data.remote.response

import com.mielechm.zftechnicaltask.util.locationLausanne

data class VehicleDetails(
    val location: Location = locationLausanne,
    val vehicleId: String = ""
)