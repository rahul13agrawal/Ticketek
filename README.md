# Ticketek - Ticket Scanning Android App

A modern Android application built with Kotlin and Jetpack Compose for scanning tickets at venues using ML Kit barcode scanning.

## Features Implemented

### 🎯 **Splash Screen**
- Clean splash screen with app logo
- Automatically navigates to permission screen after 1 second
- Uses AndroidX Core Splash Screen API for smooth transitions

### 📍 **Location Permission Management**
- Requests both `ACCESS_FINE_LOCATION` and `ACCESS_COARSE_LOCATION` permissions
- User-friendly UI with clear explanation of why location is needed
- Uses Accompanist Permissions library for declarative permission handling
- Automatically proceeds to venue discovery once permissions are granted

### 🗺️ **Venue Discovery**
- Fetches venues from API based on user's current GPS location
- Uses Google Play Services Fused Location Provider for accurate location
- Displays loading screen while fetching venues
- Clean list UI showing venue details (name, address, city)
- Pull-to-refresh functionality to reload venues
- Error handling with retry button for network failures

### 📷 **Barcode/QR Code Scanning**
- Real-time camera preview using CameraX
- ML Kit Barcode Scanning for detecting multiple barcode formats
- Camera permission handling with runtime requests
- Automatic barcode detection and processing
- Prevents duplicate scans while processing

### ✅ **Ticket Entry Validation**
- API integration to validate scanned tickets against venue entry system
- Real-time validation with success/failure states
- Visual feedback with green (success) or red (failure) color schemes
- Displays scan result status and action messages

### 🔄 **Auto-Resume Scanning**
- Automatically returns to scanning mode after 5 seconds
- Shows countdown message to user
- Seamless flow for continuous ticket scanning

### 🌐 **Network Error Handling**
- Graceful error handling for API failures
- User-friendly error messages
- Retry functionality for failed operations
- Handles network timeouts and connectivity issues

## Technical Stack

### Architecture & Design Patterns
- **Pattern**: MVVM (Model-View-ViewModel) with MVI-like state management
- **Dependency Injection**: Dagger Hilt for compile-time dependency injection
- **Navigation**: Jetpack Navigation Compose with nested navigation graphs
- **UI**: 100% Jetpack Compose with Material3 design system
- **State Management**: Kotlin Flows and StateFlow for reactive UI updates
- **Coroutines**: Structured concurrency for asynchronous operations

### Key Libraries & Versions
- **Kotlin**: Latest stable version
- **Compose**: BOM-based dependency management
- **Networking**:
  - Retrofit 2.x for REST API communication
  - OkHttp 3.x with logging interceptor
  - Gson for JSON serialization/deserialization
- **Camera**: CameraX 1.3.x for camera functionality
- **Barcode Scanning**: ML Kit Barcode Scanning 17.2.0
- **Location**: Google Play Services Location 21.x
- **Permissions**: Accompanist Permissions 0.34.0
- **Coroutines**: Kotlinx Coroutines Android & Play Services

### Project Structure
```
app/src/main/java/com/rahul/ticketek/
├── MainActivity.kt                      # Main activity with Hilt integration
├── TicketekApplication.kt              # Hilt application class
├── data/
│   ├── model/
│   │   ├── Venue.kt                    # Venue data models with Gson annotations
│   │   └── ScanModels.kt               # Scan request/response models
│   ├── api/
│   │   └── TicketekApi.kt              # Retrofit API interface with endpoints
│   └── repository/
│       └── TicketekRepository.kt       # Repository with coroutine-based API calls
├── di/
│   ├── NetworkModule.kt                # Network dependencies (Retrofit, OkHttp, Gson)
│   └── LocationModule.kt               # Location services dependencies
├── navigation/
│   ├── Screen.kt                       # Sealed class for navigation routes
│   └── AppNavHost.kt                   # Navigation graph with nested flows
├── ui/
│   ├── splash/
│   │   └── SplashScreen.kt            # Splash screen with delayed navigation
│   ├── permission/
│   │   └── LocationPermissionScreen.kt # Permission request with Accompanist
│   ├── loading/
│   │   └── LoadingScreen.kt           # Reusable loading indicator
│   ├── venues/
│   │   ├── VenuesListScreen.kt        # Venues list with LazyColumn
│   │   ├── VenuesViewModel.kt         # ViewModel with location & API logic
│   │   └── VenuesContract.kt          # UI state data class
│   ├── scan/
│   │   ├── ScanTicketScreen.kt        # Camera preview with ML Kit integration
│   │   ├── ScanViewModel.kt           # Scan logic and API validation
│   │   └── ScanContract.kt            # Scan UI state
│   ├── result/
│   │   └── ResultScreen.kt            # Success/failure result with auto-navigation
│   └── theme/
│       ├── Color.kt                    # Material3 color definitions
│       ├── Theme.kt                    # Theme with dynamic colors support
│       └── Type.kt                     # Typography definitions
└── util/
    └── Result.kt                       # Sealed class for API result handling
```

## API Integration

The app integrates with the Ticketek API using Retrofit with the following configuration:

### Base URL
```
https://ignition.qa.ticketek.net/
```

### Authentication Headers
All API requests include:
- `x-api-key`: TEq5Mddna23xSNsoDeYt8aP02BJHrvoa6X07nEuD
- `authorization`: Basic Yhd9X=38D88!
- `content-type`: application/json
- `accept-language`: en

### Endpoints

#### 1. Get Venues
```
GET /venues?latitude={lat}&longitude={lon}
```
Retrieves venues based on GPS coordinates.

**Query Parameters:**
- `latitude` (Double): User's latitude
- `longitude` (Double): User's longitude

**Response Example:**
```json
{
  "venues": [
    {
      "code": "MCG",
      "name": "Melbourne Cricket Ground",
      "address": "Brunton Ave",
      "city": "Melbourne",
      "state": "VIC",
      "postcode": "3002",
      "latitude": -37.8200,
      "longitude": 144.9834,
      "timezone": "Australia/Melbourne",
      "pax_locations": [
        {
          "name": "Gate 1",
          "gates": [{"name": "A"}]
        }
      ]
    }
  ],
  "total": 1
}
```

#### 2. Scan Ticket
```
POST /venues/{venue_code}/pax/entry/scan
```
Validates ticket barcode for venue entry.

**Path Parameters:**
- `venue_code` (String): Venue code from venues list

**Request Body:**
```json
{
  "barcode": "ABC123XYZ"
}
```

**Response Example:**
```json
{
  "status": "Entry allowed",
  "action": "ENTRY",
  "result": "SUCCESS",
  "concession": 0
}
```

**Response Fields:**
- `status`: Human-readable status message
- `action`: Action type (ENTRY, DENY, etc.)
- `result`: SUCCESS or FAILURE
- `concession`: Concession type (0 = none)

## Setup Instructions

### Prerequisites
- Android Studio Hedgehog or later
- JDK 21
- Android SDK 35 (compileSdk)
- Minimum Android SDK 29 (Android 10)
- Physical Android device or emulator with camera support

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone git@github.com:rahul13agrawal/Ticketek.git
   cd Ticketek
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned directory

3. **Configure API Base URL (Optional)**

   The API base URL is configured in `app/build.gradle.kts`:
   ```kotlin
   buildConfigField(
       "String",
       "API_BASE_URL",
       "\"https://ignition.qa.ticketek.net/\""
   )
   ```

4. **Sync Gradle**
   - Android Studio will automatically sync Gradle files
   - If not, click "Sync Now" or "File > Sync Project with Gradle Files"

5. **Build the project**
   ```bash
   ./gradlew build
   ```

6. **Run on device/emulator**
   ```bash
   ./gradlew installDebug
   ```
   Or use Android Studio's "Run" button (Shift+F10)

### Configuration

#### Changing API URL

Update the `buildConfigField` in `app/build.gradle.kts`:

```kotlin
android {
    defaultConfig {
        buildConfigField(
            "String",
            "API_BASE_URL",
            "\"https://your-api-url.com/\""
        )
    }
}
```

#### Network Timeout Configuration

Network timeouts are configured in `di/NetworkModule.kt`:
- Connect timeout: 30 seconds
- Read timeout: 30 seconds
- Write timeout: 30 seconds

To modify:
```kotlin
OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .build()
```

**Built with ❤️ using Kotlin and Jetpack Compose**
