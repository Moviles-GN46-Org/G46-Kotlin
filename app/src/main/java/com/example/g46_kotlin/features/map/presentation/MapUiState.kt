package com.example.g46_kotlin.features.map.presentation

data class PropertyPinUi (
    val id: String,
    val title: String,
    val description: String,
    val rating: Double,
    val lat: Double,
    val lon: Double,
    val price: String,
    val imageUrl: String
)

data class UserLocationUI(
    val lat: Double,
    val lon: Double,
)

data class MapUiState(
    val isLoading: Boolean = false,
    val userLocation: UserLocationUI? = null,
    val apartments: List<PropertyPinUi> = emptyList(),
    val errorMessage: String? = null,
    val selectedApartmentId: String? = null,
)