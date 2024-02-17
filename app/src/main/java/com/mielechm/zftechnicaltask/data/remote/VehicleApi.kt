package com.mielechm.zftechnicaltask.data.remote

import com.mielechm.zftechnicaltask.data.remote.response.Vehicle
import com.mielechm.zftechnicaltask.data.remote.response.VehicleDetails
import retrofit2.http.GET
import retrofit2.http.Path

interface VehicleApi {

    @GET("vehicles")
    suspend fun getVehicles(): List<Vehicle>

    @GET("vehicles/{vehicleId}")
    suspend fun getVehicleDetails(@Path("vehicleId") vehicleId: String): VehicleDetails

}