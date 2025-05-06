# Google Maps Demo

This repository showcases a demo Android application built using Kotlin and the Google Maps API. The app demonstrates various features and functionalities available in Google Maps, providing an excellent guide to developers looking to integrate maps into their Android applications.

## Features

### 1. Basic Maps Features
- **Markers**: Add markers to specific locations on the map.
- **Marker Windows**: Display additional information when a marker is clicked.
- **Custom Markers**: Use custom icons or images as markers.

### 2. Polylines and Polygons
- **Polylines**: Draw lines connecting multiple points on the map.
- **Polygons**: Highlight areas by drawing shapes with multiple points.

### 3. Directions and Distance
- Get directions between two or more points.
- Calculate the distance between points.

### 4. Travel Modes
- Support for different travel modes:
  - Driving
  - Walking
  - Bicycling
  - Transit

### 5. Map Styling
- Customize the appearance of the map to match your app's theme using JSON styling.

### 6. Advanced Data Handling
- **Places**: Search and display places of interest.
- **GeoCoding**: Convert addresses into geographic coordinates and vice versa.
- **Clustering**: Handle large datasets by combining nearby markers into clusters.

### 7. Offline Maps / Caching
- Enable offline maps to provide a seamless experience in areas with limited or no internet connectivity.

## Getting Started

### Prerequisites
- A Google Cloud Platform (GCP) account.
- Google Maps API key. Enable the following APIs:
  - Maps SDK for Android
  - Directions API
  - Places API

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/hpcreator/MapComposeDemo.git
   ```

2. Open the project in **Android Studio**.

3. Add your Google Maps API Key:
   - Navigate to `res/values/strings.xml`.
   - Replace `mapKey` with your actual API key.

4. Sync the project with Gradle files.

### Running the Application
1. Connect your Android device or start an emulator.
2. Run the app using **Android Studio**.

## Screenshots
- Yet to be added...

## Technologies Used
- **Language**: Kotlin
- **Framework**: Android SDK
- **APIs**: Google Maps API, Directions API, Places API

## Contributing
Contributions are welcome! Please follow the steps below to contribute:
1. Fork the repository.
2. Create a new branch: `git checkout -b feature-name`.
3. Commit your changes: `git commit -m 'Add some feature'`.
4. Push to the branch: `git push origin feature-name`.
5. Open a pull request.

## Acknowledgments
- Google Maps API
- Open-source libraries and tools used in this project

---

Feel free to explore, fork, and contribute to this project. Together, let's make map integration simpler and more powerful!
