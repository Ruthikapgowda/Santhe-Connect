# Santhe-Connect

Santhe-Connect is an Android mobile app that helps tourists discover local Karnataka weekly markets, authentic eateries, craft sellers, and homestays that are often missing from major travel apps.

The app supports slow travelers who want local experiences and gives small food vendors, market sellers, artisans, and homestay owners a simple digital presence.

## Problem Statement

Tourists visiting heritage towns often want to experience local Santhes, home-style food, handmade products, and family-run stays. These small local businesses usually do not appear on popular travel platforms, and their timings or locations are often known only to local people.

Santhe-Connect solves this by mapping local places, showing weekly market schedules, supporting reviews, and allowing locals to add their own listings.

## Features

- Google Maps based place discovery
- Category filters: Today, Food, Santhe, Craft, Stay
- Current-day Santhe filtering
- Today's Santhe appears first on the Today screen
- Special highlighted card for weekly Santhe listings
- Firebase Firestore real-time places and reviews
- Firebase Authentication email/password login
- Add Location form with GPS coordinate capture
- Review Wall with Firestore storage
- English and Kannada language support
- Kannada fields for place name, specialty, and village
- Coil image loading for place photos
- Category marker colors on map

## Tech Stack

- Android Studio
- Kotlin
- Jetpack Compose
- Firebase Firestore
- Firebase Authentication
- Google Maps SDK for Android
- Maps Compose
- Fused Location Provider
- Coil
- Gradle
- GitHub

## Repository Contents

```text
app/                         Android app source code
app/src/main/                Main app files
app/src/test/                Unit tests
firebase-sample-data/        Safe sample Firestore data
gradle/                      Gradle wrapper files
gradle.properties.example    Example local Gradle config
app/google-services.example.json  Example Firebase config shape
SUBMISSION.md                Submission checklist
README.md                    Project documentation
```

## Important Security Note

The real files below are intentionally ignored and should not be pushed to GitHub:

```text
gradle.properties
local.properties
app/google-services.json
app/build/
*.apk
```

Use the `.example` files to understand what local config is required.

## Setup Instructions

1. Clone the repository.

```bash
git clone https://github.com/Ruthikapgowda/Santhe-Connect.git
cd Santhe-Connect
```

2. Open the project in Android Studio.

3. Create a local `gradle.properties` file in the project root using `gradle.properties.example` as a reference.

```properties
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
android.useAndroidX=true
MAPS_API_KEY=YOUR_GOOGLE_MAPS_API_KEY
FIRESTORE_DATABASE_ID=YOUR_FIRESTORE_DATABASE_ID
```

4. Add Firebase config.

Download `google-services.json` from Firebase Console and place it here:

```text
app/google-services.json
```

If this file is not present, the project can still build, but Firebase services will not connect and the app will use sample fallback data.

5. Sync Gradle in Android Studio.

6. Run the app on an emulator or Android phone.

## Build Commands

Windows:

```powershell
.\gradlew.bat testDebugUnitTest
.\gradlew.bat assembleDebug
```

macOS/Linux:

```bash
./gradlew testDebugUnitTest
./gradlew assembleDebug
```

The debug APK is generated at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Firebase Setup

Create or select a Firebase project and add an Android app with package name:

```text
com.santheconnect.app
```

Enable:

```text
Authentication -> Sign-in method -> Email/Password
Firestore Database
```

The app reads Firestore from the database ID configured in:

```properties
FIRESTORE_DATABASE_ID=YOUR_FIRESTORE_DATABASE_ID
```

## Firestore Collections

### places

Each place document should contain:

```text
name: string
nameKn: string
type: string
specialty: string
specialtyKn: string
village: string
villageKn: string
latitude: double
longitude: double
openDays: array
imageUrl: string
reviewNote: string
popup: boolean
```

Allowed `type` values:

```text
Food
Market
Craft
Stay
```

Allowed `openDays` values:

```text
MONDAY
TUESDAY
WEDNESDAY
THURSDAY
FRIDAY
SATURDAY
SUNDAY
```

### reviews

Each review document contains:

```text
author: string
placeName: string
placeNameKn: string
note: string
noteKn: string
imageUrl: string
userEmail: string
createdAt: timestamp
```

Sample data is available in:

```text
firebase-sample-data/
```

## Firestore Rules For Demo

For testing/demo only:

```js
rules_version = '2';

service cloud.firestore {
  match /databases/{database}/documents {
    match /places/{document=**} {
      allow read, write: if true;
    }

    match /reviews/{document=**} {
      allow read, write: if true;
    }
  }
}
```

For production, use authenticated-user rules.

## Screenshots And Demo

Add screenshots under a `screenshots/` folder or attach an APK in GitHub Releases.

Suggested demo links:

```text
GitHub Repository: https://github.com/Ruthikapgowda/Santhe-Connect
APK Release Link: add after creating GitHub Release
```

## Project Impact

Santhe-Connect promotes inclusive tourism by helping tourists spend beyond commercial hotels and popular tourist spots. It gives digital visibility to small local businesses and supports cultural exchange through authentic Karnataka hospitality.
