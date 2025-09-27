package com.example.lab_week_05_00000097514.model

import com.squareup.moshi.Json

data class CatBreedData(
    val id: String,
    val name: String,
    @Json(name = "origin") val origin: String? = null
)
