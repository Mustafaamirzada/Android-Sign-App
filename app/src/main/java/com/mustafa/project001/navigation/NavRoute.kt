package com.mustafa.project001.navigation

sealed class NavRoute(val path: String) {
    object Home: NavRoute("home")
    object Camera: NavRoute("camera")
    object Profile: NavRoute("profile")

}