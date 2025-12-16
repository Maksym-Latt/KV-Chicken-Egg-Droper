package com.chicken.eggdropper.model

import androidx.annotation.DrawableRes
import com.chicken.eggdropper.R

data class ChickenSkin(
    val id: String,
    val name: String,
    val price: Int,
    val description: String,
    @DrawableRes val idleRes: Int,
    @DrawableRes val focusRes: Int
) {
    val isDefault: Boolean get() = price == 0
}

val DefaultSkins = listOf(
    ChickenSkin(
        id = "classic",
        name = "Classic Chicken",
        price = 0,
        description = "Reliable feather hero",
        idleRes = R.drawable.chicken_1_stay,
        focusRes = R.drawable.chicken_1_watch
    ),
    ChickenSkin(
        id = "blue",
        name = "Blue Chicken",
        price = 1500,
        description = "Cool sky hue",
        idleRes = R.drawable.chicken_2_stay,
        focusRes = R.drawable.chicken_2_watch
    ),
    ChickenSkin(
        id = "red",
        name = "Red Chicken",
        price = 1500,
        description = "Fiery spirit",
        idleRes = R.drawable.chicken_3_stay,
        focusRes = R.drawable.chicken_3_watch
    ),
    ChickenSkin(
        id = "shadow",
        name = "Shadow Chicken",
        price = 8500,
        description = "Night runner",
        idleRes = R.drawable.chicken_4_stay,
        focusRes = R.drawable.chicken_4_watch
    )
)
