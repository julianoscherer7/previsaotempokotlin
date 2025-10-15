package com.example.weatherpeek

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.weatherpeek.ui.WeatherScreen
import com.example.weatherpeek.ui.WeatherPeekTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherPeekTheme {
                WeatherScreen()
            }
        }
    }
}
