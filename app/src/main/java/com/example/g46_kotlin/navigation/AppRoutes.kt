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
    const val Notifications = "notifications"
    const val Favorites = "favorites"
    fun propertyDetail(propertyId: String) = "propertyDetail/$propertyId"
    const val ChatDetail = "chatDetail/{chatId}"
    fun chatDetail(chatId: String) = "chatDetail/$chatId"

    const val NotImplemented = "notImplemented"
}