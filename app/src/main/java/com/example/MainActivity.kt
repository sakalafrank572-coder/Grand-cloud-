package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.ArtistProfile
import com.example.ui.MusicViewModel
import com.example.ui.components.GlassCard
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ObsidianBackground
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.GoldenAccent
import com.example.ui.theme.LuxuryDarkCardBorder
import com.example.ui.theme.TextSecondary

// Enum representation for screen sections
enum class Route {
    Splash,
    Auth,
    Dashboard
}

enum class AuraTab(val label: String, val icon: ImageVector) {
    Home("Feed", Icons.Default.MusicNote),
    Explore("Explore", Icons.Default.Search),
    Upload("Transmit", Icons.Default.AddCircle),
    Inbox("Collab", Icons.Default.Chat),
    Alerts("Radar", Icons.Default.Notifications)
}

class MainActivity : ComponentActivity() {

    private val viewModel: MusicViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Mandatory Edge-To-Edge alignment
        
        setContent {
            MyApplicationTheme {
                var currentRoute by remember { mutableStateOf(Route.Splash) }
                var currentTab by remember { mutableStateOf(AuraTab.Home) }
                
                // Track deep profile navigation stack
                var activeArtistProfileId by remember { mutableStateOf<String?>(null) }
                
                // Keep tracks of last visited tabs to support profile BACK flows cleanly
                val profileBackHistory = remember { mutableStateListOf<AuraTab>() }

                val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

                // Boot route flow routing
                LaunchedEffect(currentUser) {
                    if (currentRoute == Route.Splash) {
                        // Let splash run its timed delay before checking login state
                    } else {
                        if (currentUser == null) {
                            currentRoute = Route.Auth
                        } else {
                            currentRoute = Route.Dashboard
                        }
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = ObsidianBackground
                ) {
                    when (currentRoute) {
                        Route.Splash -> {
                            SplashScreen(onSplashFinished = {
                                currentRoute = if (currentUser == null) Route.Auth else Route.Dashboard
                            })
                        }
                        Route.Auth -> {
                            AuthScreen(
                                viewModel = viewModel,
                                onAuthSuccess = {
                                    currentRoute = Route.Dashboard
                                }
                            )
                        }
                        Route.Dashboard -> {
                            Scaffold(
                                modifier = Modifier.fillMaxSize(),
                                contentWindowInsets = WindowInsets.safeDrawing, // safe draws avoid camera notches
                                bottomBar = {
                                    // Custom Floating Navigation Bar overlay
                                    FloatingBottomNavBar(
                                        activeTab = currentTab,
                                        onTabSelected = { tab ->
                                            activeArtistProfileId = null // clear profiles when clicking tabs
                                            currentTab = tab
                                        }
                                    )
                                }
                            ) { innerPadding ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding)
                                ) {
                                    // Master navigation controller
                                    AnimatedContent(
                                        targetState = activeArtistProfileId,
                                        transitionSpec = {
                                            if (targetState != null) {
                                                // Slide in profile details
                                                slideInHorizontally { width -> width / 3 } + fadeIn() togetherWith
                                                slideOutHorizontally { width -> -width / 3 } + fadeOut()
                                            } else {
                                                // Slide out profile details
                                                slideInHorizontally { width -> -width / 3 } + fadeIn() togetherWith
                                                slideOutHorizontally { width -> width / 3 } + fadeOut()
                                            }
                                        },
                                        modifier = Modifier.fillMaxSize(),
                                        label = "MasterNavigation"
                                    ) { profileId ->
                                        if (profileId != null) {
                                            // RENDER ARTIST DETAIL VIEW PAGE
                                            ProfileScreen(
                                                artistId = profileId,
                                                viewModel = viewModel,
                                                onBack = { activeArtistProfileId = null },
                                                onNavigateToChat = { artist ->
                                                    activeArtistProfileId = null
                                                    viewModel.openChatWith(artist)
                                                    currentTab = AuraTab.Inbox
                                                }
                                            )
                                        } else {
                                            // RENDER ACTIVE SECTIONS
                                            when (currentTab) {
                                                AuraTab.Home -> {
                                                    HomeScreen(
                                                        viewModel = viewModel,
                                                        onNavigateToChat = { artist ->
                                                            viewModel.openChatWith(artist)
                                                            currentTab = AuraTab.Inbox
                                                        },
                                                        onNavigateToProfile = { id ->
                                                            activeArtistProfileId = id
                                                        }
                                                    )
                                                }
                                                AuraTab.Explore -> {
                                                    ExploreScreen(
                                                        viewModel = viewModel,
                                                        onNavigateToProfile = { id ->
                                                            activeArtistProfileId = id
                                                        }
                                                    )
                                                }
                                                AuraTab.Upload -> {
                                                    UploadScreen(
                                                        viewModel = viewModel,
                                                        onUploadSuccess = {
                                                            currentTab = AuraTab.Home
                                                        }
                                                    )
                                                }
                                                AuraTab.Inbox -> {
                                                    MessagesScreen(
                                                        viewModel = viewModel,
                                                        onNavigateToProfile = { id ->
                                                            activeArtistProfileId = id
                                                        }
                                                    )
                                                }
                                                AuraTab.Alerts -> {
                                                    // Combined Notifications + Favorite records Screen
                                                    Column(modifier = Modifier.fillMaxSize()) {
                                                        var subTab by remember { mutableStateOf(0) } // 0 = notifications, 1 = favorites

                                                        Row(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(horizontal = 24.dp, vertical = 8.dp)
                                                                .clip(RoundedCornerShape(8.dp))
                                                                .background(Color(0xFF101014)),
                                                            horizontalArrangement = Arrangement.SpaceEvenly
                                                        ) {
                                                            listOf("RADAR NOTIFICATIONS", "FAVORITE RECORDS").forEachIndexed { index, title ->
                                                                val isSelected = subTab == index
                                                                Box(
                                                                    modifier = Modifier
                                                                        .weight(1f)
                                                                        .clickable { subTab = index }
                                                                        .background(
                                                                            brush = if (isSelected) {
                                                                                Brush.horizontalGradient(listOf(CyberPurple, CyberCyan))
                                                                            } else {
                                                                                Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                                                                            }
                                                                        )
                                                                        .padding(vertical = 10.dp),
                                                                    contentAlignment = Alignment.Center
                                                                ) {
                                                                    Text(
                                                                        title,
                                                                        color = if (isSelected) Color.White else TextSecondary,
                                                                        fontWeight = FontWeight.Black,
                                                                        fontStyle = FontStyle.Italic,
                                                                        fontSize = 10.sp
                                                                    )
                                                                }
                                                            }
                                                        }

                                                        Spacer(modifier = Modifier.height(4.dp))

                                                        if (subTab == 0) {
                                                            NotificationsScreen(viewModel = viewModel)
                                                        } else {
                                                            LibFavoritesScreen(viewModel = viewModel, onNavigateToProfile = { id -> activeArtistProfileId = id })
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    // floating glass bottom player panels (always hover above bottom navigations)
                                    MusicPlayerPanel(
                                        viewModel = viewModel,
                                        onNavigateToProfile = { id ->
                                            activeArtistProfileId = id
                                        },
                                        modifier = Modifier.align(Alignment.BottomCenter)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// FLOATING GLASS BOTTOM NAVIGATION COMPOSABLE
// ==========================================
@Composable
fun FloatingBottomNavBar(
    activeTab: AuraTab,
    onTabSelected: (AuraTab) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars) // Strict Safe Area system gesture pill offset padding!
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Floating premium glassy bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color(0xD90B0B0E)) // Semi-transparent Glass card base
                .border(1.dp, LuxuryDarkCardBorder, RoundedCornerShape(32.dp))
                .padding(horizontal = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AuraTab.values().forEach { tab ->
                    val isActive = tab == activeTab
                    val activeColor = when (tab) {
                        AuraTab.Home -> CyberCyan
                        AuraTab.Explore -> GoldenAccent
                        AuraTab.Upload -> Color(0xFFFF007F) // GlowPink
                        AuraTab.Inbox -> CyberPurple
                        AuraTab.Alerts -> CyberCyan
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .clip(CircleShape)
                            .clickable { onTabSelected(tab) }
                            .padding(vertical = 4.dp)
                            .testTag("nav_tab_${tab.name}")
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.label,
                            tint = if (isActive) activeColor else TextSecondary,
                            modifier = Modifier.size(if (isActive) 24.dp else 20.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = tab.label,
                            color = if (isActive) Color.White else TextSecondary,
                            fontSize = 9.sp,
                            fontWeight = if (isActive) FontWeight.Black else FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
