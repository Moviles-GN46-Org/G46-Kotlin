package com.example.g46_kotlin.navigation


object AppRoutes {
    const val Splash = "splash"
    const val Login = "login"
    const val Signup = "signup"
    const val Home = "home"
    const val Profile = "profile"
    const val Map = "map"
    const val Settings = "settings"
    const val PropertyDetail = "propertyDetail/{propertyId}"
    const val Chats = "chats"
    const val Feed = "feed"
    const val Roomies = "roomies"

    fun propertyDetail(propertyId: String) = "propertyDetail/$propertyId"
}