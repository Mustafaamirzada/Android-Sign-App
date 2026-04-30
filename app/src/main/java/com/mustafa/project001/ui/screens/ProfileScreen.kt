package com.mustafa.project001.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.pager.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.mustafa.project001.R

@Preview
@Composable
fun ProfileScreen(){
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF667EEa),
            Color(0xFF764BA2),
            Color(0xFF9F7AEA)
        ),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f)
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ){
        // Animated Background blobs
        AnimatedBackgroundBlobs()

        // Main Content
        ProfileContent()
    }
}

@Composable
fun AnimatedBackgroundBlobs() {
    val animatedOffset = remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true){
            animatedOffset.value = (animatedOffset.value + 1) % 100
            delay(50)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.1f),
                        Color.Transparent
                    ),
                    radius = 1000f
                )
            )
    ){
        repeat(3) {index ->
            Box(
                modifier = Modifier
                    .size((200 + index * 50).dp)
                    .offset(
                        x = (animatedOffset.value * (index + 1)).dp,
                        y = (animatedOffset.value * (index + 2)).dp,
                    )
                    .background(
                        Color.White.copy(alpha = 0.05f),
                        CircleShape
                    )
                    .blur(20.dp)
            )
        }
    }
}

@SuppressLint("AutoboxingStateCreation")
@Composable
fun ProfileContent() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Photos", "Videos", "Saved")
    val pagerState = rememberPagerState(pageCount = {tabs.size})
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        // Top Bar
        ProfileTopBar()

        // Profile Header
        ProfileHeader()

        // Action Buttons
        ProfileActionButtons()

        Spacer(modifier = Modifier.height(24.dp))


        // Highlights
        ProfileHighLights()

        Spacer(modifier = Modifier.height(16.dp))

        // Scrollable Tab Row
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            contentColor = Color.Transparent,
            edgePadding = 16.dp,
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = {
                        selectedTab = index
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = title,
                            color = if (selectedTab == index) Color.White
                                else Color.White.copy(alpha = 0.6f),
                            fontWeight = if (selectedTab == index) FontWeight.Bold
                                else FontWeight.Normal
                        )
                    }
                )
            }
        }
        // Tab Content
        HorizontalPager(
            beyondViewportPageCount = tabs.size,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) {page ->
            when(page) {
                0 -> PhotosGrid()
                1 -> VideosGrid()
                2 -> SavedGrid()
            }
        }
    }
}

@Composable
fun ProfileTopBar () {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {/* Handle Click */},
            modifier = Modifier
                .size(40.dp)
                .background(
                    Color.White.copy(alpha = 0.2f),
                    CircleShape
                )
        ) {
            Icon(
                Icons.Default.Menu,
                contentDescription = "Menu",
                tint = Color.White
            )
        }

        Text(
            text = "Profile",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        IconButton(
            onClick = {/* Handle settings */},
            modifier = Modifier
                .size(40.dp)
                .background(
                    Color.White.copy(alpha = 0.2f),
                    CircleShape
                )
        ) {
            Icon(
                Icons.Default.Settings,
                contentDescription = "Settings",
                tint = Color.White
            )
        }
    }
}

@Composable
fun ProfileHeader(){
    var isFollowing by remember { mutableStateOf(false)}

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Profile Image with animated border
        Box(
            modifier = Modifier
                .size(12.dp)
                .shadow(20.dp, CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFDC830),
                            Color(0xFFF37335)
                        )
                    ),
                    CircleShape
                )
                .padding(4.dp)
        ) {
//            AsyncImage(
//                model = "https://www.freepik.com/photos/professional-man/4",
//                contentDescription = "Profile Picture",
//                modifier = Modifier
//                    .fillMaxSize()
//                    .clip(CircleShape)
//                    .background(Color.White),
//                contentScale = ContentScale.Crop
//            )
            Image(
                painter = painterResource(R.drawable.garuda),
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Name and Bio
        Text(
            text = "Mustafa",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Text(
            text = "@gmail.com",
            fontSize = 16.sp,
            color = Color.White.copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Digital Creator | Travel Enthusiast | Photography",
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Stats Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            ProfileStat(
                value = "1,234",
                label = "Photos",
                icon = Icons.Outlined.Photo
            )
            ProfileStat(
                value = "1,234",
                label = "Photos",
                icon = Icons.Outlined.Photo
            )
        }
    }
}

@Composable
fun ProfileStat(
    value: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )

        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}


@Composable
fun ProfileActionButtons() {
    var isFollowing by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Follow/Message Button
        Button(
            onClick = { isFollowing = !isFollowing },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFollowing) Color.White.copy(alpha = 0.2f)
                    else Color.White,
                contentColor = if (isFollowing) Color.White
                    else Color(0xFF764BA2)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                if (isFollowing) Icons.Default.Message else Icons.Default.PersonAdd,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isFollowing) "Message" else "Follow",
                fontWeight = FontWeight.Bold
            )
        }

        // Contact Button
        OutlinedButton(
            onClick = { /* Handle contact */ },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.White
            ),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.5f),
                        Color.White.copy(alpha = 0.2f)
                    )
                )
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                Icons.Default.Email,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Contact",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ProfileHighLights() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Story Highlights",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(5) { index ->
                HighlightItem(
                    title = when (index) {
                        0 -> "Travel"
                        1 -> "Food"
                        2 -> "Tech"
                        3 -> "Nature"
                        else -> "Art"
                    },
                    icon = when (index) {
                        0 -> Icons.Default.Flight
                        1 -> Icons.Default.Restaurant
                        2 -> Icons.Default.Computer
                        3 -> Icons.Default.Park
                        else -> Icons.Default.Palette
                    }
                )
            }
        }
    }
}

@Composable
fun HighlightItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.2f),
                            Color.White.copy(alpha = 0.1f)
                        )
                    )
                )
                .border(
                    2.dp,
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFDC830),
                            Color(0xFFF37335)
                        )
                    ),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = title,
            fontSize = 12.sp,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun PhotosGrid() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(10) { index ->
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF667EEA),
                                Color(0xFF764BA2)
                            )
                        )
                    )
            ) {
                // Photo content
                Icon(
                    Icons.Default.Photo,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun VideosGrid() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(8) { index ->
            Box(
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.3f))
            ) {
                // Video thumbnail with play button
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            Color.White.copy(alpha = 0.3f),
                            CircleShape
                        )
                        .align(Alignment.Center),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun SavedGrid() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(9) { index ->
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFFDC830),
                                Color(0xFFF37335)
                            )
                        )
                    )
            ) {
                Icon(
                    Icons.Default.Bookmark,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun PostCard(
    post: PostItem
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Gray.copy(alpha = 0.3f))
    ) {
        // Post image (placeholder)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Image,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.size(32.dp)
            )
        }

        // Post overlay with stats
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        )
                    )
                )
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = formatNumber(post.likes),
                        fontSize = 11.sp,
                        color = Color.White
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Comment,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = formatNumber(post.comments),
                        fontSize = 11.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}


@Composable
fun ProfileBottomNavigation() {
    NavigationBar(
        containerColor = Color.Transparent,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { /* Handle navigation */ },
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Home",
                    tint = Color.White
                )
            },
            alwaysShowLabel = false,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.White.copy(alpha = 0.6f),
                indicatorColor = Color.White.copy(alpha = 0.1f)
            )
        )

        NavigationBarItem(
            selected = false,
            onClick = { /* Handle navigation */ },
            icon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White.copy(alpha = 0.6f)
                )
            },
            alwaysShowLabel = false,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.White.copy(alpha = 0.6f)
            )
        )

        NavigationBarItem(
            selected = false,
            onClick = { /* Handle navigation */ },
            icon = {
                Icon(
                    Icons.Default.AddBox,
                    contentDescription = "Add",
                    tint = Color.White.copy(alpha = 0.6f)
                )
            },
            alwaysShowLabel = false,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.White.copy(alpha = 0.6f)
            )
        )

        NavigationBarItem(
            selected = false,
            onClick = { /* Handle navigation */ },
            icon = {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = Color.White.copy(alpha = 0.6f)
                )
            },
            alwaysShowLabel = false,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.White.copy(alpha = 0.6f)
            )
        )
    }
}


@Composable
fun EditProfileModal(
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("John Anderson") }
    var username by remember { mutableStateOf("@gmail.com") }
    var bio by remember { mutableStateOf("Digital Creator | Travel Enthusiast | Photography") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Edit Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Profile Image Edit
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.Gray.copy(alpha = 0.2f))
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.Center)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = { /* Change photo */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF667EEA)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Change Photo")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Name Field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF667EEA),
                    focusedLabelColor = Color(0xFF667EEA)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Username Field
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF667EEA),
                    focusedLabelColor = Color(0xFF667EEA)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Bio Field
            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Bio") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                minLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF667EEA),
                    focusedLabelColor = Color(0xFF667EEA)
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = {
                        onSave(name, username, bio)
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF667EEA)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save")
                }
            }
        }
    }
}


// Data class for posts
data class PostItem(
    val id: Int,
    val imageUrl: String,
    val likes: Int,
    val comments: Int
)

// Helper function to format numbers (K, M)
fun formatNumber(number: Int): String {
    return when {
        number >= 1000000 -> "${number / 1000000}M"
        number >= 1000 -> "${number / 1000}K"
        else -> number.toString()
    }
}