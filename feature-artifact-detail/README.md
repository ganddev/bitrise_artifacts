# Feature Artifact Detail Module

## Übersicht

Das `feature-artifact-detail` Modul verwaltet die Anzeige und Interaktion mit APK/Artifact Details in einem BottomSheet. Es bietet eine plattformübergreifende Lösung für:

- **Artifact Anzeige**: Listet alle verfügbaren Artifacts für einen Build auf
- **URL-Öffnung**: Öffnet public install URLs im System Browser (platform-spezifisch)
- **State Management**: Vollständiges MVVM mit Koin DI

## Architektur

```
feature-artifact-detail-api/          # Public API
├── ArtifactDetailsBottomSheet.kt     # expect Composable

feature-artifact-detail/              # Implementation
├── presentation/
│   ├── ArtifactDetailsViewModel.kt   # Abstract ViewModel
│   ├── ArtifactDetailsViewModelImpl.kt# Concrete Implementation
│   ├── ArtifactDetailsUiState.kt
│   ├── ArtifactDetailsUiEvent.kt
│   └── ArtifactDetailsNavigationEvent.kt
├── domain/
│   ├── usecase/
│   │   ├── GetArtifactsListUseCase.kt
│   │   └── GetArtifactsListUseCaseImpl.kt
│   └── service/
│       └── UrlOpener.kt              # Platform interface
├── di/
│   └── ArtifactDetailModule.kt       # Koin configuration
├── androidMain/                      # Android implementations
├── iosMain/                          # iOS implementations
└── jvmMain/                          # JVM implementations
```

## Integration mit feature-builds

### 1. ViewModel Injection
BuildsScreen erhält den ArtifactDetailsViewModel:

```kotlin
@Composable
fun BuildsScreen(
    viewModel: BuildsViewModel,
    artifactDetailsViewModel: ArtifactDetailsViewModel
)
```

### 2. State Handling
BuildsUiState enthält `selectedBuildSlug` für die BottomSheet Anzeige:

```kotlin
data class Content(
    val builds: List<BuildItem>,
    val selectedBuildSlug: String? = null
) : BuildsUiState
```

### 3. Event Flow
```
BuildsScreen (clickable)
    ↓ BuildsUiEvent.OnBuildClicked
    ↓ BuildsViewModelImpl
    ↓ ArtifactDetailsUiEvent.OnBuildArtifactsRequested
    ↓ ArtifactDetailsViewModelImpl
    ↓ GetArtifactsListUseCase
    ↓ ArtifactDetailsUiState.Visible
    ↓ BuildsScreen (renders BottomSheet)
```

## Platform-spezifische Features

### Android
- Material 3 ModalBottomSheet
- `Intent.ACTION_VIEW` für URL-Öffnung
- APK-Download im System Browser

### iOS
- Custom Column Layout (kein ModalBottomSheet)
- `UIApplication.openURL()`

### JVM/Desktop
- Column Layout
- `Desktop.getDesktop().browse()`

## Verwendung

### In feature-builds

```kotlin
// Im BuildsViewModelImpl
override fun handleUiEvent(event: BuildsUiEvent) {
    when (event) {
        is BuildsUiEvent.OnBuildClicked -> {
            artifactDetailsViewModel.handleUiEvent(
                ArtifactDetailsUiEvent.OnBuildArtifactsRequested(
                    appSlug = appSlug,
                    buildSlug = event.buildSlug
                )
            )
        }
    }
}
```

### Im BuildsScreen

```kotlin
// Beim Artifact-Click
ArtifactDetailsBottomSheet(
    artifacts = artifactDetailsState.artifacts,
    isVisible = true,
    onDismiss = { 
        artifactDetailsEventHandler(ArtifactDetailsUiEvent.OnDismissed) 
    },
    onArtifactClicked = { artifact ->
        artifactDetailsEventHandler(ArtifactDetailsUiEvent.OnArtifactClicked(artifact))
    }
)
```

## Zukünftige Erweiterungen

- [ ] Detail-Screen für einzelnes Artifact
- [ ] Download-Progress Indikator
- [ ] Artifact-Filtration (z.B. nur APKs)
- [ ] Version-Code Anzeige (wenn verfügbar)
- [ ] Offline-Modus für gespeicherte URLs

