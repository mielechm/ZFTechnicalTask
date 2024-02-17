package com.mielechm.zftechnicaltask.features.vehicleslist

import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mielechm.zftechnicaltask.R
import com.mielechm.zftechnicaltask.data.model.VehicleListItem

@Composable
fun VehiclesListScreen(viewModel: VehicleListViewModel = hiltViewModel()) {

    val vehicles by viewModel.vehicles.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val loadError by viewModel.loadError.collectAsState()
    val nearbyVehicles by viewModel.vehiclesNearby.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        VehiclesList(vehicles, nearbyVehicles)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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

@Composable
fun VehiclesList(vehicles: List<VehicleListItem>, nearby: Int) {

    Column {
        Text(
            modifier = Modifier.padding(16.dp),
            style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 20.sp),
            text = stringResource(id = R.string.vehicles_in_proximity_label) + " $nearby"
        )
        LazyColumn(contentPadding = PaddingValues(start = 8.dp, end = 8.dp)) {
            itemsIndexed(vehicles, key = { _, vehicle -> vehicle.id }) { _, vehicle ->
                VehicleItem(vehicle = vehicle)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

    }
}

@Composable
fun VehicleItem(vehicle: VehicleListItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = stringResource(id = R.string.label_vehicle_id) + "\r\n${vehicle.id}")
            Text(text = stringResource(id = R.string.label_proximity) + ": ${vehicle.proximity.proximityName}")
        }
    }

}
