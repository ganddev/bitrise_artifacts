# Architecture Diagram - Artifact Details Feature

## Modul-Abhängigkeiten

```
┌─────────────────────────────────────────────────────────────────┐
│                      androidApp / iosApp                         │
└─────────────────┬───────────────────────────────────────────────┘
                  │
                  ▼
        ┌─────────────────────┐
        │  feature-builds     │
        │ (Presentation)      │
        └────┬────────────┬───┘
             │            │
             ▼            ▼
    ┌──────────────────┐  ┌──────────────────────────────┐
    │feature-artifacts │  │feature-artifact-detail       │◄──┐
    │       -api       │  │    (New Module)              │   │
    └──────────────────┘  └────────┬─────────────────────┘   │
                                   │                         │
                                   ▼                         │
                        ┌──────────────────────┐             │
                        │feature-artifact-     │             │
                        │detail-api (New)      │─────────────┘
                        └──────────────────────┘
```

## Feature-Builds Integration Flow

```
┌──────────────────────────────────────────────────────────────┐
│                    BuildsScreen                              │
│ ┌────────────────────────────────────────────────────────┐  │
│ │  BuildsList                                            │  │
│ │  ┌──────────────────────────────────────────────────┐ │  │
│ │  │ BuildItemRow (clickable)                         │ │  │
│ │  │ ┌────────────────────────────────────────────┐  │ │  │
│ │  │ │ onClick: BuildsUiEvent.OnBuildClicked()   │  │ │  │
│ │  │ └────────────────────────┬───────────────────┘  │ │  │
│ │  └────────────────────────┬─────────────────────────┘ │  │
│ └─────────────────────────┬──────────────────────────────┘  │
│                           │                                  │
│                           ▼                                  │
│ ┌──────────────────────────────────────────────────────┐  │
│ │ ArtifactDetailsBottomSheet                           │  │
│ │ (shown when state = Visible)                         │  │
│ └──────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────────┘
           │
           │ BuildsViewModelImpl handles OnBuildClicked
           ▼
┌──────────────────────────────────────────────────────────────┐
│          BuildsViewModelImpl                                  │
│  ┌──────────────────────────────────────────────────────┐  │
│  │ handleUiEvent(OnBuildClicked(buildSlug))             │  │
│  │   → artifactDetailsViewModel.handleUiEvent(          │  │
│  │       OnBuildArtifactsRequested(...)                 │  │
│  │     )                                                │  │
│  └──────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────────┘
           │
           ▼
┌──────────────────────────────────────────────────────────────┐
│      ArtifactDetailsViewModelImpl                             │
│  ┌──────────────────────────────────────────────────────┐  │
│  │ handleUiEvent(OnBuildArtifactsRequested)             │  │
│  │   → getArtifactsListUseCase(appSlug, buildSlug)      │  │
│  │   → setState(Visible(artifacts))                     │  │
│  └──────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────────┘
           │
           ▼
┌──────────────────────────────────────────────────────────────┐
│      ArtifactsRepository                                     │
│  ┌──────────────────────────────────────────────────────┐  │
│  │ getArtifacts(appSlug, buildSlug)                     │  │
│  │   → HTTP GET /apps/{app}/builds/{build}/artifacts   │  │
│  │   → Returns List<Artifact>                           │  │
│  └──────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────────┘
```

## User Interaction Flow

```
1. User Clicks Build Card
   │
   ├─ BuildsScreen renders BuildItemRow as clickable
   ├─ onClick → uiEventHandler(BuildsUiEvent.OnBuildClicked)
   │
   ▼
2. BuildsViewModelImpl processes event
   ├─ Updates state: selectedBuildSlug = buildSlug
   ├─ Calls: artifactDetailsViewModel.handleUiEvent(
   │           OnBuildArtifactsRequested(appSlug, buildSlug)
   │         )
   │
   ▼
3. ArtifactDetailsViewModelImpl processes event
   ├─ Calls: getArtifactsListUseCase(appSlug, buildSlug)
   ├─ Updates state: Visible(artifacts)
   │
   ▼
4. BuildsScreen receives new artifactDetailsState
   ├─ Renders ArtifactDetailsBottomSheet with artifacts
   │
   ▼
5. User Clicks Artifact with public_install_page_url
   ├─ onClick → artifactDetailsEventHandler(
   │             OnArtifactClicked(artifact)
   │           )
   │
   ▼
6. ArtifactDetailsViewModelImpl processes event
   ├─ Calls: urlOpener.openUrl(artifact.publicInstallPageUrl)
   │ 
   ├─ Android: Intent.ACTION_VIEW
   ├─ iOS: UIApplication.openURL()
   ├─ JVM: Desktop.getDesktop().browse()
   │
   ▼
7. System Browser Opens with Download URL
```

## DI Graph

```
┌─────────────────────────────────────────────────────────────┐
│                    Koin Module Graph                         │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  buildsModule {                                              │
│    ├─ includes(artifactDetailModule)                        │
│    │                                                        │
│    ├─ artifactDetailModule {                                │
│    │  ├─ single<UrlOpener> { provideUrlOpener() }           │
│    │  │   ├─ [Android] → AndroidUrlOpener(Context)          │
│    │  │   ├─ [iOS] → IosUrlOpener()                         │
│    │  │   └─ [JVM] → JvmUrlOpener()                         │
│    │  │                                                     │
│    │  ├─ factory<GetArtifactsListUseCase> {                 │
│    │  │   GetArtifactsListUseCaseImpl(get())                 │
│    │  │ }                                                   │
│    │  │                                                     │
│    │  └─ factory<ArtifactDetailsViewModel> {                │
│    │      ArtifactDetailsViewModelImpl(get(), get())         │
│    │      // get() = GetArtifactsListUseCase                │
│    │      // get() = UrlOpener                              │
│    │  }                                                     │
│    │                                                        │
│    ├─ single<ArtifactsRepository> { ... }                   │
│    ├─ factory<GetBuildsUseCase> { ... }                     │
│    └─ factory<BuildsViewModel> {                            │
│         BuildsViewModelImpl(                                 │
│           appSlug: String,                                  │
│           GetBuildsUseCase,                                 │
│           HasApkArtifactUseCase,                            │
│           ArtifactDetailsViewModel  ◄── NEW PARAM           │
│         )                                                   │
│       }                                                     │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

## State Management Layers

```
┌────────────────────────────────────────────────────────────┐
│  Layer 1: UI State (BuildsScreen)                          │
├────────────────────────────────────────────────────────────┤
│ BuildsUiState                                              │
│  ├─ Loading                                                │
│  ├─ Content(builds, selectedBuildSlug)  ◄── Track selected │
│  └─ Error(message)                                         │
│                                                            │
│ ArtifactDetailsUiState (collected in BuildsScreen)         │
│  ├─ Hidden                                                 │
│  └─ Visible(artifacts)  ◄── Render BottomSheet             │
└────────────────────────────────────────────────────────────┘
                     │
                     ▼
┌────────────────────────────────────────────────────────────┐
│  Layer 2: ViewModel State                                  │
├────────────────────────────────────────────────────────────┤
│ BuildsViewModelImpl._uiState: StateFlow<BuildsUiState>      │
│ BuildsViewModelImpl.artifactDetailsViewModel injected       │
│                                                            │
│ ArtifactDetailsViewModelImpl._uiState: StateFlow<...>       │
│ Contains: artifacts list, loading state                    │
└────────────────────────────────────────────────────────────┘
                     │
                     ▼
┌────────────────────────────────────────────────────────────┐
│  Layer 3: UseCase & Repository Layer                       │
├────────────────────────────────────────────────────────────┤
│ GetArtifactsListUseCase: invoke(appSlug, buildSlug)        │
│  → Calls: ArtifactsRepository.getArtifacts(...)            │
│  → Returns: Result<List<Artifact>>                         │
│                                                            │
│ ArtifactsRepository (from feature-artifacts)               │
│  → Handles HTTP calls to Bitrise API                       │
└────────────────────────────────────────────────────────────┘
```

## Platform-Specific Implementations

```
         ┌──────────────────────────────────────┐
         │ ArtifactDetailsBottomSheet (expect)   │
         └──────────────────────────────────────┘
                    │
        ┌───────────┼───────────┬─────────────┐
        │           │           │             │
        ▼           ▼           ▼             ▼
    ┌────────┐ ┌────────┐ ┌────────┐ ┌──────────────┐
    │Android │ │ iOS    │ │  JVM   │ │  Expect Def  │
    ├────────┤ ├────────┤ ├────────┤ └──────────────┘
    │Modal   │ │Column  │ │Column  │
    │Bottom  │ │Layout  │ │Layout  │
    │Sheet   │ │(custom)│ │(custom)│
    │(M3)    │ │        │ │        │
    └────────┘ └────────┘ └────────┘


         ┌──────────────────────────────────────┐
         │     UrlOpener (interface)             │
         │  fun openUrl(url: String)             │
         └──────────────────────────────────────┘
                    │
        ┌───────────┼───────────┬─────────────┐
        │           │           │             │
        ▼           ▼           ▼             ▼
    ┌────────────────┐  ┌──────────────┐  ┌──────────────┐
    │ AndroidUrl     │  │ IosUrl       │  │ JvmUrl       │
    │ Opener         │  │ Opener       │  │ Opener       │
    ├────────────────┤  ├──────────────┤  ├──────────────┤
    │ Intent         │  │ UIApplication│  │ Desktop      │
    │ .ACTION_VIEW   │  │ .shared      │  │ .getDesktop()│
    │                │  │ .openURL()   │  │ .browse()    │
    └────────────────┘  └──────────────┘  └──────────────┘
```

## API Response Flow

```
User Action: Click Artifact
        │
        ▼
GetArtifactsListUseCase.invoke(appSlug, buildSlug)
        │
        ▼
ArtifactsRepository.getArtifacts()
        │
        ├─ HTTP GET /apps/{appSlug}/builds/{buildSlug}/artifacts
        │  Returns: ArtifactsResponseDto
        │    {
        │      data: [
        │        { slug, title, artifact_type, is_public_page_enabled },
        │        ...
        │      ]
        │    }
        │
        ├─ For each artifact, fetch details:
        │  HTTP GET /apps/{appSlug}/builds/{buildSlug}/artifacts/{artifactSlug}
        │  Returns: ArtifactDetailResponseDto
        │    {
        │      data: {
        │        slug,
        │        title,
        │        artifact_type,
        │        is_public_page_enabled,
        │        public_install_page_url  ◄── KEY FIELD
        │      }
        │    }
        │
        ▼
Result<List<Artifact>>
        │
        ▼
ArtifactDetailsUiState.Visible(artifacts)
        │
        ▼
BuildsScreen renders BottomSheet
        │
        User clicks artifact
        │
        ▼
urlOpener.openUrl(publicInstallPageUrl)
```

