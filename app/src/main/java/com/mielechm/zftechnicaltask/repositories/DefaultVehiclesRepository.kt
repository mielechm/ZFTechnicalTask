package com.mielechm.zftechnicaltask.repositories

import com.mielechm.zftechnicaltask.data.remote.VehicleApi
import com.mielechm.zftechnicaltask.data.remote.response.Vehicle
import com.mielechm.zftechnicaltask.data.remote.response.VehicleDetails
import com.mielechm.zftechnicaltask.util.Resource
import dagger.hilt.android.scopes.ActivityScoped

@ActivityScoped
class DefaultVehiclesRepository(
    private val api: VehicleApi
): VehiclesRepository {
    override suspend fun getVehicles(): Resource<List<Vehicle>> {
        val response = try {
            api.getVehicles()
        } catch (e: Exception) {
            return Resource.Error(message = "Error occurred: ${e.message}")
        }
        return Resource.Success(response)
    }

    override suspend fun getVehicleDetails(id: String): Resource<VehicleDetails> {
        val response = try {
            api.getVehicleDetails(id)
        } catch (e: Exception) {
            return Resource.Error(message = "Error occured: ${e.message}")
        }
        return Resource.Success(response
        )
    }
}