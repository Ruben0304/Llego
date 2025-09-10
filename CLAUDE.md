# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Kotlin Multiplatform project using Compose Multiplatform targeting Android, iOS, and Desktop (JVM). The project name is "Llego" with the package namespace `com.llego.multiplatform`.

## Build Commands

### Android
```bash
# Windows
.\gradlew.bat :composeApp:assembleDebug

# macOS/Linux  
./gradlew :composeApp:assembleDebug
```

### Desktop (JVM)
```bash
# Windows
.\gradlew.bat :composeApp:run

# macOS/Linux
./gradlew :composeApp:run
```

### iOS
Open the `iosApp` directory in Xcode and run from there, or use the run configuration in your IDE.

## Project Structure

- `/composeApp/src/commonMain/kotlin` - Shared code for all platforms
- `/composeApp/src/androidMain/kotlin` - Android-specific implementations
- `/composeApp/src/iosMain/kotlin` - iOS-specific implementations  
- `/composeApp/src/jvmMain/kotlin` - Desktop JVM-specific implementations
- `/composeApp/src/commonTest/kotlin` - Shared test code
- `/iosApp` - iOS application entry point and SwiftUI code

## Key Dependencies

- Kotlin 2.2.10
- Compose Multiplatform 1.8.2
- Android Gradle Plugin 8.10.1
- Compose Hot Reload 1.0.0-beta06
- AndroidX Lifecycle 2.9.3
- Coil (Image loading) 3.0.0-alpha06
- Lottie Compose 6.6.7 (Android animations)

## Architecture Notes

The project uses a typical Compose Multiplatform structure with:
- **UI Architecture**: MVVM pattern with ViewModels for state management
- **State Management**: Uses `StateFlow` and `collectAsStateWithLifecycle()` for reactive UI updates
- **Component Structure**: Follows atomic design principles:
  - `atoms/` - Basic UI elements (CartButton, CategoryItem, CounterControls)
  - `molecules/` - Composite components (ProductCard, SearchBar, StoreCard)  
  - `organisms/` - Complex sections (ProductsSection, SemicircularSlider, Section)
  - `background/` - Background components (CurvedBackground)
- **Platform-specific implementations** in respective `*Main` folders
- **Shared UI code** in `commonMain` using Compose with Material 3 theming
- **Entry points**: `MainActivity.kt` (Android), `MainViewController.kt` (iOS), `main.kt` (Desktop)
- **Main UI component** in `App.kt` with custom `LlegoTheme`
- **Platform detection** through `Platform.kt` interface with platform-specific implementations
- **Data layer**: Repository pattern for data access (`ProductRepository`, `CategoryRepository`)
- **Models**: Data classes in `/data/model/` (Product)
- **State management**: UI state classes in `/ui/state/` (HomeScreenState, UiState)

## Testing

Tests are located in `commonTest` and use `kotlin-test` framework. Run tests with:
```bash
.\gradlew.bat test  # Windows
./gradlew test      # macOS/Linux
```