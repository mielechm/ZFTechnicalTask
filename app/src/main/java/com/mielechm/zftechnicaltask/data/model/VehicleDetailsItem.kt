package com.mielechm.zftechnicaltask.data.model

import com.mielechm.zftechnicaltask.data.remote.response.Location
import com.mielechm.zftechnicaltask.util.locationLausanne

data class VehicleDetailsItem(
    var id: String = "",
    var location: Location = locationLausanne,
    var proximity: Proximity = Proximity(0.0, "")
)