package com.example.g46_kotlin.features.house.data.repository

import com.example.g46_kotlin.features.house.domain.model.House
import com.example.g46_kotlin.features.house.domain.repository.HouseRepository
import kotlinx.coroutines.delay

class FakeHouseRepository : HouseRepository {

    override suspend fun getHomes(): List<House> {
        delay(400)

        return listOf(
            House(
                id = "h1",
                title = "Lakeside Suite",
                pricePerMonth = 860,
                rating = 4.5,
                distanceToCampus = "1.0 miles",
                propertyType = "2 Bed · 1 Bath",
                isVerified = true,
                isLiked = false,
                imageUrl = null
            ),
            House(
                id = "h2",
                title = "Riverstone Flat",
                pricePerMonth = 780,
                rating = 4.4,
                distanceToCampus = "0.6 miles",
                propertyType = "Studio · 1 Bath · Kitchenette",
                isVerified = true,
                isLiked = false,
                imageUrl = null
            ),
            House(
                id = "h3",
                title = "Cedar Heights",
                pricePerMonth = 1050,
                rating = 4.9,
                distanceToCampus = "1.4 miles",
                propertyType = "3 Bed · 2 Bath",
                isVerified = true,
                isLiked = true,
                imageUrl = null
            )
        )
    }
}