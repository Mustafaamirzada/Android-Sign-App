package com.mustafa.project001.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun BottomNavigationBar(navController: NavController) {
    // The items of the navigation bar
    val navItems = listOf(NavItem.Home, NavItem.Camera, NavItem.Profile, NavItem.Dictionary)

    // sync between navigation bar and current screen
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // current route
    val currentRoute = navBackStackEntry?.destination?.route

    // create a state variable that persists across recomposition
    var selectedItem = navItems.indexOfFirst { it.path == currentRoute }

    var selectedNavItem by rememberSaveable {
        mutableIntStateOf(if (selectedItem >= 0) selectedItem else 0)
    }

    // Colors
    val selectedGreen = Color(0xFF4CAF50)
    val unselectedGary = Color.Gray
    val vTransparent = Color.Transparent

    // Navigation Bar
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    ) {
        NavigationBar(
            modifier = Modifier
//                .padding(10.dp)
//                .clip(RoundedCornerShape(30.dp))
//                .background(Color.White.copy(alpha = 0.1f))
                .height(72.dp),
            //            .background(
            //                Brush.verticalGradient(
            //                    colors = listOf(
            //                            Color.Black.copy(alpha = 0.1f),// More Transparent
            //                            Color.Black.copy(alpha = 0.15f)), // slightly less transparent at bottom
            //                    startY = 0f,
            //                    endY = Float.POSITIVE_INFINITY
            //                )
            //            ),
            containerColor = Color.Transparent
            //        tonalElevation = 0.dp,
            //        // ⚒️✋ This is going to fix the transparency
            //        windowInsets = WindowInsets(0)
        ) {
            navItems.forEachIndexed { index, item ->
                val isSelected = selectedItem == index
                // Animate colors
                val animatedIconColor by animateColorAsState(
                    targetValue = if (isSelected) selectedGreen else unselectedGary,
                    animationSpec = tween(300)
                )

                NavigationBarItem(
                    modifier = Modifier
                        .background(Color.Transparent),
                    selected = isSelected,
                    onClick = {
                        selectedNavItem = index
                        // handle the navigation
                        val route = item.path
                        navController.navigate(route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                modifier = Modifier.size(24.dp),
                                tint = animatedIconColor
                            )

                        }
                    },
                    label = {
                        Text(
                            text = item.title,
                            fontSize = 10.sp,
                            color = if (item.path == currentRoute) selectedGreen else unselectedGary,
                            style = if (item.path == currentRoute) MaterialTheme.typography.labelLarge
                            else MaterialTheme.typography.labelMedium
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = selectedGreen,
                        selectedTextColor = selectedGreen,
                        unselectedIconColor = unselectedGary,
                        unselectedTextColor = unselectedGary,
                        indicatorColor = vTransparent // remove the default indicator background
                    )
                )
            }
        }
    }
}