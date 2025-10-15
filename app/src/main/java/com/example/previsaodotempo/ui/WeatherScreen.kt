package com.example.weatherpeek.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sunny
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Composable
fun WeatherScreen() {
    // UI-only state (mocked) - replace with ViewModel states later
    var lat by remember { mutableStateOf("") }
    var lon by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var temps by remember { mutableStateOf<List<Pair<String, Double>>>(emptyList()) }

    // animated entrance
    val enterTransition = remember {
        fadeIn(animationSpec = tween(700)) + slideInVertically(initialOffsetY = { it / 3 }, animationSpec = tween(700))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("WeatherPeek", style = MaterialTheme.typography.h6) },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                elevation = 8.dp
            )
        },
        backgroundColor = MaterialTheme.colors.background
    ) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .padding(16.dp)) {

            // Header card with animated gradient
            AnimatedGradientCard(modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Previsão para", style = MaterialTheme.typography.subtitle1.copy(color = Color.White))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            if (lat.isBlank() || lon.isBlank()) "Local não definido" else "Lat: $lat, Lon: $lon",
                            style = MaterialTheme.typography.h6.copy(color = Color.White)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            SmallInfoChip(label = "Vento", value = "12 km/h")
                            SmallInfoChip(label = "Umidade", value = "68%")
                        }
                    }

                    // Big temperature circle (mock)
                    BigTempCircle(tempCelsius = temps.firstOrNull()?.second ?: 24.0, caption = "Agora", modifier = Modifier.padding(start = 12.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Inputs and actions
            Card(shape = MaterialTheme.shapes.medium, elevation = 4.dp) {
                Column(modifier = Modifier.padding(12.dp)) {
                    LabeledTextField(label = "Latitude", value = lat, onValueChange = { lat = it }, leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) })
                    Spacer(modifier = Modifier.height(8.dp))
                    LabeledTextField(label = "Longitude", value = lon, onValueChange = { lon = it }, leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) })
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = {
                            // validate
                            if (lat.isBlank() || lon.isBlank()) {
                                error = "Preencha latitude e longitude."
                                return@Button
                            }
                            // simulate loading + fetch (mock)
                            error = null
                            loading = true
                            // fake async with LaunchedEffect
                            LaunchedEffect(lat, lon) {
                                // simulate delay
                                kotlinx.coroutines.delay(700)
                                // generate mock hourly list from now
                                val now = OffsetDateTime.now()
                                val list = (0 until 12).map { i ->
                                    val t = now.plusHours(i.toLong()).format(DateTimeFormatter.ofPattern("HH:mm"))
                                    t to (18 + (i * 1.8) + (lat.filter { it.isDigit() }.takeLastOrNull(1)?.toIntOrNull() ?: 0))
                                }
                                temps = list
                                loading = false
                            }
                        }, modifier = Modifier.weight(1f)) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Atualizar")
                        }

                        OutlinedButton(onClick = {
                            // example coords - Porto Alegre
                            lat = "-30.0346"
                            lon = "-51.2177"
                        }, modifier = Modifier.weight(1f)) {
                            Text("Exemplo")
                        }
                    }
                    error?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = it, color = MaterialTheme.colors.error)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // content area with crossfade between states
            Box(modifier = Modifier.fillMaxSize()) {
                Crossfade(targetState = when {
                    loading -> "loading"
                    temps.isEmpty() -> "empty"
                    else -> "data"
                }, modifier = Modifier.fillMaxSize(), animationSpec = tween(500)) { state ->
                    when (state) {
                        "loading" -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
                                CircularProgressIndicator()
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("Buscando previsão...", style = MaterialTheme.typography.subtitle1)
                            }
                        }
                        "empty" -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top, modifier = Modifier.fillMaxSize()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Nenhuma previsão carregada.", style = MaterialTheme.typography.body1)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Insira coordenadas e toque em Atualizar.", style = MaterialTheme.typography.caption)
                            }
                        }
                        "data" -> {
                            // show list with animated appearing items
                            LazyColumn(modifier = Modifier
                                .fillMaxSize()
                                .clipToBounds()) {
                                itemsIndexed(temps) { index, item ->
                                    val (time, temp) = item
                                    // staggered animation
                                    val delay = index * 50
                                    var visible by remember { mutableStateOf(false) }
                                    LaunchedEffect(key1 = temps, key2 = index) {
                                        kotlinx.coroutines.delay(delay.toLong())
                                        visible = true
                                    }
                                    AnimatedVisibility(
                                        visible = visible,
                                        enter = fadeIn(tween(300 + delay)) + slideInHorizontally(tween(300 + delay), initialOffsetX = { it / 4 }),
                                        exit = fadeOut()
                                    ) {
                                        Card(modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 6.dp),
                                            elevation = 4.dp,
                                            shape = MaterialTheme.shapes.medium
                                        ) {
                                            Row(modifier = Modifier
                                                .padding(12.dp)
                                                .fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    IconCircle {
                                                        if (temp > 26) Icon(Icons.Default.Sunny, contentDescription = null) else Icon(Icons.Default.Cloud, contentDescription = null)
                                                    }
                                                    Spacer(modifier = Modifier.width(12.dp))
                                                    Column {
                                                        Text(time, style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold))
                                                        Text("Condição: Nublado", style = MaterialTheme.typography.body2)
                                                    }
                                                }
                                                Column(horizontalAlignment = Alignment.End) {
                                                    Text(text = "${temp.roundToInt()}°C", style = MaterialTheme.typography.h6)
                                                    Text(text = "Feels ${ (temp+1).roundToInt() }°", style = MaterialTheme.typography.caption)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
