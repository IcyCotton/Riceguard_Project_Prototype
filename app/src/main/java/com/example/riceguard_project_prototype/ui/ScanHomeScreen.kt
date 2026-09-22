package com.example.riceguard_project_prototype.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.riceguard_project_prototype.ui.theme.Riceguard_Project_PrototypeTheme
import com.example.riceguard_project_prototype.ui.theme.TextDarkPrimary
import com.example.riceguard_project_prototype.ui.theme.TextDarkSecondary
import com.example.riceguard_project_prototype.ui.theme.TextLightPrimary
import com.example.riceguard_project_prototype.ui.theme.TextLightSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanHomeScreen(
    onTakeExternalPhotoClick: () -> Unit,
    onUploadFromGalleryClick: (avgR: Int, avgG: Int, avgB: Int) -> Unit,
    onDashboardClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Scan Rice Leaf",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = TextLightPrimary
                        )
                        Text(
                            text = "AI will detect disease from your photo",
                            fontSize = 14.sp,
                            color = TextLightSecondary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onDashboardClick) {
                        Icon(
                            imageVector = Icons.Filled.Dashboard,
                            contentDescription = "Dashboard",
                            tint = TextLightPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1D5C3A)
                ),
                modifier = Modifier.statusBarsPadding()
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F9F5))
                .padding(innerPadding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Main visual Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1D5C3A))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Eco,
                        contentDescription = "Leaf Icon",
                        modifier = Modifier.size(64.dp),
                        tint = Color(0xFF81C784)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Take or upload a photo of\na rice leaf to begin",
                        color = TextLightPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    )
                }
            }

            // Tips section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFCF9F2)),
                border = BorderStroke(1.dp, Color(0xFFE8E1D5))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "📌 Tips for best results:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextDarkPrimary
                    )
                    val tips = listOf(
                        "Capture the whole leaf clearly",
                        "Use natural daylight",
                        "Avoid blurry or dark images",
                        "Focus on the affected area"
                    )
                    tips.forEach { tip ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = "Check",
                                modifier = Modifier.size(16.dp),
                                tint = Color(0xFF1D5C3A)
                            )
                            Text(
                                text = tip,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextDarkSecondary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Action Buttons
            Button(
                onClick = onTakeExternalPhotoClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1D5C3A),
                    contentColor = TextLightPrimary
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.CameraAlt,
                        contentDescription = "Camera",
                        tint = TextLightPrimary
                    )
                    Text(
                        text = "Take Photo with Camera",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextLightPrimary
                    )
                }
            }

            OutlinedButton(
                onClick = { 
                    onUploadFromGalleryClick(150, 120, 60)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.5.dp, Color(0xFF1D5C3A)),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF1D5C3A)
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.PhotoLibrary,
                        contentDescription = "Gallery",
                        tint = Color(0xFF1D5C3A)
                    )
                    Text(
                        text = "Upload from Gallery",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1D5C3A)
                    )
                }
            }

            // Footer branding text
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color(0xFF81C784), RoundedCornerShape(50))
                )
                Text(
                    text = "Powered by on-device CNN (TFLite) · Works offline",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextDarkSecondary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScanHomeScreenPreview() {
    Riceguard_Project_PrototypeTheme {
        ScanHomeScreen(
            onTakeExternalPhotoClick = {},
            onUploadFromGalleryClick = { _, _, _ -> },
            onDashboardClick = {}
        )
    }
}
