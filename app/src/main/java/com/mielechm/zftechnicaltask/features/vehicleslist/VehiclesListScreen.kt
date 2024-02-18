package com.mielechm.zftechnicaltask.features.vehicleslist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.mielechm.zftechnicaltask.data.model.VehicleListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiclesListScreen(
    navController: NavController,
    viewModel: VehicleListViewModel = hiltViewModel()
) {

    val vehicles by viewModel.vehicles.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val loadError by viewModel.loadError.collectAsState()
    val nearbyVehicles by viewModel.vehiclesNearby.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(topBar = {
            androidx.compose.material3.TopAppBar(
                modifier = Modifier.shadow(elevation = 5.dp),
                title = { Text(stringResource(id = R.string.vehicles_in_proximity_label) + " $nearbyVehicles") })
        }) {padding ->
            VehiclesList(padding, navController, vehicles, nearbyVehicles)
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
fun VehiclesList(paddingValues: PaddingValues, navController: NavController, vehicles: List<VehicleListItem>, nearbyVehicles: Int) {

    Column(modifier = Modifier.padding(paddingValues)) {
        LazyColumn(contentPadding = PaddingValues(start = 8.dp, end = 8.dp)) {
            itemsIndexed(vehicles, key = { _, vehicle -> vehicle.id }) { _, vehicle ->
                VehicleItem(navController, vehicle = vehicle, nearbyVehicles)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

    }
}

@Composable
fun VehicleItem(navController: NavController, vehicle: VehicleListItem, nearbyVehicles: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable {
                navController.navigate(
                    "vehicle_details_screen/${vehicle.id}/$nearbyVehicles"
                )
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = stringResource(id = R.string.label_vehicle_id) + "\r\n${vehicle.id}")
            Text(text = stringResource(id = R.string.label_proximity) + ": ${vehicle.proximity.proximityName}")
        }
    }

}
