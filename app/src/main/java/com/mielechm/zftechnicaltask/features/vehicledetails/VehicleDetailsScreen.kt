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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mielechm.zftechnicaltask.R
import com.mielechm.zftechnicaltask.data.model.VehicleDetailsItem
import com.mielechm.zftechnicaltask.viewmodel.VehiclesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDetailsScreen(
    navController: NavController,
    id: String,
    viewModel: VehiclesViewModel = hiltViewModel()
) {

    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val loadError by viewModel.loadError.collectAsStateWithLifecycle()
    val vehicleDetails by viewModel.vehicleDetails.collectAsStateWithLifecycle()
    val nearbyVehicles by viewModel.vehiclesNearby.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.getVehicleDetails(id)
    }

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