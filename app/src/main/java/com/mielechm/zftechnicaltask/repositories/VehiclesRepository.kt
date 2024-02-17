package com.mielechm.zftechnicaltask.repositories

import com.mielechm.zftechnicaltask.data.remote.response.Vehicle
import com.mielechm.zftechnicaltask.data.remote.response.VehicleDetails
import com.mielechm.zftechnicaltask.util.Resource

interface VehiclesRepository {

    suspend fun getVehicles(): Resource<List<Vehicle>>

    suspend fun getVehicleDetails(id: String): Resource<VehicleDetails>

}