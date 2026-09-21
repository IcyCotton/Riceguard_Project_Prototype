# Project Plan

A clean, high-contrast mobile dashboard UI for an Android agricultural application called 'RiceGuard' optimized for low-end smartphones. Top header shows app title and current location badge (Davao, Philippines). Main section features a prominent Weather Widget showing temperature (28°C), humidity, and disease risk alert for farmers. Center section features a large, easy-to-tap primary CTA button labeled 'Scan Rice Leaf' with a camera icon. Below it, add two quick-access card buttons for 'Diagnosis History' and 'Geographic Disease Map'. Bottom section shows a concise 'Recent Activity Summary' card with the last scanned disease result. Use an eco-friendly green and crisp off-white color palette with minimal shadows and high readability.

## Project Brief

# Project Brief: RiceGuard (MVP)

## Features
1. **On-Device Rice Leaf Diagnosis (TFLite)**: Real-time, offline classification of rice leaf conditions using a quantized CNN inference engine (`CNNInferenceEngine` and `PreprocessingPipeline` for `riceguard_quantized.tflite` with 224x224 input). It classifies images into 6 distinct classes: Bacterial Blight, Blast, Brown Spot, Tungro, Healthy, and Out of Scope.
2. **Localized Weather & Risk Advisory Widget**: A mobile dashboard widget utilizing `WeatherService` (powered by the OpenWeatherMap API) and `AdvisoryService` to show real-time climate status (e.g., temperature 28°C, humidity) and deliver instant treatment recommendations or alerts regarding spraying conditions to farmers.
3. **Local Scan History & Metadata Logger (Room)**: Offline persistence of past diagnosis records through a Room Database, storing `DiagnosisResult` history, geographic location tags (e.g., Davao, Philippines), and comprehensive scan metadata for seamless offline tracking.
4. **Outbreak Mapping & Agro-ECommerce Integration**: An interactive layout featuring a `GeographicTracker` for visual outbreak tracking and an `ECommerceLinkManager` providing single-tap quick-access redirects to digital marketplaces (Lazada, Shopee, AgriStores) for recommended treatment supplies.

## High-Level Tech Stack
* **Language & Concurrency**: Kotlin and Kotlin Coroutines for lightweight, fluid asynchronous background execution optimized for low-end devices.
* **User Interface Framework**: Jetpack Compose using an eco-friendly green and crisp off-white high-contrast palette with minimal shadows for enhanced outdoor readability.
* **Navigation Architecture**: **Jetpack Navigation 3** for fully state-driven, predictable screen flow management.
* **Adaptive Strategy**: **Compose Material Adaptive** library to guarantee highly responsive layouts across all Android device form factors.
* **Local Intelligence**: TensorFlow Lite (TFLite) for low-latency, fully offline on-device machine learning inference.
* **Persistence Layer**: Room Database for structural offline data caching of diagnostics and metadata.
* **Network Services**: OpenWeatherMap API for fetching localized atmospheric and climate data.

## Implementation Steps

### Task_1_TFLite_And_CameraX: Implement the on-device TFLite rice leaf classification engine (CNNInferenceEngine, PreprocessingPipeline) and integrate CameraX for real-time leaf image capture.
- **Status:** COMPLETED
- **Updates:** Implemented CNNInferenceEngine and PreprocessingPipeline for TFLite inference using 'riceguard_quantized.tflite'. Integrated CameraX for real-time leaf scanning and image capture. Integrated the scanner screen into the navigation graph. All unit tests passed.
- **Acceptance Criteria:**
  - TFLite model loaded successfully
  - CNNInferenceEngine classifies 6 classes correctly
  - CameraX preview and capture functional

### Task_2_Room_Database: Implement Room Database for offline persistence of DiagnosisResult history, geographic location tags, and scan metadata.
- **Status:** COMPLETED
- **Updates:** Implemented Room database with DiagnosisResult entity, DiagnosisDao, and RiceGuardDatabase singleton. Added DiagnosisRepository to manage data access. Verified implementation with unit tests (JVM and Instrumented).
- **Acceptance Criteria:**
  - Room database schema defined
  - DAO methods for saving and fetching history implemented
  - Offline data persistence verified
- **Duration:** N/A

### Task_3_Weather_And_Advisory: Implement WeatherService powered by OpenWeatherMap API and AdvisoryService to fetch real-time climate status and provide spraying recommendations.
- **Status:** IN_PROGRESS
- **Acceptance Criteria:**
  - OpenWeatherMap API_KEY integrated securely
  - WeatherService fetches temperature/humidity successfully
  - AdvisoryService computes spraying risk alerts
- **StartTime:** 2026-09-21 16:48:17 PST

### Task_4_Compose_UI_And_Navigation: Build high-contrast green/off-white Jetpack Compose UI using Jetpack Navigation 3 and Compose Material Adaptive. Create leaf scanner view, weather widget, history log dashboard, outbreak mapping, and ECommerceLinkManager.
- **Status:** PENDING
- **Acceptance Criteria:**
  - High-contrast eco-friendly theme applied
  - Jetpack Navigation 3 routes set up between dashboard and scanner
  - ECommerce links redirect to external apps/browsers successfully

### Task_5_Run_And_Verify: Perform final compilation, execute end-to-end user flows, and run full verification to ensure app stability on low-end devices.
- **Status:** PENDING
- **Acceptance Criteria:**
  - build pass
  - make sure all existing tests pass
  - app does not crash
  - critic_agent verified application stability and alignment with user requirements

