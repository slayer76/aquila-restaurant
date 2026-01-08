# Restaurant Menu - Android App

A Proof-of-Concept Android application for displaying a restaurant menu using Firebase backend.

## Tech Stack

- **Language**: Java
- **Framework**: Android SDK (compatible with Android Studio Jellyfish or later)
- **Database/Backend**: Firebase (Cloud Firestore for menu data, Firebase Auth for guest login)
- **UI Style**: Material Design 3
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)

## Project Structure

```
RestaurantMenu/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/restaurantmenu/
│   │   │   ├── activities/
│   │   │   │   ├── MainActivity.java      # Login screen with Firebase Anonymous Auth
│   │   │   │   ├── MenuActivity.java      # Menu display with categories
│   │   │   │   └── DetailActivity.java    # Food item details
│   │   │   ├── adapters/
│   │   │   │   ├── CategoryAdapter.java   # RecyclerView adapter for categories
│   │   │   │   └── FoodItemAdapter.java   # RecyclerView adapter for food items
│   │   │   ├── models/
│   │   │   │   ├── FoodItem.java          # Food item data model
│   │   │   │   └── Category.java          # Category data model
│   │   │   └── utils/
│   │   ├── res/
│   │   │   ├── layout/                    # XML layouts
│   │   │   ├── values/                    # Strings, colors, themes
│   │   │   ├── drawable/                  # Vector icons and drawables
│   │   │   └── menu/                      # Menu resources
│   │   └── AndroidManifest.xml
│   ├── build.gradle                       # App-level Gradle config
│   └── google-services.json               # ⚠️ PLACEHOLDER - Replace with your own!
├── build.gradle                           # Project-level Gradle config
├── settings.gradle
├── gradle.properties
├── SampleData.json                        # Sample data for Firebase import
└── README.md
```

## Features

1. **Authentication**: Firebase Anonymous Authentication for guest access
2. **Menu Display**: RecyclerView with horizontal category chips and vertical food item list
3. **Category Filtering**: Filter food items by category (Appetizers, Mains, Desserts)
4. **Detail View**: Full details of selected food items with images
5. **Material Design 3**: Modern UI with Material You theming

## Setup Instructions

### 1. Firebase Setup

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or select an existing one
3. Add an Android app with package name: `com.example.restaurantmenu`
4. Download the `google-services.json` file

### 2. Replace google-services.json

**⚠️ IMPORTANT**: Replace the placeholder file at:
```
RestaurantMenu/app/google-services.json
```

The placeholder file is located in the `app/` directory. Download your actual `google-services.json` from Firebase Console and replace it.

### 3. Enable Firebase Services

In Firebase Console:

1. **Authentication**:
   - Go to Authentication > Sign-in method
   - Enable "Anonymous" sign-in provider

2. **Cloud Firestore**:
   - Go to Firestore Database
   - Create database (start in test mode for development)
   - Set up security rules as needed

### 4. Import Sample Data

To import the sample menu data into Firestore:

1. Go to Firebase Console > Firestore Database
2. Create a collection named `foodItems`
3. Import the data from `SampleData.json`:
   - You can manually add documents, or
   - Use the Firebase CLI: `firebase firestore:import SampleData.json`
   - Or use the Firebase Admin SDK

**Manual Import Steps**:
1. In Firestore, click "Start collection"
2. Name it `foodItems`
3. Add documents with the following fields:
   - `name` (string)
   - `price` (number)
   - `description` (string)
   - `imageUrl` (string)
   - `category` (string): "Appetizers", "Mains", or "Desserts"

### 5. Build and Run

1. Open the project in Android Studio Jellyfish or later
2. Sync Gradle files
3. Connect an Android device or start an emulator
4. Run the app

## Firestore Security Rules (Development)

For development, you can use these permissive rules:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /foodItems/{document=**} {
      allow read: if request.auth != null;
      allow write: if false;
    }
  }
}
```

## Sample Data

The `SampleData.json` file includes:
- **3 Pizzas**: Margherita, Pepperoni, Quattro Formaggi
- **3 Pastas**: Carbonara, Bolognese, Alfredo
- **4 Appetizers**: Caesar Salad, Caprese Salad, Garden Salad, Bruschetta, Garlic Bread
- **3 Desserts**: Tiramisu, Panna Cotta, Gelato Trio

## Dependencies

- AndroidX Core & AppCompat
- Material Components (Material Design 3)
- Firebase BOM 32.7.0
- Firebase Auth
- Firebase Firestore
- Glide 4.16.0 (Image loading)

## License

This is a proof-of-concept application for demonstration purposes.
