package com.beaconfire.travel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.beaconfire.travel.navigation.NavigationGraph
import com.beaconfire.travel.ui.theme.TravelTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TravelTheme {
                AppScreen()
            }
        }
    }
}

@Composable
fun AppScreen() {
    NavigationGraph(rememberNavController())
}
