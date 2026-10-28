package com.example.riceguard_project_prototype.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.riceguard_project_prototype.data.dataset.DatasetManager
import com.example.riceguard_project_prototype.ui.theme.Riceguard_Project_PrototypeTheme
import com.example.riceguard_project_prototype.ui.theme.TextDarkPrimary
import com.example.riceguard_project_prototype.ui.theme.TextDarkSecondary
import com.example.riceguard_project_prototype.ui.theme.TextLightPrimary
import com.example.riceguard_project_prototype.ui.theme.TextLightSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanHomeScreen(
    onTakeExternalPhotoClick: () -> Unit,
    onUploadFromGalleryClick: (uriString: String) -> Unit,
    onDashboardClick: () -> Unit
) {
    val context = LocalContext.current
    val datasetManager = remember { DatasetManager(context) }
    val metadata = remember { datasetManager.getDatasetMetadata() }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            onUploadFromGalleryClick(uri.toString())
        }
    }

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
                    .height(180.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1D5C3A))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Eco,
                        contentDescription = "Leaf Icon",
                        modifier = Modifier.size(56.dp),
                        tint = Color(0xFF81C784)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
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

            // Dataset Metadata Badge Card
            if (metadata != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    border = BorderStroke(1.dp, Color(0xFFA5D6A7))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FolderZip,
                            contentDescription = "Dataset Loaded",
                            tint = Color(0xFF1B5E20),
                            modifier = Modifier.size(28.dp)
                        )
                        Column {
                            Text(
                                text = "Dataset Integrated: ${metadata.datasetName}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = TextDarkPrimary
                            )
                            Text(
                                text = "${metadata.totalImages} images across ${metadata.categories.size} categories loaded",
                                fontSize = 12.sp,
                                color = TextDarkSecondary
                            )
                        }
                    }
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
                    galleryLauncher.launch("image/*")
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
                    text = "Powered by Combined Dataset & CNN · Works offline",
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
            onUploadFromGalleryClick = { _ -> },
            onDashboardClick = {}
        )
    }
}
