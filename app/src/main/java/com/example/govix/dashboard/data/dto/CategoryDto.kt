package com.example.govix.dashboard.data.dto

data class CategoryDto(
    @SerializedName("id")            val id: Int,
    @SerializedName("name")          val name: String,
    @SerializedName("slug")          val slug: String,
    @SerializedName("icon")          val icon: String,
    @SerializedName("description")   val description: String,
    @SerializedName("color_code")    val colorCode: String,
    @SerializedName("is_active")     val isActive: Int,
    @SerializedName("display_order") val displayOrder: Int
)

data class CategoryResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data")    val data: List<CategoryDto>
)