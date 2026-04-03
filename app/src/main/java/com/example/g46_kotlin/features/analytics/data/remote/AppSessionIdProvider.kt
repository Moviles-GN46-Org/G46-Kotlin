package com.example.g46_kotlin.features.analytics.data.remote

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSessionIdProvider @Inject constructor() {
    val sessionId: String = UUID.randomUUID().toString()
}