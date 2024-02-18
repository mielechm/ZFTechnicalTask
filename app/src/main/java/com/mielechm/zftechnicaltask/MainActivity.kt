package com.mielechm.zftechnicaltask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mielechm.zftechnicaltask.features.vehicledetails.VehicleDetailsScreen
import com.mielechm.zftechnicaltask.features.vehicleslist.VehiclesListScreen
import com.mielechm.zftechnicaltask.ui.theme.ZFTechnicalTaskTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZFTechnicalTaskTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "vehicles_list_screen"
                ) {
                    composable("vehicles_list_screen") {
                        VehiclesListScreen(navController)
                    }
                    composable(
                        "vehicle_details_screen/{id}",
                        arguments = listOf(navArgument("id") {
                            type = NavType.StringType
                        })
                    ) {
                        val id = remember {
                            it.arguments?.getString("id")
                        }
                        VehicleDetailsScreen(
                            navController = navController,
                            id = id.toString()
                        )
                    }
                }
            }
        }
    }
}
