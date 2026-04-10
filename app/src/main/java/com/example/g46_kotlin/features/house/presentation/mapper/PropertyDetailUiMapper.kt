package com.example.g46_kotlin.features.house.presentation.mapper

import com.example.g46_kotlin.features.house.domain.model.PropertyDetail
import com.example.g46_kotlin.features.house.presentation.PropertyDetailUi
import javax.inject.Inject

class PropertyDetailUiMapper @Inject constructor() {

    fun toUi(domain: PropertyDetail): PropertyDetailUi = PropertyDetailUi(
        id = domain.id,
        title = domain.title,
        description = domain.description,
        monthlyRent = domain.monthlyRent.toInt(),
        depositAmount = domain.depositAmount?.toInt(),
        neighborhood = domain.neighborhood,
        address = domain.address,
        bedrooms = domain.bedrooms,
        bathrooms = domain.bathrooms,
        sizeM2 = domain.sizeM2,
        furnished = domain.furnished,
        petFriendly = domain.petFriendly,
        hasParking = domain.hasParking,
        hasLaundry = domain.hasLaundry,
        hasWifi = domain.hasWifi,
        includesUtilities = domain.includesUtilities,
        propertyType = domain.propertyType.name.replace("_", " "),
        imageUrl = domain.imageUrls.firstOrNull()
    )
}