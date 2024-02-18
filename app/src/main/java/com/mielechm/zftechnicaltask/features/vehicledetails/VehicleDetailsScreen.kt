package com.mielechm.zftechnicaltask.features.vehicledetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mielechm.zftechnicaltask.R
import com.mielechm.zftechnicaltask.data.model.VehicleDetailsItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDetailsScreen(
    navController: NavController,
    id: String,
    nearbyVehicles: Int,
    viewModel: VehicleDetailsViewModel = hiltViewModel()
) {

    val isLoading by viewModel.isLoading.collectAsState()
    val loadError by viewModel.loadError.collectAsState()
    val vehicleDetails by viewModel.vehicleDetails.collectAsState()

    viewModel.getVehicleDetails(id)

    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(topBar = {
            TopAppBar(
                modifier = Modifier.shadow(elevation = 5.dp),
                title = { Text(stringResource(id = R.string.vehicles_in_proximity_label) + " $nearbyVehicles") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        }) { padding ->
            VehicleDetailsView(padding, vehicleDetails)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                if (loadError.isNotEmpty()) {
                    Text(
                        text = loadError,
                        modifier = Modifier.padding(20.dp)
                    )
                }
            }
        }
    }

}

@Composable
fun VehicleDetailsView(padding: PaddingValues, vehicleDetails: VehicleDetailsItem) {
    Column(modifier = Modifier.padding(padding)) {
        Text(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp),
            text = stringResource(id = R.string.label_vehicle_id) + " ${vehicleDetails.id}"
        )
        Text(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
            text = stringResource(id = R.string.label_proximity) + ": ${vehicleDetails.proximity.proximityName}"
        )
        Text(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
            text = stringResource(id = R.string.label_coordinates) + " Lat: ${vehicleDetails.location.latitude} Lon: ${vehicleDetails.location.longitude}"
        )
    }
}