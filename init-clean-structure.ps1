# ==========================================
# Clean Architecture Structure Generator
# Student Housing Android App
# ==========================================

Write-Host "Creating Clean Architecture structure..." -ForegroundColor Cyan

# ---------- FOLDERS ----------
$folders = @(
    "core",
    "core/designsystem",
    "core/ui",
    "core/navigation",
    "core/util",
    "core/network",

    "domain",
    "domain/model",
    "domain/repository",
    "domain/usecase",

    "data",
    "data/remote",
    "data/local",
    "data/repository",
    "data/mapper",

    "features",
    "features/auth",
    "features/listings",
    "features/map",
    "features/chat",
    "features/profile"
)

foreach ($folder in $folders) {
    if (-Not (Test-Path $folder)) {
        New-Item -ItemType Directory -Path $folder | Out-Null
        Write-Host "Created folder: $folder" -ForegroundColor Green
    } else {
        Write-Host "Folder exists: $folder" -ForegroundColor Yellow
    }
}

# ---------- BASE FILES ----------
$files = @(
    # Core
    "core/designsystem/Colors.kt",
    "core/designsystem/Typography.kt",
    "core/designsystem/Shapes.kt",
    "core/designsystem/Theme.kt",
    "core/navigation/NavGraph.kt",
    "core/ui/Components.kt",
    "core/network/NetworkModule.kt",

    # Domain
    "domain/model/User.kt",
    "domain/model/Listing.kt",
    "domain/model/Message.kt",
    "domain/repository/UserRepository.kt",
    "domain/repository/ListingRepository.kt",
    "domain/repository/ChatRepository.kt",
    "domain/usecase/LoginUseCase.kt",
    "domain/usecase/RegisterUserUseCase.kt",
    "domain/usecase/GetListingsUseCase.kt",
    "domain/usecase/GetNearbyListingsUseCase.kt",
    "domain/usecase/SendMessageUseCase.kt",

    # Data
    "data/remote/UserApi.kt",
    "data/remote/ListingApi.kt",
    "data/remote/ChatApi.kt",
    "data/repository/UserRepositoryImpl.kt",
    "data/repository/ListingRepositoryImpl.kt",
    "data/repository/ChatRepositoryImpl.kt",
    "data/mapper/UserMapper.kt",
    "data/mapper/ListingMapper.kt",
    "data/mapper/MessageMapper.kt",

    # Features - Auth
    "features/auth/AuthScreen.kt",
    "features/auth/AuthViewModel.kt",

    # Features - Listings
    "features/listings/ListingsScreen.kt",
    "features/listings/ListingsViewModel.kt",

    # Features - Map
    "features/map/MapScreen.kt",
    "features/map/MapViewModel.kt",

    # Features - Chat
    "features/chat/ChatScreen.kt",
    "features/chat/ChatViewModel.kt",

    # Features - Profile
    "features/profile/ProfileScreen.kt",
    "features/profile/ProfileViewModel.kt"
)

foreach ($file in $files) {
    if (-Not (Test-Path $file)) {
        New-Item -ItemType File -Path $file | Out-Null
        Write-Host "Created file: $file" -ForegroundColor Green
    } else {
        Write-Host "File exists: $file" -ForegroundColor Yellow
    }
}

Write-Host "Clean Architecture structure created successfully!" -ForegroundColor Cyan