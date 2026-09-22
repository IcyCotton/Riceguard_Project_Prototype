package com.example.riceguard_project_prototype.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.riceguard_project_prototype.ui.theme.TextDarkPrimary
import com.example.riceguard_project_prototype.ui.theme.TextDarkSecondary
import com.example.riceguard_project_prototype.ui.theme.TextLightPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rice Field Risk Map", fontWeight = FontWeight.Bold, color = TextLightPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1D5C3A))
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F9F5))
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = "Map view",
                            tint = Color(0xFF1D5C3A),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Interactive Regional Farm Map",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = TextDarkPrimary
                        )
                        Text(
                            text = "Lat: 15.4827° N, Lon: 120.9692° E",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1B5E20)
                        )
                    }
                }
            }

            Text(
                text = "Nearby Outbreak Monitoring",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D5C3A)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(32.dp)
                    )
                    Column {
                        Text(
                            text = "Nueva Ecija Sector 4 - Moderate Risk",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = TextDarkPrimary
                        )
                        Text(
                            text = "Brown Spot report logged 2km away from your farm.",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextDarkSecondary
                        )
                    }
                }
            }
        }
    }
}
