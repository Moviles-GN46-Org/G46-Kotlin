package com.example.g46_kotlin.features.house.data.repository

import com.example.g46_kotlin.features.house.domain.model.PropertyDetail
import com.example.g46_kotlin.features.house.domain.model.PropertyStatus
import com.example.g46_kotlin.features.house.domain.model.PropertyType
import com.example.g46_kotlin.features.house.domain.repository.HouseRepository
import kotlinx.coroutines.delay

class FakeHouseRepository : HouseRepository {

    override suspend fun getProperties(): List<PropertyDetail> {
        delay(400)

        return listOf(
            PropertyDetail(
                id = "p1",
                landlordId = "l1",
                title = "The spot",
                description = "Apartamento amplio cerca de la universidad.",
                propertyType = PropertyType.APARTMENT,
                status = PropertyStatus.ACTIVE,
                monthlyRent = 860.0,
                depositAmount = 400.0,
                includesUtilities = true,
                address = "Calle 21 #5 - 31",
                neighborhood = "Santa fe ",
                city = "Bogota",
                latitude = 4.606311305973901,
                longitude = -74.06990798990263,
                sizeM2 = 62.0,
                bedrooms = 2,
                bathrooms = 1,
                furnished = true,
                petFriendly = false,
                hasParking = false,
                hasLaundry = true,
                hasWifi = true,
                imageUrls = emptyList(),
                publishedAt = "2026-03-10T10:00:00Z",
                lastActiveAt = "2026-03-18T09:30:00Z",
                expirationNotice = null,
                createdAt = "2026-03-10T10:00:00Z",
                updatedAt = "2026-03-18T09:30:00Z"
            ),
            PropertyDetail(
                id = "p2",
                landlordId = "l2",
                title = "City U",
                description = "Studio moderno con excelente iluminacion.",
                propertyType = PropertyType.STUDIO,
                status = PropertyStatus.ACTIVE,
                monthlyRent = 780.0,
                depositAmount = null,
                includesUtilities = false,
                address = "Calle 19 #2a - 10",
                neighborhood = "Santa fe",
                city = "Bogota",
                latitude = 4.603255218948534,
                longitude = -74.06720305584139,
                sizeM2 = 35.0,
                bedrooms = 1,
                bathrooms = 1,
                furnished = false,
                petFriendly = true,
                hasParking = false,
                hasLaundry = false,
                hasWifi = true,
                imageUrls = emptyList(),
                publishedAt = "2026-03-11T14:00:00Z",
                lastActiveAt = "2026-03-18T08:10:00Z",
                expirationNotice = null,
                createdAt = "2026-03-11T14:00:00Z",
                updatedAt = "2026-03-18T08:10:00Z"
            ),
            PropertyDetail(
                id = "p3",
                landlordId = "l3",
                title = "Puerti Bahia",
                description = "Casa comoda para compartir con roommates.",
                propertyType = PropertyType.HOUSE,
                status = PropertyStatus.RENTED,
                monthlyRent = 1050.0,
                depositAmount = 600.0,
                includesUtilities = false,
                address = "Calle 23 #68-50",
                neighborhood = "Salitre",
                city = "Bogota",
                latitude = 4.6491874824642885,
                longitude = -74.10820730030592,
                sizeM2 = 95.0,
                bedrooms = 3,
                bathrooms = 2,
                furnished = true,
                petFriendly = true,
                hasParking = true,
                hasLaundry = true,
                hasWifi = true,
                imageUrls = emptyList(),
                publishedAt = "2026-03-05T12:00:00Z",
                lastActiveAt = "2026-03-15T18:25:00Z",
                expirationNotice = null,
                createdAt = "2026-03-05T12:00:00Z",
                updatedAt = "2026-03-15T18:25:00Z"
            )
        )
    }

    override suspend fun getPropertyById(id: String): PropertyDetail {
        return PropertyDetail(
            id = "p3",
            landlordId = "l3",
            title = "Puerto Bahia",
            description = "Casa comoda para compartir con roommates.",
            propertyType = PropertyType.HOUSE,
            status = PropertyStatus.RENTED,
            monthlyRent = 1050.0,
            depositAmount = 600.0,
            includesUtilities = false,
            address = "Calle 23 #68-50",
            neighborhood = "Salitre",
            city = "Bogota",
            latitude = 4.6491874824642885,
            longitude = -74.10820730030592,
            sizeM2 = 95.0,
            bedrooms = 3,
            bathrooms = 2,
            furnished = true,
            petFriendly = true,
            hasParking = true,
            hasLaundry = true,
            hasWifi = true,
            imageUrls = emptyList(),
            publishedAt = "2026-03-05T12:00:00Z",
            lastActiveAt = "2026-03-15T18:25:00Z",
            expirationNotice = null,
            createdAt = "2026-03-05T12:00:00Z",
            updatedAt = "2026-03-15T18:25:00Z"
        )
    }
}