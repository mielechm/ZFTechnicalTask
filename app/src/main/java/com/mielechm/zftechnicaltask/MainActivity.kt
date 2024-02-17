package com.mielechm.zftechnicaltask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                        VehiclesListScreen()
                    }
                }
            }
        }
    }
}
