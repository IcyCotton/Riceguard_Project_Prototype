package com.example.riceguard_project_prototype.ui

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.riceguard_project_prototype.ml.CNNInferenceEngine
import com.example.riceguard_project_prototype.ui.theme.Riceguard_Project_PrototypeTheme
import com.example.riceguard_project_prototype.ui.theme.TextDarkPrimary
import com.example.riceguard_project_prototype.ui.theme.TextDarkSecondary
import com.example.riceguard_project_prototype.ui.theme.TextLightPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassificationResultScreen(
    avgR: Int,
    avgG: Int,
    avgB: Int,
    isFromGallery: Boolean,
    onScanAnotherClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val inferenceEngine = remember { CNNInferenceEngine(context) }

    val bitmap = remember(avgR, avgG, avgB) {
        val bmp = Bitmap.createBitmap(224, 224, Bitmap.Config.ARGB_8888)
        bmp.eraseColor(android.graphics.Color.rgb(avgR, avgG, avgB))
        bmp
    }

    val classificationResults = remember(bitmap) {
        inferenceEngine.classifyLeaf(bitmap)
    }

    val topResult = classificationResults.firstOrNull()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Diagnosis Result", fontWeight = FontWeight.Bold, color = TextLightPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = TextLightPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1D5C3A),
                    titleContentColor = TextLightPrimary,
                    navigationIconContentColor = TextLightPrimary
                ),
                modifier = Modifier.statusBarsPadding()
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F9F5))
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main Summary Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (topResult?.className == "Healthy") Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isFromGallery) "Gallery Upload Analysis" else "Real-time Scan Diagnosis",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDarkSecondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = topResult?.className ?: "Analyzing...",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (topResult?.className == "Healthy") Color(0xFF1B5E20) else Color(0xFFB71C1C)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Confidence: ${String.format("%.1f%%", (topResult?.confidence ?: 0f) * 100)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextDarkPrimary
                        )
                    }
                }
            }

            // Section title: Probability Breakdown
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "CNN Inference Output (6 Classes)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1D5C3A)
                    )
                }
            }

            // Confidence Breakdown list
            items(classificationResults) { result ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = result.className,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = TextDarkPrimary
                            )
                            Text(
                                text = String.format("%.2f%%", result.confidence * 100),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFF1D5C3A)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { result.confidence },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                            color = Color(0xFF1D5C3A),
                            trackColor = Color(0xFFE0E0E0),
                            strokeCap = StrokeCap.Round
                        )
                    }
                }
            }

            // Call to action button
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onScanAnotherClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1D5C3A),
                        contentColor = TextLightPrimary
                    )
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh", tint = TextLightPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Scan Another Leaf", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextLightPrimary)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClassificationResultScreenPreview() {
    Riceguard_Project_PrototypeTheme {
        ClassificationResultScreen(
            avgR = 40,
            avgG = 160,
            avgB = 40,
            isFromGallery = false,
            onScanAnotherClick = {},
            onBackClick = {}
        )
    }
}
