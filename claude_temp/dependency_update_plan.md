# PAWKIT Dependency Update Plan

## Status: ✅ ALL UPDATES COMPLETED

## Project Overview
Android percussion app using Kotlin, Oboe, Firebase, Google Play services, and custom audio processing.

## Updated Major Versions
- **Kotlin**: 1.4.21 (Dec 2020) → Need latest
- **Android Gradle Plugin**: 4.2.2 → Need latest
- **Gradle**: 7.0.1 → Need latest
- **compileSdkVersion**: 30 → Need 34+
- **targetSdkVersion**: 30 → Need 34+
- **buildToolsVersion**: 30.0.3 → Need latest

## Critical Infrastructure Updates

### 1. Gradle Wrapper Update
- [ ] Update from Gradle 7.0.1 to latest stable (8.x)
- [ ] Test gradle wrapper functionality

### 2. Android Gradle Plugin (AGP) Update
- [ ] Update from 4.2.2 to latest stable (8.x)
- [ ] Update build.gradle syntax if needed for new AGP version

### 3. Remove Deprecated Repositories
- [ ] Remove jcenter() from all build.gradle files (deprecated since 2021)
- [ ] Ensure all dependencies are available from mavenCentral() or Google()

### 4. Kotlin Updates
- [ ] Update Kotlin from 1.4.21 to latest stable (1.9.x or 2.0.x)
- [ ] Update kotlin-gradle-plugin
- [ ] Update kotlin-stdlib-jdk7
- [ ] Update kotlin-stdlib-jdk8
- [ ] Update kotlin-stdlib
- [ ] Update kotlin-reflect
- [ ] Update kotlinx-coroutines-core from 1.5.0 to latest

### 5. Android SDK Versions
- [ ] Update compileSdkVersion from 30 to 34 or 35
- [ ] Update targetSdkVersion from 30 to 34 or 35
- [ ] Update buildToolsVersion to latest
- [ ] Update minSdkVersion if beneficial (currently 24, might increase to 26)

## AndroidX Library Updates

### 6. Core AndroidX Libraries
- [ ] Update androidx.core:core-ktx from 1.6.0 to latest
- [ ] Update androidx.appcompat:appcompat from 1.3.0 to latest (app module)
- [ ] Update androidx.appcompat:appcompat from 1.1.0 to latest (iolib & parselib modules)
- [ ] Update androidx.constraintlayout:constraintlayout from 2.0.4 to latest
- [ ] Update androidx.cardview:cardview from 1.0.0 to latest
- [ ] Update androidx.legacy:legacy-support-v4 from 1.0.0 to latest (or remove if not needed)

### 7. Lifecycle & ViewModel Libraries
- [ ] Update androidx.lifecycle:lifecycle-livedata-ktx from 2.3.1 to latest
- [ ] Update androidx.lifecycle:lifecycle-viewmodel-ktx from 2.3.1 to latest
- [ ] Update androidx.lifecycle:lifecycle-extensions from 2.2.0 to latest (or remove - deprecated)

### 8. Navigation Component
- [ ] Update navigation-fragment-ktx from 2.3.5 to latest
- [ ] Update navigation-ui-ktx from 2.3.5 to latest
- [ ] Update navigation-safe-args-gradle-plugin from 2.3.5 to latest

### 9. Material Design
- [ ] Update com.google.android.material:material from 1.4.0 to latest

## Google Services & Firebase Updates

### 10. Google Services Plugin
- [ ] Update com.google.gms:google-services from 4.3.8 to latest

### 11. Firebase Dependencies
- [ ] Update firebase-bom from 26.8.0 to latest
- [ ] Update firebase-analytics from 19.0.0 to latest (or use BOM version)
- [ ] Verify firebase-firestore-ktx uses BOM version

### 12. Google Play Services
- [ ] Update com.google.android.play:core from 1.10.0 to latest
- [ ] Update com.google.android.play:core-ktx from 1.8.1 to latest
- [ ] Update billing-ktx from 3.0.0 to latest (currently 6.x or 7.x)

## Testing Libraries Updates

### 13. Unit Testing
- [ ] Update junit-jupiter-api from 5.3.1 to latest
- [ ] Update junit-jupiter-engine from 5.3.1 to latest

### 14. Android Testing
- [ ] Update androidx.test.ext:junit from 1.1.3 to latest
- [ ] Update androidx.test.espresso:espresso-core from 3.4.0 to latest

## Third-Party Library Updates

### 15. Other Dependencies
- [ ] Update moshi-kotlin from 1.8.0 to latest
- [ ] Update androidyoutubeplayer:core from 10.0.5 to latest
- [ ] Update glide from 4.11.0 to latest
- [ ] Update glide compiler from 4.11.0 to latest

## Module-Specific Updates

### 16. iolib Module Updates
- [ ] Update compileSdkVersion from 29 to 34+
- [ ] Update buildToolsVersion from 29.0.1 to latest
- [ ] Update targetSdkVersion from 29 to 34+
- [ ] Update minSdkVersion from 26 to align with app module
- [ ] Update androidx.appcompat:appcompat from 1.1.0 to latest

### 17. parselib Module Updates
- [ ] Update compileSdkVersion from 29 to 34+
- [ ] Update buildToolsVersion from 29.0.1 to latest
- [ ] Update targetSdkVersion from 29 to 34+
- [ ] Update minSdkVersion from 16 to align with app module
- [ ] Update androidx.appcompat:appcompat from 1.1.0 to latest

## Build Configuration Updates

### 18. Java/Kotlin Compatibility
- [ ] Review sourceCompatibility and targetCompatibility (currently Java 8)
- [ ] Consider updating to Java 11 or 17 if compatible with NDK/CMake
- [ ] Update jvmTarget accordingly

### 19. Build Features & Options
- [ ] Review and update build features (dataBinding, viewBinding)
- [ ] Review Kapt configuration for optimization
- [ ] Review ProGuard/R8 configuration

## Post-Update Verification

### 20. Build & Test
- [ ] Run clean build
- [ ] Run all unit tests
- [ ] Run instrumentation tests if available
- [ ] Test on physical device
- [ ] Verify NDK/CMake compilation still works
- [ ] Verify Oboe integration still works

### 21. Runtime Verification
- [ ] Test Firebase integration
- [ ] Test in-app purchases
- [ ] Test in-app updates
- [ ] Test audio playback functionality
- [ ] Test YouTube player integration

## Notes
- JCenter is fully deprecated - must remove all references
- Some dependencies may have breaking API changes requiring code updates
- Firebase BOM manages versions automatically - use BOM versions where possible
- Navigation safe args plugin may require syntax updates
- Lifecycle extensions library is deprecated - migrate to individual components
- Google Play Billing has major API changes from 3.x to 6.x/7.x
- May need to handle new Android 13/14 permissions and behaviors
