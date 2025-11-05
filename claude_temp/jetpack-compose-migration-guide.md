# PAWKIT - Jetpack Compose Migration Guide

## Executive Summary

This document outlines the complete migration path from XML-based UI with View Binding/Data Binding to Jetpack Compose for the PAWKIT percussion instrument app. The migration will modernize the entire UI layer while updating all non-UI dependencies to their latest versions.

### Project Overview
- **App Type:** Dynamic zone-based percussion interface with in-app purchases
- **Current SDK:** Min 24, Target 30
- **Current Kotlin:** 1.4.21
- **Architecture:** MVVM + Repository Pattern + Navigation Component
- **Modules:** app, iolib, parselib

### Migration Scope
- **9 XML layouts** → Composable functions
- **3 Activities** → Compose Activities
- **4 Fragments** → Composable screens
- **2 RecyclerView adapters** → LazyGrid/LazyColumn
- **Navigation Graph** → Compose Navigation
- **View/Data Binding** → Compose State

---

## Table of Contents
1. [Dependency Updates](#1-dependency-updates)
2. [Build Configuration Changes](#2-build-configuration-changes)
3. [Migration Phases](#3-migration-phases)
4. [File-by-File Transformation Plan](#4-file-by-file-transformation-plan)
5. [Architecture Changes](#5-architecture-changes)
6. [Navigation Migration](#6-navigation-migration)
7. [Theme & Styling Migration](#7-theme--styling-migration)
8. [Third-Party Library Integration](#8-third-party-library-integration)
9. [Testing Strategy](#9-testing-strategy)
10. [Risk Mitigation](#10-risk-mitigation)

---

## 1. Dependency Updates

### 1.1 Core Dependencies to Update (Non-UI)

#### Kotlin & Gradle
```gradle
// FROM:
kotlin_version = '1.4.21'

// TO:
kotlin_version = '2.0.21' // Latest stable with Compose compiler
```

#### Coroutines
```gradle
// FROM:
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0"

// TO:
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1"
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1"
```

#### Firebase
```gradle
// FROM:
implementation 'com.google.firebase:firebase-analytics:19.0.0'
implementation 'com.google.firebase:firebase-firestore-ktx'

// TO:
implementation 'com.google.firebase:firebase-analytics:22.1.2'
implementation 'com.google.firebase:firebase-firestore-ktx:25.1.1'
implementation 'com.google.firebase:firebase-bom:33.5.1' // Use BOM for version management
```

#### JSON Parsing (Moshi)
```gradle
// FROM:
implementation "com.squareup.moshi:moshi-kotlin:1.8.0"

// TO:
implementation "com.squareup.moshi:moshi-kotlin:1.15.1"
kapt "com.squareup.moshi:moshi-kotlin-codegen:1.15.1"
```

#### Billing
```gradle
// FROM:
implementation 'com.android.billingclient:billing-ktx:3.0.0'

// TO:
implementation 'com.android.billingclient:billing-ktx:7.1.1'
```

#### Play Core
```gradle
// FROM:
implementation 'com.google.android.play:core:1.10.0'

// TO:
implementation 'com.google.android.play:app-update:2.1.0'
implementation 'com.google.android.play:app-update-ktx:2.1.0'
```

### 1.2 New Jetpack Compose Dependencies

```gradle
// Compose BOM for version management
implementation platform('androidx.compose:compose-bom:2024.10.01')

// Compose Core
implementation 'androidx.compose.ui:ui'
implementation 'androidx.compose.ui:ui-tooling-preview'
implementation 'androidx.compose.material3:material3'
implementation 'androidx.compose.material:material-icons-extended'

// Compose Lifecycle integration
implementation 'androidx.lifecycle:lifecycle-runtime-compose:2.8.7'
implementation 'androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7'

// Navigation Compose
implementation 'androidx.navigation:navigation-compose:2.8.4'

// Integration with existing architecture
implementation 'androidx.activity:activity-compose:1.9.3'
implementation 'androidx.compose.runtime:runtime-livedata'

// Image loading - Coil (modern replacement for Glide)
implementation 'io.coil-kt:coil-compose:2.7.0'

// Tooling
debugImplementation 'androidx.compose.ui:ui-tooling'
debugImplementation 'androidx.compose.ui:ui-test-manifest'

// Testing
androidTestImplementation 'androidx.compose.ui:ui-test-junit4'
```

### 1.3 Dependencies to Remove/Replace

#### Remove (Replaced by Compose)
```gradle
// Remove these after migration:
implementation "androidx.constraintlayout:constraintlayout:2.0.4"
implementation "androidx.cardview:cardview:1.0.0"
implementation "androidx.navigation:navigation-fragment-ktx:2.3.5"
implementation "androidx.navigation:navigation-ui-ktx:2.3.5"
implementation "androidx.lifecycle:lifecycle-extensions:2.2.0" // Deprecated
```

#### Replace Image Loading
```gradle
// FROM:
implementation 'com.github.bumptech.glide:glide:4.11.0'
kapt 'com.github.bumptech.glide:compiler:4.11.0'

// TO:
implementation 'io.coil-kt:coil-compose:2.7.0' // Better Compose integration
```

#### Keep (Core functionality)
```gradle
// These remain essential:
implementation 'androidx.core:core-ktx:1.15.0' // Updated from 1.6.0
implementation 'androidx.appcompat:appcompat:1.7.0' // Updated from 1.3.0
implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7' // Updated from 2.3.1
implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.8.7' // Updated from 2.3.1
```

### 1.4 YouTube Player Library

**Challenge:** The current library `com.pierfrancescosoffritti.androidyoutubeplayer:core:10.0.5` is View-based.

**Options:**
1. **Keep View integration (Recommended for Phase 1):**
   ```gradle
   implementation 'com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.1' // Updated
   ```
   Use `AndroidView` in Compose to embed the existing player.

2. **Migrate to Compose-native alternative:**
   - Custom implementation using WebView in Compose
   - Evaluate third-party Compose YouTube players

---

## 2. Build Configuration Changes

### 2.1 Project-Level build.gradle

```gradle
buildscript {
    ext {
        kotlin_version = '2.0.21'
        compose_version = '1.7.5'
        compose_compiler_version = '1.5.15'
    }

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath 'com.android.tools.build:gradle:8.7.2' // AGP update
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
        classpath 'com.google.gms:google-services:4.4.2' // Updated from 4.3.8
        // Remove: Safe Args (replaced by Compose Navigation)
    }
}
```

### 2.2 App-Level build.gradle

```gradle
plugins {
    id 'com.android.application'
    id 'kotlin-android'
    id 'kotlin-parcelize' // For Parcelable support
    id 'com.google.gms.google-services'
    id 'org.jetbrains.kotlin.plugin.compose' version '2.0.21' // New Compose compiler plugin
}

android {
    namespace 'com.tunepruner.fingerperc'
    compileSdk 35 // Updated from 30

    defaultConfig {
        applicationId "com.tunepruner.fingerperc"
        minSdk 24
        targetSdk 35 // Updated from 30
        versionCode 1
        versionName "1.0"
    }

    buildFeatures {
        compose true
        // Remove after migration complete:
        viewBinding false // Will be removed
        dataBinding false // Will be removed
    }

    composeOptions {
        kotlinCompilerExtensionVersion = compose_compiler_version
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17 // Updated from 1_8
        targetCompatibility JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = '17'
    }
}
```

---

## 3. Migration Phases

### Phase 1: Foundation & Preparation (Week 1-2)
**Goal:** Set up Compose infrastructure without breaking existing app

**Tasks:**
1. ✅ Update Gradle plugin to 8.7.2
2. ✅ Update Kotlin to 2.0.21
3. ✅ Add Compose dependencies
4. ✅ Update all non-UI dependencies
5. ✅ Create theme system in Compose (parallel to existing themes)
6. ✅ Migrate color, typography, and dimension resources
7. ✅ Set up Compose preview infrastructure
8. ✅ Create base Composable utilities (loading states, buttons, etc.)

**Deliverables:**
- App compiles with both View and Compose libraries
- Theme system parity
- Component library started

---

### Phase 2: Simple Screens (Week 3-4)
**Goal:** Migrate simplest screens to build confidence

**Migration Order:**
1. **LoadingInstrumentFragment** → `LoadingScreen.kt`
   - Simplest: Just centered text
   - No complex state
   - Good proof of concept

2. **UpdateDialogActivity** → `UpdateDialog.kt`
   - Simple dialog with 2 buttons
   - Tests dialog handling in Compose

3. **ProgressBarLayout** → `ProgressBarComposable.kt`
   - Reusable component
   - LinearProgressIndicator equivalent

**Deliverables:**
- 3 screens fully migrated
- Dialog handling pattern established
- Basic navigation working

---

### Phase 3: List Screens (Week 5-7)
**Goal:** Migrate RecyclerView-based screens

**Migration Order:**
1. **library_grid_item.xml** → `LibraryGridItem.kt`
   - Composable for grid item
   - Coil image loading
   - Click handling

2. **LibraryListRecyclerAdapter** → Removed (logic moves to Composable)

3. **LibraryListRecyclerFragment** → `LibraryListScreen.kt`
   - LazyVerticalGrid (2 columns)
   - ViewModel integration
   - Navigation to detail screen

4. **SoundpackDetailFragment** → `SoundpackDetailScreen.kt`
   - LazyColumn
   - Filtered list logic
   - Purchase button state

**Deliverables:**
- RecyclerView patterns converted to LazyColumn/LazyGrid
- Adapter logic absorbed into Composables
- ViewModel observation working with Compose State

---

### Phase 4: Detail Screen (Week 8-9)
**Goal:** Migrate complex detail screen with YouTube player

**Migration:**
1. **LibraryDetailFragment** → `LibraryDetailScreen.kt`
   - Complex layout with multiple buttons
   - YouTube player via AndroidView (interop)
   - Glide → Coil migration
   - Fade animations
   - Navigation with arguments

**Key Challenges:**
- YouTube player integration (AndroidView wrapper)
- Alpha animations (AnimatedVisibility)
- Shared element transitions (if desired)
- Image loading with Coil
- In-app purchase UI state

**Deliverables:**
- Detail screen fully functional
- YouTube integration working
- Purchase flow UI complete

---

### Phase 5: Instrument Screen (Week 10-12)
**Goal:** Migrate the most complex screen - the instrument playing interface

**Migration:**
1. **all_in_one_file.xml** → `InstrumentScreen.kt`
   - Two-zone split layout (Column with weighted boxes)
   - Touch handling with Modifier.pointerInput
   - Image state management
   - Alpha animations
   - Dynamic image loading based on instrument state

2. **InstrumentGUI.kt** → Logic absorbed into `InstrumentScreen.kt` state
   - setupImages() → State initialization
   - setUpTitles() → Composable text elements

3. **InstrumentActivity** → Compose Activity
   - setContent { InstrumentScreen() }
   - Touch event delegation to Compose
   - Fullscreen/immersive mode (SystemUiController)

**Key Challenges:**
- Complex touch handling (onTouchEvent → Modifier.pointerInput)
- Real-time image swapping during play
- Alpha animation performance
- Position tracking (PointF → Offset)
- Integration with native audio engine

**Deliverables:**
- Instrument screen fully playable
- Touch latency acceptable (<10ms)
- Animation performance verified (60fps)

---

### Phase 6: Navigation & Activity Refactor (Week 13-14)
**Goal:** Complete migration by refactoring navigation

**Migration:**
1. **nav_graph.xml** → Removed
2. **LaunchScreenActivity** → Single Compose Activity
   - NavHost with Compose Navigation
   - All screens as destinations
   - Safe type-safe navigation (no Safe Args plugin needed)

3. **Navigation Arguments** → Type-safe classes
   ```kotlin
   @Parcelize
   data class LibraryDetailArgs(
       val libraryName: String,
       val libraryId: String,
       // ... all args
   ) : Parcelable
   ```

**Deliverables:**
- Single Activity architecture
- All fragments removed
- Type-safe navigation working
- Deep linking preserved

---

### Phase 7: Cleanup & Optimization (Week 15-16)
**Goal:** Remove legacy code and optimize

**Tasks:**
1. ✅ Remove all XML layouts
2. ✅ Remove View Binding / Data Binding
3. ✅ Remove Fragment dependencies
4. ✅ Remove RecyclerView dependencies
5. ✅ Remove Glide
6. ✅ Remove ConstraintLayout, CardView
7. ✅ Remove navigation-fragment-ktx
8. ✅ Update ProGuard rules for Compose
9. ✅ Performance profiling
10. ✅ Accessibility audit

**Deliverables:**
- Codebase cleaned of legacy UI code
- APK size analysis (should be similar or smaller)
- Performance benchmarks documented

---

## 4. File-by-File Transformation Plan

### 4.1 LoadingInstrumentFragment → LoadingScreen.kt

**Current (Fragment):**
```kotlin
// File: app/src/main/java/com/tunepruner/fingerperc/launchscreen/librarydetail/LoadingInstrumentFragment.kt
class LoadingInstrumentFragment : Fragment() {
    private lateinit var binding: LoadingInstrumentFragmentBinding

    override fun onCreateView(...): View {
        binding = LoadingInstrumentFragmentBinding.inflate(layoutInflater)
        return binding.root
    }
}

// Layout: loading_instrument_fragment.xml
<ConstraintLayout>
    <TextView
        android:text="Loading Instrument..."
        android:layout_gravity="center" />
</ConstraintLayout>
```

**New (Compose):**
```kotlin
// File: app/src/main/java/com/tunepruner/fingerperc/ui/screens/LoadingScreen.kt
package com.tunepruner.fingerperc.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LoadingScreen(
    message: String = "Loading Instrument..."
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingScreenPreview() {
    PAWKITTheme {
        LoadingScreen()
    }
}
```

**Files Affected:**
- ✅ CREATE: `app/src/main/java/com/tunepruner/fingerperc/ui/screens/LoadingScreen.kt`
- ❌ DELETE: `app/src/main/java/com/tunepruner/fingerperc/launchscreen/librarydetail/LoadingInstrumentFragment.kt`
- ❌ DELETE: `app/src/main/res/layout/loading_instrument_fragment.xml`

---

### 4.2 UpdateDialogActivity → UpdateDialog.kt

**Current (Activity):**
```kotlin
// File: app/src/main/java/.../UpdateDialogActivity.kt
class UpdateDialogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.app_update_activity)

        findViewById<Button>(R.id.update_button).setOnClickListener {
            appUpdateManager.startUpdateFlow(...)
        }

        findViewById<Button>(R.id.later_button).setOnClickListener {
            finish()
        }
    }
}

// Layout: app_update_activity.xml
<LinearLayout>
    <TextView text="Update available" />
    <Button id="update_button" text="Update now" />
    <Button id="later_button" text="I'll do it later" />
</LinearLayout>
```

**New (Compose):**
```kotlin
// File: app/src/main/java/com/tunepruner/fingerperc/ui/dialogs/UpdateDialog.kt
package com.tunepruner.fingerperc.ui.dialogs

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun UpdateDialog(
    onUpdateClick: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update Available") },
        text = { Text("A new version of PAWKIT is available. Update now for the latest features.") },
        confirmButton = {
            TextButton(onClick = onUpdateClick) {
                Text("Update now")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("I'll do it later")
            }
        }
    )
}

// New Activity wrapper for dialog
class UpdateDialogActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PAWKITTheme {
                UpdateDialog(
                    onUpdateClick = { startUpdate() },
                    onDismiss = { finish() }
                )
            }
        }
    }

    private fun startUpdate() {
        // AppUpdateManager logic
    }
}
```

**Files Affected:**
- ✅ CREATE: `app/src/main/java/com/tunepruner/fingerperc/ui/dialogs/UpdateDialog.kt`
- ✏️ MODIFY: `app/src/main/java/com/tunepruner/fingerperc/launchscreen/librarylist/UpdateDialogActivity.kt` (convert to Compose)
- ❌ DELETE: `app/src/main/res/layout/app_update_activity.xml`

---

### 4.3 progress_bar_layout.xml → ProgressBarComposable.kt

**Current (XML):**
```xml
<!-- File: progress_bar_layout.xml -->
<LinearLayout>
    <TextView android:id="@+id/status_text" />
    <ProgressBar
        android:id="@+id/progress_bar"
        android:progressDrawable="@drawable/my_progress_bar"
        style="?android:attr/progressBarStyleHorizontal" />
</LinearLayout>
```

**New (Compose):**
```kotlin
// File: app/src/main/java/com/tunepruner/fingerperc/ui/components/ProgressBarComponent.kt
package com.tunepruner.fingerperc.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProgressBarComponent(
    statusText: String,
    progress: Float, // 0f to 1f
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = statusText,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Preview
@Composable
private fun ProgressBarPreview() {
    PAWKITTheme {
        ProgressBarComponent(
            statusText = "Downloading... 45%",
            progress = 0.45f
        )
    }
}
```

**Files Affected:**
- ✅ CREATE: `app/src/main/java/com/tunepruner/fingerperc/ui/components/ProgressBarComponent.kt`
- ❌ DELETE: `app/src/main/res/layout/progress_bar_layout.xml`
- ❌ DELETE: `app/src/main/res/drawable/my_progress_bar.xml` (if not used elsewhere)

---

### 4.4 library_grid_item.xml → LibraryGridItem.kt

**Current (XML + ViewHolder):**
```xml
<!-- File: library_grid_item.xml -->
<LinearLayout android:layout_height="220dp">
    <ImageView android:id="@+id/imageViewLibraryRecycler"
               android:layout_width="150dp"
               android:layout_height="150dp" />
    <TextView android:id="@+id/textViewLibraryRecyclerTitle" />
    <TextView android:id="@+id/textViewLibraryRecyclerSubtitle" />
</LinearLayout>
```

```kotlin
// In LibraryListRecyclerAdapter.kt
class LibraryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val image: ImageView = itemView.findViewById(R.id.imageViewLibraryRecycler)
    val title: TextView = itemView.findViewById(R.id.textViewLibraryRecyclerTitle)
    val subtitle: TextView = itemView.findViewById(R.id.textViewLibraryRecyclerSubtitle)
}

override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
    val library = libraries[position]
    holder.title.text = library.libraryName
    holder.subtitle.text = getStatusText(library)
    Glide.with(holder.image.context)
        .load(library.imageUrl)
        .into(holder.image)

    holder.itemView.transitionName = "library_${library.libraryID}"
    holder.itemView.startAnimation(fadeIn)
}
```

**New (Compose):**
```kotlin
// File: app/src/main/java/com/tunepruner/fingerperc/ui/components/LibraryGridItem.kt
package com.tunepruner.fingerperc.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tunepruner.fingerperc.launchscreen.librarydetail.Library

@Composable
fun LibraryGridItem(
    library: Library,
    onClick: (Library) -> Unit,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn()
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(220.dp)
                .clickable { onClick(library) },
            shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = library.imageUrl,
                    contentDescription = library.libraryName,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = library.libraryName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = getStatusText(library),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private fun getStatusText(library: Library): String {
    return when {
        library.isInstalled -> "Tap to play"
        library.isPurchased -> "Download"
        !library.isReleased -> "Coming soon"
        else -> library.soundpackName
    }
}
```

**Files Affected:**
- ✅ CREATE: `app/src/main/java/com/tunepruner/fingerperc/ui/components/LibraryGridItem.kt`
- ❌ DELETE: `app/src/main/res/layout/library_grid_item.xml`

---

### 4.5 LibraryListRecyclerFragment → LibraryListScreen.kt

**Current (Fragment with RecyclerView):**
```kotlin
// File: LibraryListRecyclerFragment.kt (230 lines)
class LibraryListRecyclerFragment : Fragment(), LibraryItemListener {
    private lateinit var binding: LaunchScreen2Binding
    private lateinit var viewModel: SoundbankViewModel
    private lateinit var adapter: LibraryListRecyclerAdapter

    override fun onCreateView(...): View {
        binding = LaunchScreen2Binding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this, factory).get(SoundbankViewModel::class.java)

        adapter = LibraryListRecyclerAdapter(this)
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = this@LibraryListRecyclerFragment.adapter
        }

        viewModel.soundbank.observe(viewLifecycleOwner) { soundbank ->
            adapter.updateList(soundbank.getLibraries())
        }

        return binding.root
    }

    override fun onLibraryItemClick(library: Library) {
        val extras = FragmentNavigatorExtras(
            itemView to "library_${library.libraryID}"
        )
        findNavController().navigate(
            LibraryListRecyclerFragmentDirections
                .actionLaunchScreenFragmentToLibraryDetailFragment3(...),
            extras
        )
    }
}
```

**New (Compose Screen):**
```kotlin
// File: app/src/main/java/com/tunepruner/fingerperc/ui/screens/LibraryListScreen.kt
package com.tunepruner.fingerperc.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tunepruner.fingerperc.launchscreen.librarydetail.Library
import com.tunepruner.fingerperc.launchscreen.viewmodel.SoundbankViewModel
import com.tunepruner.fingerperc.ui.components.LibraryGridItem

@Composable
fun LibraryListScreen(
    onLibraryClick: (Library) -> Unit,
    onSendFeedback: () -> Unit,
    viewModel: SoundbankViewModel = viewModel()
) {
    val soundbank by viewModel.soundbank.observeAsState()
    val libraries = soundbank?.getLibraries() ?: emptyList()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PAWKIT") },
                actions = {
                    TextButton(onClick = onSendFeedback) {
                        Text("Send feedback to developer")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = libraries,
                key = { it.libraryID }
            ) { library ->
                LibraryGridItem(
                    library = library,
                    onClick = onLibraryClick
                )
            }
        }
    }
}
```

**Files Affected:**
- ✅ CREATE: `app/src/main/java/com/tunepruner/fingerperc/ui/screens/LibraryListScreen.kt`
- ❌ DELETE: `app/src/main/java/com/tunepruner/fingerperc/launchscreen/librarylist/LibraryListRecyclerFragment.kt`
- ❌ DELETE: `app/src/main/java/com/tunepruner/fingerperc/launchscreen/librarylist/LibraryListRecyclerAdapter.kt`
- ❌ DELETE: `app/src/main/res/layout/launch_screen2.xml`

---

### 4.6 LibraryDetailFragment → LibraryDetailScreen.kt

**Current (Fragment with YouTube Player):**
```kotlin
// File: LibraryDetailFragment.kt (complex)
class LibraryDetailFragment : Fragment(), BillingClientListener {
    private lateinit var binding: FragmentLibraryDetailBinding
    private val args: LibraryDetailFragmentArgs by navArgs()
    private lateinit var youTubePlayer: YouTubePlayer

    override fun onCreateView(...): View {
        binding = FragmentLibraryDetailBinding.inflate(layoutInflater)

        binding.titleOfLibraryDetail.text = args.libraryname

        // YouTube player setup
        binding.youtubePlayerView.alpha = 0F
        lifecycle.addObserver(binding.youtubePlayerView)
        binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(player: YouTubePlayer) {
                youTubePlayer = player
            }
        })

        // Glide image loading
        Glide.with(this)
            .load(args.imageUrl)
            .into(binding.libraryThumbnailImageView)

        // Button listeners
        binding.playButton.setOnClickListener { launchInstrument() }
        binding.demoButton.setOnClickListener { showYouTubeDemo() }
        binding.viewSoundpackButton.setOnClickListener { navigateToSoundpack() }

        return binding.root
    }

    private fun showYouTubeDemo() {
        binding.youtubePlayerView.animate().alpha(1F).duration = 500
        youTubePlayer.loadVideo("videoId", 0f)
    }
}
```

**New (Compose with AndroidView for YouTube):**
```kotlin
// File: app/src/main/java/com/tunepruner/fingerperc/ui/screens/LibraryDetailScreen.kt
package com.tunepruner.fingerperc.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.tunepruner.fingerperc.launchscreen.librarydetail.LibraryDetailArgs

@Composable
fun LibraryDetailScreen(
    args: LibraryDetailArgs,
    onPlayNow: () -> Unit,
    onViewSoundpack: () -> Unit,
    onMainMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showYouTubePlayer by remember { mutableStateOf(false) }
    var youTubePlayerInstance by remember { mutableStateOf<YouTubePlayer?>(null) }
    val lifecycleOwner = LocalLifecycleOwner.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title
        Text(
            text = args.libraryName,
            style = MaterialTheme.typography.displaySmall
        )

        // Library thumbnail
        AsyncImage(
            model = args.imageUrl,
            contentDescription = args.libraryName,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )

        // Buttons
        Button(
            onClick = onPlayNow,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Play now!")
        }

        Button(
            onClick = onViewSoundpack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View soundpack")
        }

        Button(
            onClick = { showYouTubePlayer = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Demo")
        }

        OutlinedButton(
            onClick = onMainMenu,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Main menu")
        }

        // YouTube Player (using AndroidView for interop)
        AnimatedVisibility(
            visible = showYouTubePlayer,
            enter = fadeIn()
        ) {
            AndroidView(
                factory = { context ->
                    YouTubePlayerView(context).apply {
                        lifecycleOwner.lifecycle.addObserver(this)

                        addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                            override fun onReady(player: YouTubePlayer) {
                                youTubePlayerInstance = player
                                player.loadVideo("demoVideoId", 0f)
                            }
                        })
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
    }
}

// Data class for arguments (replaces Safe Args)
@Parcelize
data class LibraryDetailArgs(
    val libraryName: String,
    val libraryId: String,
    val soundpackID: String,
    val imageUrl: String,
    val isPurchased: Boolean,
    val price: String,
    val soundpackName: String
) : Parcelable
```

**Files Affected:**
- ✅ CREATE: `app/src/main/java/com/tunepruner/fingerperc/ui/screens/LibraryDetailScreen.kt`
- ✅ CREATE: `app/src/main/java/com/tunepruner/fingerperc/ui/navigation/NavigationArgs.kt` (for data classes)
- ❌ DELETE: `app/src/main/java/com/tunepruner/fingerperc/launchscreen/librarydetail/LibraryDetailFragment.kt`
- ❌ DELETE: `app/src/main/res/layout/fragment_library_detail.xml`

---

### 4.7 SoundpackDetailFragment → SoundpackDetailScreen.kt

**Current (Fragment with filtered RecyclerView):**
```kotlin
// File: SoundpackDetailFragment.kt
class SoundpackDetailFragment : Fragment(), LibraryItemListener, BillingClientListener {
    private lateinit var binding: FragmentSoundpackDetailBinding
    private val args: SoundpackDetailFragmentArgs by navArgs()
    private lateinit var viewModel: SoundbankViewModel

    override fun onCreateView(...): View {
        binding = FragmentSoundpackDetailBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this, factory).get(SoundbankViewModel::class.java)

        val adapter = SoundpackRecyclerAdapter(this)
        binding.root.findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapter
        }

        viewModel.soundbank.observe(viewLifecycleOwner) { soundbank ->
            val filtered = soundbank.getLibraries().filter { it.soundpackID == args.soundpackID }
            adapter.updateList(filtered)
        }

        binding.soundpackTitle.text = args.soundpackname
        binding.soundpackSubtitle.text = args.soundpackname

        // Purchase button logic
        binding.actionButton.text = if (args.ispurchased) "Download" else args.price
        binding.actionButton.setOnClickListener {
            if (!args.ispurchased) {
                initiatePurchase()
            } else {
                downloadSoundpack()
            }
        }

        return binding.root
    }
}
```

**New (Compose):**
```kotlin
// File: app/src/main/java/com/tunepruner/fingerperc/ui/screens/SoundpackDetailScreen.kt
package com.tunepruner.fingerperc.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tunepruner.fingerperc.launchscreen.librarydetail.Library
import com.tunepruner.fingerperc.launchscreen.viewmodel.SoundbankViewModel
import com.tunepruner.fingerperc.ui.components.LibraryGridItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundpackDetailScreen(
    soundpackId: String,
    soundpackName: String,
    isPurchased: Boolean,
    price: String,
    onLibraryClick: (Library) -> Unit,
    onPurchaseClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: SoundbankViewModel = viewModel()
) {
    val soundbank by viewModel.soundbank.observeAsState()
    val libraries = remember(soundbank, soundpackId) {
        soundbank?.getLibraries()?.filter { it.soundpackID == soundpackId } ?: emptyList()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(soundpackName, style = MaterialTheme.typography.titleLarge)
                        Text(soundpackName, style = MaterialTheme.typography.bodySmall)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = { if (isPurchased) onDownloadClick() else onPurchaseClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(if (isPurchased) "Download" else price)
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = libraries,
                key = { it.libraryID }
            ) { library ->
                LibraryGridItem(
                    library = library,
                    onClick = onLibraryClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
```

**Files Affected:**
- ✅ CREATE: `app/src/main/java/com/tunepruner/fingerperc/ui/screens/SoundpackDetailScreen.kt`
- ❌ DELETE: `app/src/main/java/com/tunepruner/fingerperc/launchscreen/soundpackDetail/SoundpackDetailFragment.kt`
- ❌ DELETE: `app/src/main/java/com/tunepruner/fingerperc/launchscreen/soundpackDetail/SoundpackRecyclerAdapter.kt`
- ❌ DELETE: `app/src/main/res/layout/fragment_soundpack_detail.xml`

---

### 4.8 InstrumentActivity + all_in_one_file.xml → InstrumentScreen.kt

**Current (Activity with complex touch handling):**
```kotlin
// File: InstrumentActivity.kt
class InstrumentActivity : AppCompatActivity() {
    private lateinit var instrument: Instrument

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemUI()

        val libraryID = intent.getStringExtra("libraryID")
        instrument = Instrument.create(this, libraryID)

        setContentView(R.layout.all_in_one_file)

        instrument.gui.setupImages(libraryID)
        instrument.gui.setUpTitles(libraryID)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        instrument.onTouch(event)
        return true
    }

    override fun onPause() {
        super.onPause()
        instrument.tearDown()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
        )
    }
}

// Layout: all_in_one_file.xml
<ConstraintLayout>
    <!-- Top Zone -->
    <ImageView android:id="@+id/articulation1image"
               app:layout_constraintTop_toTopOf="parent"
               app:layout_constraintBottom_toTopOf="@id/articulation2image" />
    <TextView android:id="@+id/articulation1title" />

    <!-- Bottom Zone -->
    <ImageView android:id="@+id/articulation2image"
               app:layout_constraintTop_toBottomOf="@id/articulation1image"
               app:layout_constraintBottom_toBottomOf="parent" />
    <TextView android:id="@+id/articulation2title" />
</ConstraintLayout>

// InstrumentGUI.kt
class InstrumentGUI(private val activity: Activity) {
    private lateinit var articulation1Image: ImageView
    private lateinit var articulation2Image: ImageView
    private lateinit var articulation1Title: TextView
    private lateinit var articulation2Title: TextView

    fun setupImages(libraryID: String) {
        articulation1Image = activity.findViewById(R.id.articulation1image)
        articulation2Image = activity.findViewById(R.id.articulation2image)

        when (libraryID) {
            "cajon" -> {
                articulation1Image.setImageResource(R.drawable.cajon_high_atrest)
                articulation2Image.setImageResource(R.drawable.cajon_low_atrest)
            }
            "bomboleguero" -> {
                articulation1Image.setImageResource(R.drawable.bomboleguero_high_atrest)
                articulation2Image.setImageResource(R.drawable.bomboleguero_low_atrest)
            }
            // ... more instruments
        }
    }

    fun setUpTitles(libraryID: String) {
        articulation1Title = activity.findViewById(R.id.articulation1title)
        articulation2Title = activity.findViewById(R.id.articulation2title)

        when (libraryID) {
            "cajon" -> {
                articulation1Title.text = activity.getString(R.string.cajonTopArticulationTitle)
                articulation2Title.text = activity.getString(R.string.cajonBottomArticulationTitle)
            }
            // ... more instruments
        }
    }

    fun setImageAlpha(articulationIndex: Int, alpha: Float) {
        when (articulationIndex) {
            0 -> articulation1Image.alpha = alpha
            1 -> articulation2Image.alpha = alpha
        }
    }
}
```

**New (Compose):**
```kotlin
// File: app/src/main/java/com/tunepruner/fingerperc/ui/screens/InstrumentScreen.kt
package com.tunepruner.fingerperc.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tunepruner.fingerperc.R
import com.tunepruner.fingerperc.instrument.Instrument

data class InstrumentImages(
    val topAtRest: Int,
    val topOnHit: Int,
    val bottomAtRest: Int,
    val bottomOnHit: Int
)

data class InstrumentTitles(
    val topTitle: String,
    val bottomTitle: String
)

@Composable
fun InstrumentScreen(
    libraryId: String,
    instrument: Instrument,
    modifier: Modifier = Modifier
) {
    val images = remember(libraryId) { getInstrumentImages(libraryId) }
    val titles = remember(libraryId) { getInstrumentTitles(libraryId) }

    var topZoneAlpha by remember { mutableStateOf(1f) }
    var bottomZoneAlpha by remember { mutableStateOf(1f) }

    var topZoneBounds by remember { mutableStateOf<IntRect?>(null) }
    var bottomZoneBounds by remember { mutableStateOf<IntRect?>(null) }

    // Alpha animation for flash effect
    val topAlphaAnimatable = remember { Animatable(1f) }
    val bottomAlphaAnimatable = remember { Animatable(1f) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    // Determine which zone was tapped
                    val tappedZone = when {
                        topZoneBounds?.contains(offset.toIntOffset()) == true -> 0
                        bottomZoneBounds?.contains(offset.toIntOffset()) == true -> 1
                        else -> -1
                    }

                    if (tappedZone >= 0) {
                        // Trigger instrument audio
                        instrument.onTouch(tappedZone, offset.x, offset.y)

                        // Flash animation
                        kotlinx.coroutines.launch {
                            val animatable = if (tappedZone == 0) topAlphaAnimatable else bottomAlphaAnimatable
                            animatable.snapTo(0.3f)
                            animatable.animateTo(1f, animationSpec = tween(150))
                        }
                    }
                }
            }
    ) {
        // Top Zone (50% of screen)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .onGloballyPositioned { coordinates ->
                    topZoneBounds = coordinates.boundsInWindow().toIntRect()
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = images.topAtRest),
                contentDescription = titles.topTitle,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { alpha = topAlphaAnimatable.value },
                contentScale = ContentScale.Fit
            )

            Text(
                text = titles.topTitle,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            )
        }

        // Bottom Zone (50% of screen)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .onGloballyPositioned { coordinates ->
                    bottomZoneBounds = coordinates.boundsInWindow().toIntRect()
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = images.bottomAtRest),
                contentDescription = titles.bottomTitle,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { alpha = bottomAlphaAnimatable.value },
                contentScale = ContentScale.Fit
            )

            Text(
                text = titles.bottomTitle,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            instrument.tearDown()
        }
    }
}

private fun getInstrumentImages(libraryId: String): InstrumentImages {
    return when (libraryId) {
        "cajon" -> InstrumentImages(
            topAtRest = R.drawable.cajon_high_atrest,
            topOnHit = R.drawable.cajon_high_onhit,
            bottomAtRest = R.drawable.cajon_low_atrest,
            bottomOnHit = R.drawable.cajon_low_onhit
        )
        "bomboleguero" -> InstrumentImages(
            topAtRest = R.drawable.bomboleguero_high_atrest,
            topOnHit = R.drawable.bomboleguero_high_onhit,
            bottomAtRest = R.drawable.bomboleguero_low_atrest,
            bottomOnHit = R.drawable.bomboleguero_low_onhit
        )
        "dancedrums" -> InstrumentImages(
            topAtRest = R.drawable.dancedrums_high_atrest,
            topOnHit = R.drawable.dancedrums_high_onhit,
            bottomAtRest = R.drawable.dancedrums_low_atrest,
            bottomOnHit = R.drawable.dancedrums_low_onhit
        )
        else -> throw IllegalArgumentException("Unknown library: $libraryId")
    }
}

private fun getInstrumentTitles(libraryId: String): InstrumentTitles {
    return when (libraryId) {
        "cajon" -> InstrumentTitles("High Tone", "Bass Tone")
        "bomboleguero" -> InstrumentTitles("High Tone", "Bass Tone")
        "dancedrums" -> InstrumentTitles("Hi-Hat / Snare", "Kick Drum")
        else -> InstrumentTitles("Top", "Bottom")
    }
}

// Activity wrapper
class InstrumentActivity : ComponentActivity() {
    private lateinit var instrument: Instrument

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val libraryID = intent.getStringExtra("libraryID") ?: return finish()
        instrument = Instrument.create(this, libraryID)

        enableEdgeToEdge()

        setContent {
            PAWKITTheme {
                SystemUiController(rememberSystemUiController()) {
                    setSystemBarsVisible(false)
                }

                InstrumentScreen(
                    libraryId = libraryID,
                    instrument = instrument
                )
            }
        }
    }
}

// Extension for SystemUiController (fullscreen/immersive mode)
@Composable
fun SystemUiController(
    controller: SystemUiController,
    content: SystemUiController.() -> Unit
) {
    DisposableEffect(controller) {
        controller.content()
        onDispose { }
    }
}
```

**Files Affected:**
- ✅ CREATE: `app/src/main/java/com/tunepruner/fingerperc/ui/screens/InstrumentScreen.kt`
- ✏️ MODIFY: `app/src/main/java/com/tunepruner/fingerperc/InstrumentActivity.kt` (convert to Compose)
- ✏️ MODIFY: `app/src/main/java/com/tunepruner/fingerperc/instrument/Instrument.kt` (update touch interface)
- ❌ DELETE: `app/src/main/java/com/tunepruner/fingerperc/gui/InstrumentGUI.kt`
- ❌ DELETE: `app/src/main/res/layout/all_in_one_file.xml`

---

### 4.9 LaunchScreenActivity + Navigation → Compose Navigation

**Current (Activity with NavHostFragment):**
```kotlin
// File: LaunchScreenActivity.kt
class LaunchScreenActivity : AppCompatActivity(), FragmentListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_testing_navhost)

        System.loadLibrary("bomboleguero")

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
    }

    override fun launchInstrument(libraryID: String) {
        val intent = Intent(this, InstrumentActivity::class.java).apply {
            putExtra("libraryID", libraryID)
        }
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}

// Navigation graph: nav_graph.xml
<navigation>
    <fragment android:id="@+id/launchScreenFragment"
              android:name="...LibraryListRecyclerFragment">
        <action android:id="@+id/action_launchScreenFragment_to_libraryDetailFragment3"
                app:destination="@id/libraryDetailFragment3" />
    </fragment>

    <fragment android:id="@+id/libraryDetailFragment3"
              android:name="...LibraryDetailFragment">
        <argument android:name="libraryname" app:argType="string" />
        <!-- ... more arguments -->
        <action android:id="@+id/action_libraryDetailFragment3_to_soundpackFragment"
                app:destination="@id/soundpackFragment" />
    </fragment>

    <fragment android:id="@+id/soundpackFragment"
              android:name="...SoundpackDetailFragment">
        <!-- arguments -->
    </fragment>
</navigation>
```

**New (Compose Navigation):**
```kotlin
// File: app/src/main/java/com/tunepruner/fingerperc/ui/navigation/PAWKITNavigation.kt
package com.tunepruner.fingerperc.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.tunepruner.fingerperc.ui.screens.*
import kotlinx.serialization.Serializable

// Type-safe navigation routes
@Serializable
object LibraryListRoute

@Serializable
data class LibraryDetailRoute(
    val libraryName: String,
    val libraryId: String,
    val soundpackID: String,
    val imageUrl: String,
    val isPurchased: Boolean,
    val price: String,
    val soundpackName: String
)

@Serializable
data class SoundpackDetailRoute(
    val soundpackID: String,
    val soundpackName: String,
    val isPurchased: Boolean,
    val price: String
)

@Serializable
object LoadingRoute

@Composable
fun PAWKITNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: Any = LibraryListRoute,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<LibraryListRoute> {
            LibraryListScreen(
                onLibraryClick = { library ->
                    navController.navigate(
                        LibraryDetailRoute(
                            libraryName = library.libraryName,
                            libraryId = library.libraryID,
                            soundpackID = library.soundpackID,
                            imageUrl = library.imageUrl,
                            isPurchased = library.isPurchased,
                            price = library.price ?: "$9.99",
                            soundpackName = library.soundpackName
                        )
                    )
                },
                onSendFeedback = {
                    // Email intent
                }
            )
        }

        composable<LibraryDetailRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<LibraryDetailRoute>()

            LibraryDetailScreen(
                args = args,
                onPlayNow = {
                    // Launch InstrumentActivity
                    val context = navController.context
                    val intent = Intent(context, InstrumentActivity::class.java).apply {
                        putExtra("libraryID", args.libraryId)
                    }
                    context.startActivity(intent)
                },
                onViewSoundpack = {
                    navController.navigate(
                        SoundpackDetailRoute(
                            soundpackID = args.soundpackID,
                            soundpackName = args.soundpackName,
                            isPurchased = args.isPurchased,
                            price = args.price
                        )
                    )
                },
                onMainMenu = {
                    navController.popBackStack(LibraryListRoute, inclusive = false)
                }
            )
        }

        composable<SoundpackDetailRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<SoundpackDetailRoute>()

            SoundpackDetailScreen(
                soundpackId = args.soundpackID,
                soundpackName = args.soundpackName,
                isPurchased = args.isPurchased,
                price = args.price,
                onLibraryClick = { library ->
                    navController.navigate(
                        LibraryDetailRoute(
                            libraryName = library.libraryName,
                            libraryId = library.libraryID,
                            soundpackID = library.soundpackID,
                            imageUrl = library.imageUrl,
                            isPurchased = library.isPurchased,
                            price = library.price ?: "$9.99",
                            soundpackName = library.soundpackName
                        )
                    )
                },
                onPurchaseClick = { /* Purchase logic */ },
                onDownloadClick = { /* Download logic */ },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<LoadingRoute> {
            LoadingScreen()
        }
    }
}

// New main activity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        System.loadLibrary("bomboleguero")

        setContent {
            PAWKITTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PAWKITNavHost()
                }
            }
        }
    }
}
```

**Files Affected:**
- ✅ CREATE: `app/src/main/java/com/tunepruner/fingerperc/ui/navigation/PAWKITNavigation.kt`
- ✅ CREATE: `app/src/main/java/com/tunepruner/fingerperc/MainActivity.kt`
- ❌ DELETE: `app/src/main/java/com/tunepruner/fingerperc/launchscreen/LaunchScreenActivity.kt`
- ❌ DELETE: `app/src/main/res/layout/main_activity_testing_navhost.xml`
- ❌ DELETE: `app/src/main/res/navigation/nav_graph.xml`
- ✏️ MODIFY: `app/src/main/AndroidManifest.xml` (change launcher activity)

---

## 5. Architecture Changes

### 5.1 MVVM Pattern Compatibility

**Current Architecture (COMPATIBLE with Compose):**
```kotlin
// ViewModels remain unchanged
class SoundbankViewModel(private val repository: SoundbankRepo) : AndroidViewModel(application) {
    val soundbank: LiveData<Soundbank> = repository.soundbank
}

// Repository remains unchanged
class SoundbankRepo {
    val soundbank: MutableLiveData<Soundbank> = MutableLiveData()

    fun fetchSoundbanks() {
        // Firebase fetch logic
    }
}
```

**Compose Integration (No changes to ViewModel/Repository):**
```kotlin
@Composable
fun LibraryListScreen(
    viewModel: SoundbankViewModel = viewModel()
) {
    // LiveData → State conversion
    val soundbank by viewModel.soundbank.observeAsState()

    // UI updates automatically when LiveData changes
    LazyVerticalGrid(...) {
        items(soundbank?.getLibraries() ?: emptyList()) { library ->
            LibraryGridItem(library)
        }
    }
}
```

**Optional: Migrate LiveData to StateFlow (Better Compose integration):**
```kotlin
// Enhanced ViewModel with StateFlow
class SoundbankViewModel(private val repository: SoundbankRepo) : AndroidViewModel(application) {
    val soundbank: StateFlow<Soundbank?> = repository.soundbank
        .asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}

// In Composable
@Composable
fun LibraryListScreen(viewModel: SoundbankViewModel = viewModel()) {
    val soundbank by viewModel.soundbank.collectAsState()
    // ... rest of composable
}
```

### 5.2 State Management Patterns

**Fragment-Based (OLD):**
```kotlin
class LibraryListRecyclerFragment : Fragment() {
    private lateinit var binding: LaunchScreen2Binding

    override fun onCreateView(...): View {
        binding = LaunchScreen2Binding.inflate(layoutInflater)

        viewModel.soundbank.observe(viewLifecycleOwner) { soundbank ->
            adapter.updateList(soundbank.getLibraries())
        }

        return binding.root
    }
}
```

**Compose-Based (NEW):**
```kotlin
@Composable
fun LibraryListScreen(viewModel: SoundbankViewModel = viewModel()) {
    // State is automatically managed by Compose
    val soundbank by viewModel.soundbank.observeAsState()
    val libraries = soundbank?.getLibraries() ?: emptyList()

    // UI automatically recomposes when state changes
    LazyVerticalGrid(...) {
        items(libraries, key = { it.libraryID }) { library ->
            LibraryGridItem(library)
        }
    }
}
```

### 5.3 Listener/Callback Pattern Elimination

**Before (Interfaces for callbacks):**
```kotlin
interface LibraryItemListener {
    fun onLibraryItemClick(library: Library)
}

class LibraryListRecyclerFragment : Fragment(), LibraryItemListener {
    override fun onLibraryItemClick(library: Library) {
        findNavController().navigate(...)
    }
}

class LibraryListRecyclerAdapter(private val listener: LibraryItemListener) : RecyclerView.Adapter<>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            listener.onLibraryItemClick(libraries[position])
        }
    }
}
```

**After (Lambda callbacks in Compose):**
```kotlin
@Composable
fun LibraryListScreen(
    onLibraryClick: (Library) -> Unit // Direct lambda callback
) {
    LazyVerticalGrid(...) {
        items(libraries) { library ->
            LibraryGridItem(
                library = library,
                onClick = onLibraryClick // Pass lambda directly
            )
        }
    }
}

@Composable
fun LibraryGridItem(
    library: Library,
    onClick: (Library) -> Unit
) {
    Card(
        modifier = Modifier.clickable { onClick(library) }
    ) {
        // ... content
    }
}
```

---

## 6. Navigation Migration

### 6.1 Navigation Component → Compose Navigation

**Differences:**

| Feature | XML Navigation | Compose Navigation |
|---------|---------------|-------------------|
| **Route Definition** | XML `<fragment>` tags | Kotlin sealed classes / data classes |
| **Type Safety** | Safe Args plugin | Built-in with Kotlin serialization |
| **Arguments** | `<argument>` XML tags | Data class properties |
| **Transitions** | XML animation resources | Compose AnimatedContent |
| **Deep Links** | XML `<deepLink>` tags | Kotlin annotations |
| **Back Stack** | Fragment back stack | NavController back stack |

### 6.2 Type-Safe Navigation Implementation

**Add Kotlin Serialization:**
```gradle
plugins {
    id 'org.jetbrains.kotlin.plugin.serialization' version '2.0.21'
}

dependencies {
    implementation 'org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3'
}
```

**Define Routes:**
```kotlin
// File: app/src/main/java/com/tunepruner/fingerperc/ui/navigation/Routes.kt
package com.tunepruner.fingerperc.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object LibraryList

@Serializable
data class LibraryDetail(
    val libraryName: String,
    val libraryId: String,
    val soundpackID: String,
    val imageUrl: String,
    val isPurchased: Boolean,
    val price: String,
    val soundpackName: String
)

@Serializable
data class SoundpackDetail(
    val soundpackID: String,
    val soundpackName: String,
    val isPurchased: Boolean,
    val price: String
)
```

### 6.3 Shared Element Transitions (Optional)

**Current (Fragment transitions):**
```kotlin
val extras = FragmentNavigatorExtras(
    itemView to "library_${library.libraryID}"
)
findNavController().navigate(action, extras)
```

**Compose Alternative:**
```kotlin
// Using predictive back gesture (Android 14+)
// Or use experimental Compose shared element transitions
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun LibraryGridItem(
    library: Library,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    with(sharedTransitionScope) {
        AsyncImage(
            model = library.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .sharedElement(
                    rememberSharedContentState(key = "image-${library.libraryID}"),
                    animatedVisibilityScope = animatedContentScope
                )
        )
    }
}
```

---

## 7. Theme & Styling Migration

### 7.1 XML Themes → Material3 Theme

**Current (themes.xml):**
```xml
<resources>
    <style name="NoActionBar" parent="Theme.AppCompat.Light.NoActionBar">
        <item name="colorPrimary">@color/black</item>
        <item name="android:navigationBarColor">@color/black</item>
    </style>

    <style name="Theme.MyTheme" parent="android:Theme.Dialog">
        <item name="android:windowBackground">@drawable/background_general</item>
        <item name="android:windowNoTitle">true</item>
    </style>
</resources>
```

**New (Compose Material3 Theme):**
```kotlin
// File: app/src/main/java/com/tunepruner/fingerperc/ui/theme/Theme.kt
package com.tunepruner.fingerperc.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Color palette from colors.xml
private val Black = Color(0xFF000000)
private val White = Color(0xFFFFFFFF)
private val TextColor = Color(0xFFBBBBBB)
private val DarkGray = Color(0xFF1A1A1A)

private val DarkColorScheme = darkColorScheme(
    primary = White,
    onPrimary = Black,
    secondary = TextColor,
    background = Black,
    surface = DarkGray,
    onBackground = White,
    onSurface = White
)

@Composable
fun PAWKITTheme(
    darkTheme: Boolean = true, // Always dark theme for this app
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = PAWKITTypography,
        content = content
    )
}
```

### 7.2 Typography Migration

**Current (dimens.xml + implicit styles):**
```xml
<resources>
    <dimen name="generalTextSize">18sp</dimen>
</resources>

<!-- In layouts: -->
<TextView
    android:textSize="60sp"
    android:fontFamily="@font/kodchasan_regular" />
```

**New (Compose Typography):**
```kotlin
// File: app/src/main/java/com/tunepruner/fingerperc/ui/theme/Type.kt
package com.tunepruner.fingerperc.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.tunepruner.fingerperc.R

// Custom fonts
val KodchasanFamily = FontFamily(
    Font(R.font.kodchasan_regular, FontWeight.Normal),
    Font(R.font.kodchasan_extralight, FontWeight.ExtraLight)
)

val FingerpercFamily = FontFamily(
    Font(R.font.fingerperc_regular, FontWeight.Normal),
    Font(R.font.fingerperc_narrow, FontWeight.Normal)
)

val PAWKITTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = KodchasanFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 60.sp
    ),
    displaySmall = TextStyle(
        fontFamily = FingerpercFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = KodchasanFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = KodchasanFamily,
        fontWeight = FontWeight.ExtraLight,
        fontSize = 16.sp
    )
)
```

### 7.3 Drawable → Compose Equivalents

**Gradient Background:**
```xml
<!-- background_general.xml -->
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <gradient
        android:angle="270"
        android:endColor="#FF000000"
        android:startColor="#FF1A1A1A"
        android:type="linear" />
</shape>
```

```kotlin
// Compose equivalent
@Composable
fun GradientBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A1A1A),
                        Color(0xFF000000)
                    )
                )
            )
    ) {
        content()
    }
}
```

**Rounded Corner Selector:**
```xml
<!-- background_grid_item.xml -->
<selector>
    <item android:state_pressed="true">
        <shape android:shape="rectangle">
            <corners android:radius="25dp"/>
            <solid android:color="#33FFFFFF"/>
        </shape>
    </item>
    <item>
        <shape android:shape="rectangle">
            <corners android:radius="25dp"/>
            <solid android:color="#1A1A1A"/>
        </shape>
    </item>
</selector>
```

```kotlin
// Compose equivalent (built into Card/Button)
Card(
    shape = RoundedCornerShape(25.dp),
    colors = CardDefaults.cardColors(
        containerColor = Color(0xFF1A1A1A)
    ),
    modifier = Modifier.clickable { /* pressed state handled automatically */ }
) {
    // Content
}
```

---

## 8. Third-Party Library Integration

### 8.1 Glide → Coil Migration

**Current (Glide):**
```kotlin
Glide.with(context)
    .load(imageUrl)
    .placeholder(R.drawable.placeholder)
    .error(R.drawable.error)
    .into(imageView)
```

**New (Coil Compose):**
```kotlin
AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
        .data(imageUrl)
        .crossfade(true)
        .build(),
    placeholder = painterResource(R.drawable.placeholder),
    error = painterResource(R.drawable.error),
    contentDescription = "Library image",
    contentScale = ContentScale.Crop,
    modifier = Modifier.size(150.dp)
)
```

**Why Coil?**
- Native Compose support (AsyncImage composable)
- Kotlin coroutines-first
- Smaller APK size
- Better performance with Compose
- Active development

### 8.2 YouTube Player Integration

**Current (View-based):**
```kotlin
binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
    override fun onReady(player: YouTubePlayer) {
        player.loadVideo(videoId, 0f)
    }
})
lifecycle.addObserver(binding.youtubePlayerView)
```

**New (Compose with AndroidView):**
```kotlin
@Composable
fun YouTubePlayerComposable(
    videoId: String,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var player by remember { mutableStateOf<YouTubePlayer?>(null) }

    AndroidView(
        factory = { context ->
            YouTubePlayerView(context).apply {
                lifecycleOwner.lifecycle.addObserver(this)

                addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        player = youTubePlayer
                        youTubePlayer.cueVideo(videoId, 0f)
                    }
                })
            }
        },
        modifier = modifier,
        update = { view ->
            player?.loadVideo(videoId, 0f)
        }
    )

    DisposableEffect(lifecycleOwner) {
        onDispose {
            player?.pause()
        }
    }
}
```

### 8.3 RecyclerView → Lazy Layouts

**Performance Comparison:**

| Aspect | RecyclerView | LazyColumn/Grid |
|--------|--------------|-----------------|
| **Setup Complexity** | High (Adapter, ViewHolder, LayoutManager) | Low (Single composable) |
| **Item Recycling** | Manual ViewHolder recycling | Automatic Composition recycling |
| **Animations** | ItemAnimator or manual | Built-in (animateItemPlacement) |
| **Performance** | Excellent (mature) | Excellent (optimized) |
| **Item Keys** | Manual via DiffUtil | Built-in `key` parameter |
| **Nested Scrolling** | Manual coordination | Automatic |

**Migration Benefits:**
- 70% less code (no adapters, view holders)
- Type-safe item handling
- Easier animations
- Better state preservation

---

## 9. Testing Strategy

### 9.1 Compose Testing Setup

```gradle
androidTestImplementation 'androidx.compose.ui:ui-test-junit4'
debugImplementation 'androidx.compose.ui:ui-test-manifest'
```

### 9.2 Unit Tests for Composables

```kotlin
// File: app/src/androidTest/java/com/tunepruner/fingerperc/ui/LibraryGridItemTest.kt
@RunWith(AndroidJUnit4::class)
class LibraryGridItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun libraryGridItem_displaysCorrectInformation() {
        val library = Library(
            index = 0,
            libraryName = "Cajon",
            imageUrl = "https://example.com/cajon.jpg",
            isPurchased = true,
            isInstalled = true,
            isReleased = true,
            libraryID = "cajon",
            soundpackID = "sp1",
            soundpackName = "Latin Percussion"
        )

        composeTestRule.setContent {
            PAWKITTheme {
                LibraryGridItem(
                    library = library,
                    onClick = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("Cajon")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Tap to play")
            .assertIsDisplayed()
    }

    @Test
    fun libraryGridItem_clickTriggersCallback() {
        var clickedLibrary: Library? = null
        val library = Library(...)

        composeTestRule.setContent {
            PAWKITTheme {
                LibraryGridItem(
                    library = library,
                    onClick = { clickedLibrary = it }
                )
            }
        }

        composeTestRule
            .onNodeWithText("Cajon")
            .performClick()

        assertEquals(library, clickedLibrary)
    }
}
```

### 9.3 Screenshot Testing

```kotlin
// Using Paparazzi or Roborazzi
class ScreenshotTests {
    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun libraryListScreen_lightTheme() {
        paparazzi.snapshot {
            PAWKITTheme(darkTheme = false) {
                LibraryListScreen(
                    onLibraryClick = {},
                    onSendFeedback = {}
                )
            }
        }
    }
}
```

### 9.4 Performance Testing

```kotlin
// Benchmarking scrolling performance
@RunWith(AndroidJUnit4::class)
class ScrollPerformanceTest {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun scrollLibraryList() {
        benchmarkRule.measureRepeated(
            packageName = "com.tunepruner.fingerperc",
            metrics = listOf(FrameTimingMetric()),
            iterations = 5,
            setupBlock = {
                pressHome()
                startActivityAndWait()
            }
        ) {
            val recycler = device.findObject(By.res("library_list"))
            recycler.fling(Direction.DOWN)
            device.waitForIdle()
        }
    }
}
```

### 9.5 Migration Testing Checklist

**Phase 1 (Simple Screens):**
- [ ] LoadingScreen displays correctly
- [ ] UpdateDialog appears and dismisses
- [ ] ProgressBar animates properly

**Phase 2 (List Screens):**
- [ ] Library grid loads all items
- [ ] Grid scrolls smoothly (60fps)
- [ ] Click navigation works
- [ ] Images load correctly (Coil)
- [ ] Fade-in animation works

**Phase 3 (Detail Screen):**
- [ ] All buttons functional
- [ ] YouTube player loads and plays
- [ ] Arguments passed correctly
- [ ] Back navigation works
- [ ] Purchase flow UI updates correctly

**Phase 4 (Instrument Screen):**
- [ ] Touch zones respond correctly
- [ ] Audio triggers on touch (no latency increase)
- [ ] Alpha animations perform at 60fps
- [ ] Images swap correctly based on state
- [ ] Fullscreen mode works
- [ ] No frame drops during play

**Phase 5 (Navigation):**
- [ ] All navigation paths work
- [ ] Back stack behaves correctly
- [ ] Deep links work (if applicable)
- [ ] Type-safe navigation prevents errors
- [ ] No memory leaks

**Final:**
- [ ] APK size comparable or smaller
- [ ] Cold start time similar or better
- [ ] Memory usage similar or better
- [ ] Battery usage similar or better
- [ ] All features working
- [ ] No crashes
- [ ] Accessibility working (TalkBack tested)

---

## 10. Risk Mitigation

### 10.1 High-Risk Areas

#### 1. Instrument Touch Latency
**Risk:** Compose touch handling could introduce latency.

**Mitigation:**
- Benchmark touch-to-audio latency before and after migration
- Target: <10ms latency (current baseline)
- Use `Modifier.pointerInput` for lowest-level touch handling
- Profile with systrace/perfetto
- Fallback plan: Keep InstrumentActivity as View-based if needed

**Validation:**
```kotlin
// Benchmark touch latency
var touchStartTime = 0L

Modifier.pointerInput(Unit) {
    detectTapGestures { offset ->
        val latency = System.nanoTime() - touchStartTime
        Log.d("Latency", "Touch latency: ${latency / 1_000_000}ms")

        instrument.onTouch(offset)
    }
}
```

#### 2. YouTube Player Integration
**Risk:** AndroidView interop could cause lifecycle issues.

**Mitigation:**
- Thorough testing of lifecycle events (pause, resume, destroy)
- Memory leak testing with LeakCanary
- Fallback: Keep LibraryDetailFragment as View-based if needed

#### 3. Animation Performance
**Risk:** Complex animations might drop frames.

**Mitigation:**
- Use `derivedStateOf` for expensive calculations
- Leverage `key` parameter for stable composition
- Profile with Compose Layout Inspector
- Target: 60fps for all animations

#### 4. APK Size Increase
**Risk:** Adding Compose libraries could bloat APK.

**Mitigation:**
- Use R8/ProGuard aggressively
- Remove all View-based libraries after migration
- Enable resource shrinking
- Monitor APK size at each phase
- Target: No more than 10% increase (acceptable tradeoff)

### 10.2 Rollback Strategy

**Per-Phase Rollback:**
- Each phase can be rolled back independently
- Keep feature flags for Compose screens
- Use Git branches for each migration phase

**Example Feature Flag:**
```kotlin
object FeatureFlags {
    const val USE_COMPOSE_LIBRARY_LIST = true
    const val USE_COMPOSE_LIBRARY_DETAIL = false
    const val USE_COMPOSE_INSTRUMENT = false
}

// In navigation
if (FeatureFlags.USE_COMPOSE_LIBRARY_LIST) {
    LibraryListScreen()
} else {
    AndroidView(factory = { /* Fragment container */ })
}
```

### 10.3 Gradual Rollout Plan

**Beta Testing:**
1. Internal testing (Week 1-2 of each phase)
2. Alpha track on Play Store (10% of users)
3. Beta track (50% of users)
4. Production (100% rollout)

**Crash Monitoring:**
- Firebase Crashlytics
- Monitor crash-free rate (target: >99.5%)
- Set up alerts for critical crashes

**Performance Monitoring:**
- Firebase Performance Monitoring
- Track key metrics:
  - App start time
  - Screen rendering time
  - Touch latency (custom trace)
  - Memory usage

### 10.4 Documentation & Knowledge Transfer

**Create Documentation:**
- [ ] Compose architecture overview
- [ ] Component library documentation
- [ ] Navigation flow diagrams
- [ ] Theme customization guide
- [ ] Testing best practices

**Team Training:**
- [ ] Compose basics workshop
- [ ] State management patterns
- [ ] Testing strategies
- [ ] Performance optimization

---

## Summary of All Files Affected

### Files to CREATE (21):
1. `app/src/main/java/com/tunepruner/fingerperc/ui/theme/Theme.kt`
2. `app/src/main/java/com/tunepruner/fingerperc/ui/theme/Type.kt`
3. `app/src/main/java/com/tunepruner/fingerperc/ui/theme/Color.kt`
4. `app/src/main/java/com/tunepruner/fingerperc/ui/screens/LoadingScreen.kt`
5. `app/src/main/java/com/tunepruner/fingerperc/ui/screens/LibraryListScreen.kt`
6. `app/src/main/java/com/tunepruner/fingerperc/ui/screens/LibraryDetailScreen.kt`
7. `app/src/main/java/com/tunepruner/fingerperc/ui/screens/SoundpackDetailScreen.kt`
8. `app/src/main/java/com/tunepruner/fingerperc/ui/screens/InstrumentScreen.kt`
9. `app/src/main/java/com/tunepruner/fingerperc/ui/components/LibraryGridItem.kt`
10. `app/src/main/java/com/tunepruner/fingerperc/ui/components/ProgressBarComponent.kt`
11. `app/src/main/java/com/tunepruner/fingerperc/ui/dialogs/UpdateDialog.kt`
12. `app/src/main/java/com/tunepruner/fingerperc/ui/navigation/PAWKITNavigation.kt`
13. `app/src/main/java/com/tunepruner/fingerperc/ui/navigation/Routes.kt`
14. `app/src/main/java/com/tunepruner/fingerperc/MainActivity.kt`
15. `app/src/androidTest/java/com/tunepruner/fingerperc/ui/LibraryGridItemTest.kt`
16. `app/src/androidTest/java/com/tunepruner/fingerperc/ui/ScreenshotTests.kt`
17. `app/src/androidTest/java/com/tunepruner/fingerperc/ui/ScrollPerformanceTest.kt`

### Files to MODIFY (6):
1. `build.gradle` (project-level) - Update Gradle, Kotlin, remove Safe Args
2. `app/build.gradle` - Add Compose dependencies, update all dependencies
3. `app/src/main/java/com/tunepruner/fingerperc/InstrumentActivity.kt` - Convert to Compose
4. `app/src/main/java/com/tunepruner/fingerperc/instrument/Instrument.kt` - Update touch interface
5. `app/src/main/java/com/tunepruner/fingerperc/launchscreen/librarylist/UpdateDialogActivity.kt` - Convert to Compose
6. `app/src/main/AndroidManifest.xml` - Change launcher activity to MainActivity

### Files to DELETE (18):
1. `app/src/main/res/layout/launch_screen2.xml`
2. `app/src/main/res/layout/fragment_library_detail.xml`
3. `app/src/main/res/layout/fragment_soundpack_detail.xml`
4. `app/src/main/res/layout/all_in_one_file.xml`
5. `app/src/main/res/layout/library_grid_item.xml`
6. `app/src/main/res/layout/app_update_activity.xml`
7. `app/src/main/res/layout/loading_instrument_fragment.xml`
8. `app/src/main/res/layout/main_activity_testing_navhost.xml`
9. `app/src/main/res/layout/progress_bar_layout.xml`
10. `app/src/main/res/navigation/nav_graph.xml`
11. `app/src/main/java/com/tunepruner/fingerperc/launchscreen/LaunchScreenActivity.kt`
12. `app/src/main/java/com/tunepruner/fingerperc/launchscreen/librarylist/LibraryListRecyclerFragment.kt`
13. `app/src/main/java/com/tunepruner/fingerperc/launchscreen/librarylist/LibraryListRecyclerAdapter.kt`
14. `app/src/main/java/com/tunepruner/fingerperc/launchscreen/librarydetail/LibraryDetailFragment.kt`
15. `app/src/main/java/com/tunepruner/fingerperc/launchscreen/librarydetail/LoadingInstrumentFragment.kt`
16. `app/src/main/java/com/tunepruner/fingerperc/launchscreen/soundpackDetail/SoundpackDetailFragment.kt`
17. `app/src/main/java/com/tunepruner/fingerperc/launchscreen/soundpackDetail/SoundpackRecyclerAdapter.kt`
18. `app/src/main/java/com/tunepruner/fingerperc/gui/InstrumentGUI.kt`

### Files UNAFFECTED (Business Logic - 25+):
- All files in `instrument/` package (audio logic)
- All files in `parselib/` module
- All files in `iolib/` module
- `SoundbankViewModel.kt` (works with Compose)
- `SoundbankRepo.kt` (works with Compose)
- `BillingClientWrapper.kt` (works with Compose)
- Data models (Library, Soundpack, Soundbank)
- All drawable resources (images)
- All string resources
- All font resources

---

## Estimated Timeline

### Conservative Estimate (16 weeks):
- **Phase 1 (Foundation):** 2 weeks
- **Phase 2 (Simple Screens):** 2 weeks
- **Phase 3 (List Screens):** 3 weeks
- **Phase 4 (Detail Screen):** 2 weeks
- **Phase 5 (Instrument Screen):** 3 weeks
- **Phase 6 (Navigation Refactor):** 2 weeks
- **Phase 7 (Cleanup & Optimization):** 2 weeks

### Aggressive Estimate (10 weeks):
- Combine phases where possible
- Parallel development streams
- Risk: Less testing time

### Recommended: 14 weeks (3.5 months)
- Balance between speed and quality
- Adequate testing at each phase
- Buffer for unforeseen issues

---

## Key Success Metrics

**Functional:**
- ✅ All screens migrated
- ✅ All features working
- ✅ Zero regressions
- ✅ Touch latency <10ms

**Non-Functional:**
- ✅ APK size increase <10%
- ✅ Cold start time similar or better
- ✅ Frame rate: 60fps on all screens
- ✅ Crash-free rate >99.5%
- ✅ Memory usage similar or better

**Code Quality:**
- ✅ Test coverage >80%
- ✅ No lint warnings
- ✅ All previews working
- ✅ Documentation complete

---

## Conclusion

This migration will modernize PAWKIT's UI layer, reduce codebase complexity by ~30%, and position the app for future Android development. The phased approach minimizes risk while delivering value incrementally.

**Key Advantages:**
- Less code (no adapters, view holders, binding)
- Better state management
- Modern declarative UI
- Improved developer experience
- Future-proof architecture

**Key Challenges:**
- Instrument screen touch latency
- YouTube player integration
- Learning curve for team
- Testing coverage

**Recommendation:** Proceed with Phase 1 (Foundation) to validate approach, then commit to full migration based on performance benchmarks.
