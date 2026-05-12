# Santhe-Connect

Santhe-Connect is a Kotlin Android app for discovering weekly markets, local eateries, crafts, and homestays around heritage towns in Karnataka.

## Features Included

- Google Map with category-colored markers.
- Current-day Santhe filtering.
- Local eatery, market, craft, and stay categories.
- Add Location form that captures GPS coordinates.
- Review Wall with image URL and voice-note text support.
- Firestore-ready repository with built-in sample data fallback.
- Coil image loading for food and market photos.

## Open In Android Studio

1. Open this folder in Android Studio.
2. Let Gradle sync.
3. Add your Google Maps key in `gradle.properties`:

```properties
MAPS_API_KEY=your_key_here
```

4. To use Firebase Firestore, add your app in Firebase Console and place `google-services.json` in `app/`. Then add the Google Services plugin using Android Studio Firebase Assistant or Gradle.

The app still works as a demo with sample data before Firebase is connected.
