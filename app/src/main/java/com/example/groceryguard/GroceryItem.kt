package com.example.groceryguard

data class GroceryItem(
    val name: String = "",
    val price: Double = 0.0,
    val category: String = "",
    val isPurchased: Boolean = false
)