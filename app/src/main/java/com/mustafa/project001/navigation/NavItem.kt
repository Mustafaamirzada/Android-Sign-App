package com.mustafa.project001.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItem {
    object Home:
            Item(NavRoute.Home.path.toString(),
                "Home", Icons.Default.Home
            )
    object Camera:
            Item(NavRoute.Camera.path.toString(),
                "Camera", Icons.Default.CameraAlt)
    object Profile:
            Item(NavRoute.Profile.path.toString(),
                "Profile", Icons.Default.Person)

    object Dictionary:
            Item(NavRoute.Dictionary.path.toString(),
                "Dictionary", Icons.Default.Book
            )
}

open class Item(
    val path: String,
    val title: String,
    val icon: ImageVector

)