package com.example.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.zIndex
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.*
import com.example.ui.MusicViewModel
import com.example.ui.components.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ==========================================
// 1. SPLASH SCREEN
// ==========================================
@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2200)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBackground)
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        RadialCosmicGlow(color = CyberPurple, modifier = Modifier.fillMaxSize())

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Shimmering Logo ring
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .border(2.dp, Brush.radialGradient(listOf(GoldenAccent, CyberCyan)), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = "ZM BEATS Logo",
                    tint = GoldenAccent,
                    modifier = Modifier.size(50.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            GlowingText(
                text = "ZM BEATS",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontStyle = FontStyle.Italic,
                    letterSpacing = 8.sp,
                    color = Color.White
                ),
                glowColor = GoldenAccent
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "UPCOMING & INDEPENDENT RADAR",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.0.sp
            )
        }
    }
}

// ==========================================
// 2. AUTHENTICATION & REGISTRATION SCREEN
// ==========================================
@Composable
fun AuthScreen(viewModel: MusicViewModel, onAuthSuccess: () -> Unit) {
    var isSignUp by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val authError by viewModel.authError.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBackground)
            .testTag("auth_screen")
    ) {
        RadialCosmicGlow(color = CyberPurple, modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            GlowingText(
                text = "ZM BEATS CENTRAL",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Black,
                    fontStyle = FontStyle.Italic,
                    letterSpacing = (-1).sp,
                    color = Color.White
                ),
                glowColor = GoldenAccent
            )

            Text(
                text = "Enter the launch pad for independent creators",
                color = TextSecondary,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("auth_card"),
                borderColor = CyberPurple.copy(alpha = 0.3f)
            ) {
                Text(
                    text = if (isSignUp) "CREATE CREATOR KEY" else "VALIDATE ARTIST KEY",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (authError != null) {
                    Text(
                        text = authError ?: "",
                        color = GlowPink,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                TextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Artist Alias / Username") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0x1AFFFDF7),
                        unfocusedContainerColor = Color(0x0AFFFFFF),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = GoldenAccent,
                        unfocusedLabelColor = TextSecondary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("username_input")
                )

                if (isSignUp) {
                    Spacer(modifier = Modifier.height(12.dp))
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Contact Email Address") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0x1AFFFDF7),
                            unfocusedContainerColor = Color(0x0AFFFFFF),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = GoldenAccent,
                            unfocusedLabelColor = TextSecondary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Access Key / Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0x1AFFFDF7),
                        unfocusedContainerColor = Color(0x0AFFFFFF),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = GoldenAccent,
                        unfocusedLabelColor = TextSecondary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("password_input")
                )

                Spacer(modifier = Modifier.height(24.dp))

                GlowingButton(
                    text = if (isSignUp) "INITIALIZE CREATOR ID" else "UNLOCK ARTIST PORTAL",
                    onClick = {
                        if (isSignUp) {
                            viewModel.register(username, email, password, onAuthSuccess)
                        } else {
                            viewModel.login(username, password, onAuthSuccess)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("submit_button")
                )

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = { isSignUp = !isSignUp },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = if (isSignUp) "Already have a station? Sign In" else "New independent? Click here to register",
                        color = CyberCyan,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

// ==========================================
// 3. HOME SCREEN & MUSIC STORIES FEED
// ==========================================
@Composable
fun HomeScreen(
    viewModel: MusicViewModel,
    onNavigateToChat: (ArtistProfile) -> Unit,
    onNavigateToProfile: (String) -> Unit
) {
    val songs by viewModel.allSongs.collectAsStateWithLifecycle()
    val artists by viewModel.allArtists.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    var activeCommentSong by remember { mutableStateOf<Song?>(null) }
    var activeViewerStatus by remember { mutableStateOf<Status?>(null) }
    var showStatusCreator by remember { mutableStateOf(false) }
    var activeReelIndex by remember { mutableStateOf<Int?>(null) }
    val reels by viewModel.allReels.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 16.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("RADAR FREQUENCY", color = GoldenAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("ZM BEATS FEED", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black, fontStyle = FontStyle.Italic, letterSpacing = (-0.5).sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(imageVector = Icons.Default.OfflineBolt, contentDescription = "Active network", tint = CyberCyan, modifier = Modifier.size(18.dp))
                        }
                    }

                    currentUser?.let {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFF16161C))
                                .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
                                .clickable { onNavigateToProfile(it.id) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            AvatarWithGradient(
                                displayName = it.displayName,
                                startColor = it.avatarGradientStart,
                                endColor = it.avatarGradientEnd,
                                modifier = Modifier.size(26.dp),
                                avatarUri = it.avatarUri
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(it.displayName, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
            }

            // Status Updates Row Category
            item {
                val statuses by viewModel.allStatuses.collectAsStateWithLifecycle()
                
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(
                        text = "ARTIST STATUS UPDATES",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 10.dp)
                    )

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // "My Status" add or view trigger
                        item {
                            val myStatus = statuses.find { it.artistId == currentUser?.id }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clickable {
                                        if (myStatus != null) {
                                            activeViewerStatus = myStatus
                                        } else {
                                            showStatusCreator = true
                                        }
                                    }
                                    .padding(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(68.dp)
                                        .border(
                                            width = 2.dp,
                                            brush = if (myStatus != null) {
                                                Brush.sweepGradient(listOf(GlowPink, CyberPurple, CyberCyan))
                                            } else {
                                                Brush.linearGradient(listOf(Color.White.copy(0.15f), Color.White.copy(0.15f)))
                                            },
                                            shape = CircleShape
                                        )
                                        .padding(3.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape)
                                            .background(
                                                if (myStatus != null) {
                                                    try { Color(android.graphics.Color.parseColor(myStatus.bgGradientStart)) }
                                                    catch(e: Exception) { CyberPurple }
                                                } else {
                                                    Color(0xFF16161C)
                                                }
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (myStatus != null) {
                                            Text(
                                                text = myStatus.text.take(2).uppercase(),
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                        } else {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = "Add Status",
                                                tint = CyberCyan,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "My Status",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Other artist statuses
                        items(statuses) { status ->
                            if (status.artistId != currentUser?.id) {
                                val sHex = try { Color(android.graphics.Color.parseColor(status.bgGradientStart)) } catch(e: Exception) { CyberPurple }
                                val eHex = try { Color(android.graphics.Color.parseColor(status.bgGradientEnd)) } catch(e: Exception) { CyberCyan }
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .clickable { activeViewerStatus = status }
                                        .padding(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(68.dp)
                                            .border(
                                                width = 2.dp,
                                                brush = Brush.sweepGradient(listOf(CyberCyan, CyberPurple, GlowPink)),
                                                shape = CircleShape
                                            )
                                            .padding(3.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(CircleShape)
                                                .background(Brush.linearGradient(listOf(sHex, eHex))),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = status.artistName.take(2).uppercase(),
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = status.artistName,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.width(70.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Artist Stories (Rings for quick sample streaming)
            item {
                Column(modifier = Modifier.padding(vertical = 12.dp)) {
                    Text(
                        text = "EXPLORE ARTIST REELS",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 10.dp)
                    )

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(artists) { artist ->
                            if (artist.id != currentUser?.id) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .clickable {
                                            val artistReelIndex = reels.indexOfFirst { it.artistId == artist.id }
                                            if (artistReelIndex != -1) {
                                                activeReelIndex = artistReelIndex
                                            } else {
                                                // Trigger starting songs from this artist
                                                val artistSongs = songs.filter { it.artistId == artist.id }
                                                if (artistSongs.isNotEmpty()) {
                                                    viewModel.playSong(artistSongs.first(), artistSongs)
                                                }
                                            }
                                        }
                                        .padding(4.dp)
                                        .testTag("story_ring_${artist.id}")
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(68.dp)
                                            .border(
                                                width = 2.dp,
                                                brush = Brush.sweepGradient(
                                                    listOf(CyberPurple, CyberCyan, GlowPink, CyberPurple)
                                                ),
                                                shape = CircleShape
                                            )
                                            .padding(3.dp)
                                    ) {
                                        AvatarWithGradient(
                                            displayName = artist.displayName,
                                            startColor = artist.avatarGradientStart,
                                            endColor = artist.avatarGradientEnd,
                                            modifier = Modifier.fillMaxSize(),
                                            isVerified = artist.isVerified,
                                            avatarUri = artist.avatarUri
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = artist.displayName,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.width(70.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Feed Header
            item {
                Text(
                    text = "LATEST SUBMISSIONS",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 8.dp)
                )
            }

            // Song list feed
            if (songs.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(imageVector = Icons.Default.MusicVideo, contentDescription = "none", tint = TextSecondaryDark, modifier = Modifier.size(60.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No transmissions live yet. Go upload a hit!", color = TextSecondary, fontSize = 14.sp)
                    }
                }
            } else {
                items(songs) { song ->
                    SongFeedCard(
                        song = song,
                        viewModel = viewModel,
                        onArtistClick = { onNavigateToProfile(song.artistId) },
                        onCommentClick = { activeCommentSong = song }
                    )
                }
            }
        }

        // Expanded Comments Drawer popup
        activeCommentSong?.let { song ->
            CommentDrawerDialog(
                song = song,
                viewModel = viewModel,
                onDismiss = { activeCommentSong = null }
            )
        }

        // --- Dialogs overlay ---
        activeViewerStatus?.let { status ->
            StatusViewerDialog(status = status, onDismiss = { activeViewerStatus = null })
        }

        if (showStatusCreator) {
            StatusCreatorDialog(viewModel = viewModel, onDismiss = { showStatusCreator = false })
        }

        activeReelIndex?.let { index ->
            ReelsPlayerDialog(reels = reels, initialIndex = index, viewModel = viewModel, onDismiss = { activeReelIndex = null })
        }
    }
}

@Composable
fun SongFeedCard(
    song: Song,
    viewModel: MusicViewModel,
    onArtistClick: () -> Unit,
    onCommentClick: () -> Unit
) {
    val currentPlaying by viewModel.currentPlayingSong.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
    
    val isLiked by viewModel.isSongLiked(song.id).collectAsStateWithLifecycle(initialValue = false)
    val isReposted by viewModel.isSongReposted(song.id).collectAsStateWithLifecycle(initialValue = false)

    val isActive = currentPlaying?.id == song.id

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { viewModel.playSong(song) }
            .testTag("song_feed_card_${song.id}"),
        borderColor = if (isActive) CyberCyan.copy(alpha = 0.4f) else LuxuryDarkCardBorder
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Album art simulated gradient
            val sColor = remember { try { Color(android.graphics.Color.parseColor(song.coverColorStart)) } catch(e: Exception) { CyberPurple } }
            val eColor = remember { try { Color(android.graphics.Color.parseColor(song.coverColorEnd)) } catch(e: Exception) { CyberCyan } }

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.linearGradient(listOf(sColor, eColor)))
                    .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (isActive && isPlaying) {
                    MusicVisualizer(isPlaying = true, modifier = Modifier.fillMaxSize(0.7f), glowColor = Color.White, barCount = 6)
                } else {
                    Icon(imageVector = Icons.Default.MusicNote, contentDescription = "Music", tint = Color.Black.copy(alpha = 0.6f))
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = song.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onArtistClick() }
                ) {
                    Text(
                        text = song.artistName,
                        color = textThemeGradientColor(song.genre),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("•", color = TextSecondaryDark, fontSize = 11.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = song.genre,
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Quick Play/Pause indicator
            IconButton(
                onClick = {
                    if (isActive) viewModel.togglePlayPause() else viewModel.playSong(song)
                },
                modifier = Modifier.testTag("play_icon_button_${song.id}")
            ) {
                Icon(
                    imageVector = if (isActive && isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "play-pause",
                    tint = if (isActive) CyberCyan else Color.White
                )
            }
        }

        if (song.description.isNotEmpty()) {
            Text(
                text = song.description,
                color = TextSecondary,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 10.dp, bottom = 4.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Interaction Statistics metrics
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Plays count
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Headset, contentDescription = "Plays", tint = TextSecondary, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(formatStatCount(song.plays + if (isActive && isPlaying) 1 else 0), color = TextSecondary, fontSize = 11.sp)
            }

            // Likes count toggle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { viewModel.toggleLikeSong(song.id) }
                    .padding(4.dp)
                    .testTag("like_row_${song.id}")
            ) {
                Icon(
                    imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Like icon",
                    tint = if (isLiked) GlowPink else TextSecondary,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = formatStatCount(song.likes),
                    color = if (isLiked) GlowPink else TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Repost action
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { viewModel.toggleRepostSong(song.id) }
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Autorenew,
                    contentDescription = "Repost",
                    tint = if (isReposted) CyberCyan else TextSecondary,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = formatStatCount(song.reposts),
                    color = if (isReposted) CyberCyan else TextSecondary,
                    fontSize = 11.sp
                )
            }

            // Comment trigger
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onCommentClick() }
                    .padding(4.dp)
                    .testTag("comment_button_${song.id}")
            ) {
                Icon(imageVector = Icons.Default.Comment, contentDescription = "Comment", tint = TextSecondary, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Chat", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// Comments sliding sheet helper
@Composable
fun CommentDrawerDialog(
    song: Song,
    viewModel: MusicViewModel,
    onDismiss: () -> Unit
) {
    val comments by viewModel.getSongComments(song.id).collectAsStateWithLifecycle(initialValue = emptyList())
    var textInput by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Dialog(onDismissRequest = onDismiss) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(480.dp)
                .testTag("comment_drawer_${song.id}"),
            borderColor = CyberPurple
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Comments (${song.title})",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp
                )

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Dismiss", tint = Color.White)
                }
            }

            Divider(color = LuxuryDarkCardBorder, modifier = Modifier.padding(vertical = 8.dp))

            // Scrollable comments list
            Box(modifier = Modifier.weight(1f)) {
                if (comments.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("No discussions yet.", color = TextSecondary, fontSize = 13.sp)
                        Text("Be the first to post a reaction!", color = GoldenAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(comments) { comment ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.linearGradient(
                                                listOf(CyberPurple, CyberCyan)
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(comment.userName.take(1).uppercase(), color = Color.Black, fontWeight = FontWeight.Black, fontSize = 11.sp)
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(comment.userName, color = GoldenAccent, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(formatTimeAgo(comment.timestamp), color = TextSecondaryDark, fontSize = 10.sp)
                                    }
                                    Text(comment.text, color = Color.White, fontSize = 13.sp, modifier = Modifier.padding(top = 2.dp))
                                }
                            }
                        }
                    }
                }
            }

            Divider(color = LuxuryDarkCardBorder, modifier = Modifier.padding(vertical = 8.dp))

            // Comment text input Box
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = { Text("Add comment from underground station...", fontSize = 12.sp) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF14141A),
                        unfocusedContainerColor = Color(0xFF0F0F12),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("comment_field_input"),
                    shape = RoundedCornerShape(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (textInput.trim().isNotEmpty()) {
                            viewModel.postComment(song.id, textInput.trim())
                            textInput = ""
                        }
                    },
                    modifier = Modifier
                        .background(CyberPurple, CircleShape)
                        .testTag("comment_submit")
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send comment", tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

// ==========================================
// 4. EXPLORE / DISCOVER & BEAT MARKETPLACE
// ==========================================
@Composable
fun ExploreScreen(
    viewModel: MusicViewModel,
    onNavigateToProfile: (String) -> Unit
) {
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
    val beats by viewModel.allBeats.collectAsStateWithLifecycle()
    val query by viewModel.searchQuery.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    var activeTab by remember { mutableStateOf(0) } // 0 = Music Search, 1 = Beat Marketplace
    val focusManager = LocalFocusManager.current

    // Beats licensing local states
    var buyingBeat by remember { mutableStateOf<BeatListing?>(null) }
    var transactionReceipt by remember { mutableStateOf<String?>(null) }

    // Upload beat builder states
    var showBeatUploadDialog by remember { mutableStateOf(false) }
    var beatTitle by remember { mutableStateOf("") }
    var beatBpm by remember { mutableStateOf("140") }
    var beatPrice by remember { mutableStateOf("$29.99") }
    var beatTags by remember { mutableStateOf("Trap,Phonk") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("explore_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Central search field and category tabs
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 16.dp, bottom = 8.dp)
            ) {
                Text("DISCOVER TALENTS", color = GoldenAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                Text("SEARCH STATION", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black, fontStyle = FontStyle.Italic, letterSpacing = (-0.5).sp)

                Spacer(modifier = Modifier.height(12.dp))

                TextField(
                    value = query,
                    onValueChange = { viewModel.search(it) },
                    placeholder = { Text("Find songs, producers, genres (#phonk)...") },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = TextSecondary) },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = { viewModel.search("") }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", tint = Color.White)
                            }
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF13131A),
                        unfocusedContainerColor = Color(0xFF0C0C0E),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_bar_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Dual tabs: Music vs Beats
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF101014)),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("DISCOVER SONGS", "BEAT LICENSE MARKET").forEachIndexed { index, text ->
                        val isSelected = activeTab == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { activeTab = index }
                                .background(
                                    brush = if (isSelected) {
                                        Brush.horizontalGradient(listOf(CyberPurple, CyberCyan))
                                    } else {
                                        Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                                    }
                                )
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = text,
                                color = if (isSelected) Color.White else TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                fontStyle = FontStyle.Italic,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }

            // Tab rendering
            if (activeTab == 0) {
                // Music listing view
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { focusManager.clearFocus() },
                    contentPadding = PaddingValues(bottom = 120.dp, top = 8.dp)
                ) {
                    if (searchResults.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 60.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("No tracks match your transmission.", color = TextSecondary, fontSize = 14.sp)
                                Text("Try 'phonk' or 'dreampop'", color = GoldenAccent, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                            }
                        }
                    } else {
                        items(searchResults) { song ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.playSong(song, searchResults) }
                                    .padding(horizontal = 24.dp, vertical = 10.dp)
                                    .testTag("explore_song_row_${song.id}"),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val sColor = remember { try { Color(android.graphics.Color.parseColor(song.coverColorStart)) } catch(e: Exception) { CyberPurple } }
                                val eColor = remember { try { Color(android.graphics.Color.parseColor(song.coverColorEnd)) } catch(e: Exception) { CyberCyan } }

                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Brush.linearGradient(listOf(sColor, eColor))),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(imageVector = Icons.Default.MusicNote, contentDescription = "", tint = Color.Black.copy(0.4f))
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(song.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(song.artistName, color = TextSecondary, fontSize = 12.sp)
                                }

                                Text(
                                    text = song.genre,
                                    color = textThemeGradientColor(song.genre),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .border(1.dp, Color.White.copy(0.1f), RoundedCornerShape(10.dp))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Divider(color = LuxuryDarkCardBorder.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 24.dp))
                        }
                    }
                }
            } else {
                // Beat Marketplace module
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 120.dp, top = 8.dp)
                ) {
                    // Beat licensing notice
                    item {
                        GlassCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            borderColor = GoldenAccent.copy(0.3f)
                        ) {
                            Text("Beat Marketplace 💸", color = GoldenAccent, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(
                                "Purchase royalty-free instrumental licenses directly from independent developers with instant smart invoice generation. Monetize your songs hassle free.",
                                color = TextSecondary,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                            )
                            Button(
                                onClick = { showBeatUploadDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16161C)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth().testTag("add_beat_licence")
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "Add beat")
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("LIST A BEAT FOR SALE", fontSize = 11.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }

                    if (beats.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(40.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MusicNote,
                                    contentDescription = "No beats",
                                    tint = TextSecondaryDark,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "No beat instrumental listings listed yet. Click above to list yours!",
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        items(beats) { beat ->
                            GlassCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                                    .testTag("beat_item_${beat.id}"),
                                borderColor = LuxuryDarkCardBorder
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(beat.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "prod. ${beat.producerName}",
                                                color = GoldenAccent,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("•", color = TextSecondaryDark, fontSize = 12.sp)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("${beat.bpm} BPM", color = TextSecondary, fontSize = 11.sp)
                                        }

                                        Row(modifier = Modifier.padding(top = 4.dp)) {
                                            beat.tags.split(",").forEach { tag ->
                                                Text(
                                                    tag,
                                                    color = CyberCyan,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Black,
                                                    modifier = Modifier
                                                        .padding(end = 4.dp)
                                                        .background(Color(0x3300FFE0), RoundedCornerShape(4.dp))
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Button(
                                        onClick = { buyingBeat = beat },
                                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.testTag("purchase_${beat.id}")
                                    ) {
                                        Text(beat.price, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Dialog: Purchase beat confirmation
        buyingBeat?.let { beat ->
            Dialog(onDismissRequest = { buyingBeat = null }) {
                GlassCard(
                    modifier = Modifier.fillMaxWidth().testTag("beat_checkout_popup"),
                    borderColor = CyberCyan
                ) {
                    Text("Secure Checkout 🔐", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Licensing Contract:", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(
                        "Unlimited commercial streaming, broadcast streams up to 1M plays, 100% royalty share on ZM BEATS platform.",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("PRODUCT: ${beat.title}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    Text("PRICE: ${beat.price}", color = GoldenAccent, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    
                    Spacer(modifier = Modifier.height(20.dp))

                    GlowingButton(
                        text = "AUTHORIZE INSTANT LICENSE PURCHASE",
                        onClick = {
                            transactionReceipt = "INV-${(10000..99999).random()}-AURA"
                            buyingBeat = null
                        },
                        glowColor = CyberCyan,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Dialog: Transaction Receipt modal
        transactionReceipt?.let { receipt ->
            Dialog(onDismissRequest = { transactionReceipt = null }) {
                GlassCard(
                    modifier = Modifier.fillMaxWidth().testTag("receipt_popup"),
                    borderColor = GoldenAccent
                ) {
                    Text("Payment Successful! 🎉", color = GoldenAccent, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Transaction authorized dynamically via digital ledger. Your royalty contract has been filed.", fontSize = 12.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("RECEIPT ID: $receipt", color = CyberCyan, fontSize = 12.sp, fontWeight = FontWeight.Black)
                    Text("The instrumental files have been dispatched to your email address.", fontSize = 11.sp, color = TextSecondary)
                    
                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { transactionReceipt = null },
                        colors = ButtonDefaults.buttonColors(containerColor = LuxuryDarkCardBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("CLOSE STATION RECEIPT", color = Color.White)
                    }
                }
            }
        }

        // Dialog: Upload Beat
        if (showBeatUploadDialog) {
            Dialog(onDismissRequest = { showBeatUploadDialog = false }) {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = CyberPurple
                ) {
                    Text("Sell Beat License 💸", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))

                    TextField(
                        value = beatTitle,
                        onValueChange = { beatTitle = it },
                        label = { Text("Beat Title (e.g. 'Devil Shifter')") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        TextField(
                            value = beatBpm,
                            onValueChange = { beatBpm = it },
                            label = { Text("BPM") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TextField(
                            value = beatPrice,
                            onValueChange = { beatPrice = it },
                            label = { Text("Price ($)") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = beatTags,
                        onValueChange = { beatTags = it },
                        label = { Text("Tags (comma separated)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    GlowingButton(
                        text = "PUBLISH INSTRUMENTAL LICENSE",
                        onClick = {
                            if (beatTitle.isNotEmpty()) {
                                viewModel.uploadBeat(
                                    beatTitle,
                                    beatBpm.toIntOrNull() ?: 140,
                                    beatPrice,
                                    beatTags
                                )
                                // reset
                                beatTitle = ""
                                showBeatUploadDialog = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

// ==========================================
// 5. UPLOAD SCREEN
// ==========================================
data class LocalMockFile(
    val id: Int,
    val name: String,
    val size: String,
    val typeIcon: String,
    val typeLabel: String,
    val targetTab: Int,
    val extractedTitle: String,
    val durationSec: Int,
    val description: String,
    val subGenre: String,
    val hintText: String
)

@Composable
fun UploadScreen(
    viewModel: MusicViewModel,
    onUploadSuccess: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("Phonk") }
    var description by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("180") }

    // Custom gradient cover colors (Hex code picker) - Strictly no purple/pink
    var selectedGradientStart by remember { mutableStateOf("#00FFE0") } // Teal/Cyan default
    var selectedGradientEnd by remember { mutableStateOf("#0051FF") }   // Blue default

    val availableGradients = listOf(
        Pair("#00FFE0", "#0051FF"), // Cyber Ocean (Cyan & Blue)
        Pair("#00FF90", "#00FFE0"), // Aurora Mint (Green & Cyan)
        Pair("#FFDF7A", "#FF9F00"), // Solar Flare (Gold & Orange)
        Pair("#0051FF", "#00FF90"), // Quantum Flow (Blue & Green)
        Pair("#00FFE0", "#FFDF7A")  // Neon Sun (Teal & Gold)
    )

    var uploadTypeTab by remember { mutableStateOf(0) } // 0 = Track, 1 = Reel, 2 = Status
    var reelTitle by remember { mutableStateOf("") }
    var reelSoundName by remember { mutableStateOf("") }
    var statusText by remember { mutableStateOf("") }

    // Unified Extraction & State Network
    var isExtracting by remember { mutableStateOf(false) }
    var extractionStatus by remember { mutableStateOf("Initializing scanner...") }
    var loadedFileName by remember { mutableStateOf<String?>(null) }
    var loadedFileSize by remember { mutableStateOf<String?>(null) }
    
    // Drag and Drop Internal Tracking
    var dropZoneBounds by remember { mutableStateOf(androidx.compose.ui.geometry.Rect.Zero) }
    var isDraggingAnyLocalFile by remember { mutableStateOf(false) }
    var activeDraggedFileId by remember { mutableStateOf<Int?>(null) }

    val localFiles = remember {
        listOf(
            LocalMockFile(
                id = 1,
                name = "lost_in_the_ether.mp3",
                size = "8.42 MB",
                typeIcon = "🎵",
                typeLabel = "Audio (MP3)",
                targetTab = 0,
                extractedTitle = "Lost In The Ether",
                durationSec = 222,
                description = "Ethereal wave networks captured on local device. Smooth synth progressions with cyberpunk vocal underlays.",
                subGenre = "Synthwave",
                hintText = "Fills Tracks tab"
            ),
            LocalMockFile(
                id = 2,
                name = "toxic_phonk_frequencies.wav",
                size = "41.15 MB",
                typeIcon = "⚡",
                typeLabel = "Audio (WAV)",
                targetTab = 0,
                extractedTitle = "TOXIC PHONK FREQUENCIES",
                durationSec = 158,
                description = "High octane Memphis drift style WAV file. Dynamic cowbell grids with extreme low frequency resonance.",
                subGenre = "Phonk",
                hintText = "Fills Tracks tab with Phonk preset"
            ),
            LocalMockFile(
                id = 3,
                name = "tokyo_by_night_underlay.mp4",
                size = "24.80 MB",
                typeIcon = "🎥",
                typeLabel = "Video (MP4)",
                targetTab = 1,
                extractedTitle = "Tokyo neon afterglow driving visual capture #Tokyovibes",
                durationSec = 45,
                description = "Neon Tokyo driving loop underlay",
                subGenre = "Midnight Drift Beats",
                hintText = "Fills Reels tab"
            ),
            LocalMockFile(
                id = 4,
                name = "philosopher_insights.txt",
                size = "1.5 KB",
                typeIcon = "📄",
                typeLabel = "Text (TXT)",
                targetTab = 2,
                extractedTitle = "Symphony of cosmic waves. Reality is only vibrational alignment.",
                durationSec = 0,
                description = "",
                subGenre = "",
                hintText = "Fills Status tab"
            )
        )
    }

    // Direct Metadata Extraction utility
    fun triggerFileExtraction(file: LocalMockFile) {
        isExtracting = true
        extractionStatus = "Scanning ${file.name} frequencies..."
        scope.launch {
            delay(800)
            extractionStatus = "Extracting ID3 header logistics..."
            delay(700)
            
            when (file.targetTab) {
                0 -> {
                    uploadTypeTab = 0
                    title = file.extractedTitle
                    duration = file.durationSec.toString()
                    description = file.description
                    genre = file.subGenre
                }
                1 -> {
                    uploadTypeTab = 1
                    reelTitle = file.extractedTitle
                    reelSoundName = file.subGenre
                }
                2 -> {
                    uploadTypeTab = 2
                    statusText = file.extractedTitle
                }
            }
            
            isExtracting = false
            loadedFileName = file.name
            loadedFileSize = file.size
            android.widget.Toast.makeText(context, "METADATA DISPATCH SUCCESSFUL: Imported ${file.name}!", android.widget.Toast.LENGTH_LONG).show()
        }
    }

    // Native android storage file system picker
    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: android.net.Uri? ->
        uri?.let { selectedUri ->
            var displayName = "local_dispatch.mp3"
            var sizeInBytes: Long = 5240000 // 5.24MB approx placeholder
            try {
                context.contentResolver.query(selectedUri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    val sizeIndex = cursor.getColumnIndex(android.provider.OpenableColumns.SIZE)
                    if (cursor.moveToFirst()) {
                        if (nameIndex != -1) displayName = cursor.getString(nameIndex) ?: "imported_node.wav"
                        if (sizeIndex != -1) sizeInBytes = cursor.getLong(sizeIndex)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val mimeType = context.contentResolver.getType(selectedUri) ?: ""
            val isVideo = mimeType.startsWith("video") || displayName.endsWith(".mp4", ignoreCase = true) || displayName.endsWith(".mov", ignoreCase = true)
            val isAudio = mimeType.startsWith("audio") || displayName.endsWith(".mp3", ignoreCase = true) || displayName.endsWith(".wav", ignoreCase = true) || displayName.endsWith(".flac", ignoreCase = true)

            isExtracting = true
            extractionStatus = "Mapping signal connection to $displayName..."
            
            var extractedDurationSec = 180
            try {
                val retriever = android.media.MediaMetadataRetriever()
                retriever.setDataSource(context, selectedUri)
                val durationStr = retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION)
                if (durationStr != null) {
                    extractedDurationSec = (durationStr.toLong() / 1000).toInt()
                }
                retriever.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            scope.launch {
                delay(950)
                extractionStatus = "Decrypting compressed frequency bitrate..."
                delay(750)
                
                val cleanTitle = displayName.substringBeforeLast(".")
                if (isVideo) {
                    uploadTypeTab = 1
                    reelTitle = "Aura Capture of $cleanTitle 🎬"
                    reelSoundName = "Original Sound Signature"
                } else if (isAudio) {
                    uploadTypeTab = 0
                    title = cleanTitle
                    duration = extractedDurationSec.toString()
                    genre = if (cleanTitle.lowercase().contains("phonk")) "Phonk" else "Synthwave"
                    description = "High fidelity direct stream extracted from your local device storage."
                } else {
                    uploadTypeTab = 2
                    statusText = "Sync: $cleanTitle"
                }

                isExtracting = false
                loadedFileName = displayName
                loadedFileSize = "${String.format("%.2f", sizeInBytes / (1024.0 * 1024.0))} MB"
                android.widget.Toast.makeText(context, "NATIVE FILE METADATA EXTRACTED: Loaded $displayName!", android.widget.Toast.LENGTH_LONG).show()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("upload_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                Text("TRANSMIT WAVES", color = GoldenAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                Text("UPLOAD HUB", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black, fontStyle = FontStyle.Italic, letterSpacing = (-0.5).sp)
            }

            // UNIFIED METADATA DISPATCH CONTAINER & DROP ZONE
            val infiniteTransition = rememberInfiniteTransition(label = "Pulse")
            val pulseFactor by infiniteTransition.animateFloat(
                initialValue = 0.5f,
                targetValue = 1.0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1200, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "Pulse"
            )

            val dropZoneBorderBrush = if (isDraggingAnyLocalFile) {
                Brush.sweepGradient(listOf(CyberCyan.copy(alpha = pulseFactor), CyberPurple.copy(alpha = pulseFactor)))
            } else {
                Brush.sweepGradient(listOf(CyberPurple, CyberCyan))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF09090C))
                    .border(
                        width = if (isDraggingAnyLocalFile) 2.dp else 1.dp,
                        brush = dropZoneBorderBrush,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .onGloballyPositioned { coords ->
                        dropZoneBounds = coords.boundsInRoot()
                    }
                    .clickable {
                        filePicker.launch("*/*")
                    }
                    .testTag("upload_dropzone"),
                contentAlignment = Alignment.Center
            ) {
                if (isExtracting) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        CircularProgressIndicator(color = CyberCyan, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = extractionStatus,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "KEEP EMULATOR PIPELINE CONNECTED",
                            color = TextSecondary,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                } else if (loadedFileName != null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Active connection",
                                tint = CyberCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "FREQUENCY PARSED & SECURED",
                                color = CyberCyan,
                                fontWeight = FontWeight.Black,
                                fontSize = 9.sp,
                                letterSpacing = 2.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = loadedFileName!!,
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Size: ${loadedFileSize ?: "Unknown"} • Active Channel Matrix",
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White.copy(0.1f))
                                    .clickable {
                                        loadedFileName = null
                                        loadedFileSize = null
                                        title = ""
                                        reelTitle = ""
                                        reelSoundName = ""
                                        statusText = ""
                                    }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("EJECT FILE", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CyberPurple.copy(0.3f))
                                    .clickable { filePicker.launch("*/*") }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("BROWSE AGAIN", color = CyberCyan, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudUpload,
                            contentDescription = "Cloud upload core",
                            tint = if (isDraggingAnyLocalFile) CyberCyan else TextSecondary,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isDraggingAnyLocalFile) "DETECTION BOUNDS ACTIVE" else "TAP TO BROWSE OR DRAG & DROP",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Supports MP3, WAV, MP4, and TXT (Auto-detects tab format)",
                            color = TextSecondary,
                            fontSize = 9.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Three-way tab switcher
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF101014))
            ) {
                listOf("TRACKS", "REELS", "STATUS").forEachIndexed { index, name ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { uploadTypeTab = index }
                            .background(if (uploadTypeTab == index) LuxuryDarkCardBorder else Color.Transparent)
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = name,
                            color = if (uploadTypeTab == index) Color.White else TextSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            if (uploadTypeTab == 0) {
                // TRACK UPLOAD VIEW
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = LuxuryDarkCardBorder
                ) {
                    Text("TRACK LOGISTICS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.padding(bottom = 12.dp))

                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Track Title") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF14141D),
                            unfocusedContainerColor = Color(0xFF0C0C0E),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("upload_title_field")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("SELECT SUB-GENRE TAG", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    LazyRow(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val genres = listOf("Phonk", "Synthwave", "Dreampop", "Lofi Hip Hop", "Emo Rap", "Hyperpop", "Trap")
                        items(genres) { g ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (genre == g) CyberPurple else Color(0xFF16161C))
                                    .border(1.dp, if (genre == g) CyberCyan else Color.Transparent, RoundedCornerShape(12.dp))
                                    .clickable { genre = g }
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(g, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    TextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Inspirational Bio/Description") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF14141D),
                            unfocusedContainerColor = Color(0xFF0C0C0E),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    TextField(
                        value = duration,
                        onValueChange = { duration = it },
                        label = { Text("Playback Duration (Seconds)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF14141D),
                            unfocusedContainerColor = Color(0xFF0C0C0E),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else if (uploadTypeTab == 1) {
                // REEL UPLOAD VIEW
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = LuxuryDarkCardBorder
                ) {
                    Text("REEL SPECIFICATIONS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.padding(bottom = 12.dp))

                    TextField(
                        value = reelTitle,
                        onValueChange = { reelTitle = it },
                        label = { Text("Reel Caption / Details") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF14141D),
                            unfocusedContainerColor = Color(0xFF0C0C0E),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("reel_title_field")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    TextField(
                        value = reelSoundName,
                        onValueChange = { reelSoundName = it },
                        label = { Text("Underlay Sound Track Name") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF14141D),
                            unfocusedContainerColor = Color(0xFF0C0C0E),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                // STATUS UPLOAD VIEW
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = LuxuryDarkCardBorder
                ) {
                    Text("STATUS QUOTE SPECIFICATIONS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.padding(bottom = 12.dp))

                    TextField(
                        value = statusText,
                        onValueChange = { if (it.length <= 80) statusText = it },
                        label = { Text("Status Message (Max 80 Characters)") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF14141D),
                            unfocusedContainerColor = Color(0xFF0C0C0E),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("upload_status_field")
                    )
                }
            }

            // Cover Art Chromatics Picker
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text("AESTHETIC DECK ACCENT", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("Select custom color gradients to represent your item's vibe", color = TextSecondary, fontSize = 11.sp, modifier = Modifier.padding(bottom = 12.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(availableGradients) { p ->
                        val startHex = try { Color(android.graphics.Color.parseColor(p.first)) } catch(e: Exception) { CyberPurple }
                        val endHex = try { Color(android.graphics.Color.parseColor(p.second)) } catch(e: Exception) { CyberCyan }

                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Brush.linearGradient(listOf(startHex, endHex)))
                                .border(
                                    width = if (selectedGradientStart == p.first) 2.dp else 0.dp,
                                    color = Color.White,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    selectedGradientStart = p.first
                                    selectedGradientEnd = p.second
                                }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            GlowingButton(
                text = when (uploadTypeTab) {
                    0 -> "BROADCAST TRANSMISSION NOW"
                    1 -> "POST REEL ON ACCOUNT"
                    else -> "BROADCAST STATUS NOW"
                },
                onClick = {
                    when (uploadTypeTab) {
                        0 -> {
                            if (title.isNotEmpty()) {
                                viewModel.uploadSong(
                                    title = title,
                                    description = description,
                                    genre = genre,
                                    durationSec = duration.toIntOrNull() ?: 180,
                                    startColor = selectedGradientStart,
                                    endColor = selectedGradientEnd
                                )
                                onUploadSuccess()
                            } else {
                                android.widget.Toast.makeText(context, "Please set a Track Title first", android.widget.Toast.LENGTH_SHORT).show()
                            }
                        }
                        1 -> {
                            if (reelTitle.isNotEmpty()) {
                                viewModel.uploadReel(
                                    title = reelTitle,
                                    sound = if (reelSoundName.isEmpty()) "Original Sound" else reelSoundName,
                                    coverStart = selectedGradientStart,
                                    coverEnd = selectedGradientEnd
                                )
                                onUploadSuccess()
                            } else {
                                android.widget.Toast.makeText(context, "Please set a Reel Caption first", android.widget.Toast.LENGTH_SHORT).show()
                            }
                        }
                        2 -> {
                            if (statusText.isNotEmpty()) {
                                viewModel.uploadStatus(
                                    text = statusText,
                                    bgStart = selectedGradientStart,
                                    bgEnd = selectedGradientEnd
                                )
                                onUploadSuccess()
                            } else {
                                android.widget.Toast.makeText(context, "Please set Status text first", android.widget.Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("upload_submit_btn")
            )

            // INTERACTIVE LOCAL DEVICE SANDBOX VAULT (DRAG & DROP SIMULATOR)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF0A0A0F))
                    .border(1.dp, LuxuryDarkCardBorder.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("🔒", fontSize = 14.sp)
                    Text(
                        text = "LOCAL DEVICE STORAGE COMPARTMENT",
                        color = GoldenAccent,
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp,
                        letterSpacing = 1.5.sp
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Press and Drag a file onto the Drop Zone above, or tap to quickly extract its metadata waves into the platform form.",
                    color = TextSecondary,
                    fontSize = 10.sp,
                    lineHeight = 14.sp
                )
                
                Spacer(modifier = Modifier.height(14.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(localFiles) { file ->
                        var itemOffset by remember { mutableStateOf(androidx.compose.ui.geometry.Offset.Zero) }
                        var isCurrentlyDraggingThis by remember { mutableStateOf(false) }
                        var itemBounds by remember { mutableStateOf(androidx.compose.ui.geometry.Rect.Zero) }

                        Box(
                            modifier = Modifier
                                .width(170.dp)
                                .onGloballyPositioned { coords ->
                                    if (!isCurrentlyDraggingThis) {
                                        itemBounds = coords.boundsInRoot()
                                    }
                                }
                                .offset {
                                    androidx.compose.ui.unit.IntOffset(itemOffset.x.toInt(), itemOffset.y.toInt())
                                }
                                .zIndex(if (isCurrentlyDraggingThis) 15f else 1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isCurrentlyDraggingThis) Color(0xFF1C1C24) else Color(0xFF111116))
                                .border(
                                    width = 1.dp,
                                    color = if (isCurrentlyDraggingThis) CyberCyan else Color.White.copy(0.08f),
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .pointerInput(file) {
                                    detectDragGestures(
                                        onDragStart = {
                                            isCurrentlyDraggingThis = true
                                            isDraggingAnyLocalFile = true
                                            activeDraggedFileId = file.id
                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            itemOffset += dragAmount
                                        },
                                        onDragEnd = {
                                            isCurrentlyDraggingThis = false
                                            isDraggingAnyLocalFile = false
                                            activeDraggedFileId = null
                                            
                                            // Check drop geometry overlap
                                            val currentCenter = itemBounds.center + itemOffset
                                            if (dropZoneBounds.contains(currentCenter)) {
                                                triggerFileExtraction(file)
                                            }
                                            itemOffset = androidx.compose.ui.geometry.Offset.Zero
                                        },
                                        onDragCancel = {
                                            isCurrentlyDraggingThis = false
                                            isDraggingAnyLocalFile = false
                                            activeDraggedFileId = null
                                            itemOffset = androidx.compose.ui.geometry.Offset.Zero
                                        }
                                    )
                                }
                                .padding(12.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(file.typeIcon, fontSize = 16.sp)
                                    Column {
                                        Text(
                                            text = file.typeLabel,
                                            color = CyberCyan,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 8.sp,
                                            letterSpacing = 0.5.sp
                                        )
                                        Text(
                                            text = file.size,
                                            color = TextSecondary,
                                            fontSize = 8.sp
                                        )
                                    }
                                }

                                Text(
                                    text = file.name,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    text = file.hintText,
                                    color = TextSecondary,
                                    fontSize = 8.sp,
                                    lineHeight = 11.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(CyberCyan.copy(0.12f))
                                        .border(1.dp, CyberCyan.copy(0.3f), RoundedCornerShape(8.dp))
                                        .clickable {
                                            triggerFileExtraction(file)
                                        }
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "TAP TO EXTRACT",
                                        color = CyberCyan,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 8.sp,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

// ==========================================
// 6. MESSAGING / FAN INTERACTION CHATS
// ==========================================
@Composable
fun MessagesScreen(
    viewModel: MusicViewModel,
    onNavigateToProfile: (String) -> Unit
) {
    val artists by viewModel.allArtists.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val activeChatRecipient by viewModel.activeChatRecipient.collectAsStateWithLifecycle()
    val chatMessages by viewModel.activeChatMessages.collectAsStateWithLifecycle()

    var messageText by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("messages_screen")
    ) {
        if (activeChatRecipient == null) {
            // General inbox list
            Column(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 16.dp, bottom = 8.dp)
                ) {
                    Text("ARTIST FREQUENCIES", color = GoldenAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                    Text("INBOX HUB", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black, fontStyle = FontStyle.Italic, letterSpacing = (-0.5).sp)
                }

                // Chat list
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 120.dp, top = 8.dp)
                ) {
                    items(artists) { artist ->
                        if (artist.id != currentUser?.id) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.openChatWith(artist) }
                                    .padding(horizontal = 24.dp, vertical = 14.dp)
                                    .testTag("chat_row_${artist.id}"),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AvatarWithGradient(
                                    displayName = artist.displayName,
                                    startColor = artist.avatarGradientStart,
                                    endColor = artist.avatarGradientEnd,
                                    modifier = Modifier.size(52.dp),
                                    isVerified = artist.isVerified,
                                    avatarUri = artist.avatarUri
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(artist.displayName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Text(
                                        artist.bio,
                                        color = TextSecondary,
                                        fontSize = 12.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    // Live status indicator (glowing teal particle)
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(CyberCyan, CircleShape)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Active", color = CyberCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Divider(color = LuxuryDarkCardBorder.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 24.dp))
                        }
                    }
                }
            }
        } else {
            // Active Messaging box
            val recipient = activeChatRecipient!!
            Column(modifier = Modifier.fillMaxSize().background(ObsidianBackground)) {
                // Header of DM
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0F0F12))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { viewModel.closeChat() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    AvatarWithGradient(
                        displayName = recipient.displayName,
                        startColor = recipient.avatarGradientStart,
                        endColor = recipient.avatarGradientEnd,
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { onNavigateToProfile(recipient.id) },
                        avatarUri = recipient.avatarUri
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(recipient.displayName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("COLLABORATION FREQUENCY INSECURE CHANNEL", color = CyberCyan, fontSize = 9.sp, letterSpacing = 1.sp)
                    }
                }

                // Messages list scrolling
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        items(chatMessages) { message ->
                            val isMe = message.senderId == currentUser?.id

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(
                                            topStart = 16.dp,
                                            topEnd = 16.dp,
                                            bottomStart = if (isMe) 16.dp else 4.dp,
                                            bottomEnd = if (isMe) 4.dp else 16.dp
                                        ))
                                        .background(if (isMe) CyberPurple else Color(0xFF1E1E26))
                                        .padding(horizontal = 14.dp, vertical = 10.dp)
                                        .widthIn(max = 260.dp)
                                ) {
                                    Column {
                                        Text(message.text, color = Color.White, fontSize = 13.sp)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            formatTimeAgo(message.timestamp),
                                            color = Color.White.copy(0.4f),
                                            fontSize = 9.sp,
                                            modifier = Modifier.align(Alignment.End)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Divider(color = LuxuryDarkCardBorder)

                // Input box
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        placeholder = { Text("Secure message, collaboration specs...") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF16161D),
                            unfocusedContainerColor = Color(0xFF0C0C0E),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_field_input"),
                        shape = RoundedCornerShape(24.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (messageText.trim().isNotEmpty()) {
                                viewModel.sendChatMessage(messageText)
                                messageText = ""
                            }
                        },
                        modifier = Modifier
                            .background(GoldenAccent, CircleShape)
                            .testTag("chat_send_button")
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.Black)
                    }
                }
            }
        }
    }
}

// ==========================================
// 7. NOTIFICATIONS LIST SCREEN
// ==========================================
@Composable
fun NotificationsScreen(viewModel: MusicViewModel) {
    val alerts by viewModel.notifications.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("notifications_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 16.dp, bottom = 8.dp)
            ) {
                Text("SOCIAL IMPACT", color = GoldenAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                Text("ALERTS FEED", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black, fontStyle = FontStyle.Italic, letterSpacing = (-0.5).sp)
            }

            if (alerts.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No social wave impacts registered yet.", color = TextSecondary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 120.dp, top = 8.dp)
                ) {
                    items(alerts) { alert ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF0D0D11))
                                .border(1.dp, LuxuryDarkCardBorder, RoundedCornerShape(12.dp))
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val alertIcon = when(alert.type) {
                                "like" -> Icons.Default.Favorite
                                "follow" -> Icons.Default.PersonAdd
                                "upload" -> Icons.Default.CloudUpload
                                "comment" -> Icons.Default.Comment
                                else -> Icons.Default.CheckCircle
                            }
                            val alertIconColor = when(alert.type) {
                                "like" -> GlowPink
                                "follow" -> CyberCyan
                                "upload" -> GoldenAccent
                                "comment" -> CyberPurple
                                else -> GoldenAccent
                            }

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(alertIconColor.copy(alpha = 0.15f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = alertIcon, contentDescription = "type", tint = alertIconColor, modifier = Modifier.size(18.dp))
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(alert.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(alert.content, color = TextSecondary, fontSize = 11.sp)
                            }

                            Text(formatTimeAgo(alert.timestamp), color = TextSecondaryDark, fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 8. PLAYLIST LIBRARY / FAVORITES
// ==========================================
@Composable
fun LibFavoritesScreen(
    viewModel: MusicViewModel,
    onNavigateToProfile: (String) -> Unit
) {
    val favorites by viewModel.likedSongs.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("library_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 16.dp, bottom = 8.dp)
            ) {
                Text("RECORD VAULT", color = GoldenAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                Text("MY STATION", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black, fontStyle = FontStyle.Italic, letterSpacing = (-0.5).sp)
            }

            if (favorites.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = "heart", tint = TextSecondaryDark, modifier = Modifier.size(50.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Your library station has no favorites liked.", color = TextSecondary)
                    Text("Tap the heart on home tracks to seed your vault!", color = GoldenAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 120.dp, top = 8.dp)
                ) {
                    items(favorites) { song ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.playSong(song, favorites) }
                                .padding(horizontal = 24.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val sColor = remember { try { Color(android.graphics.Color.parseColor(song.coverColorStart)) } catch(e: Exception) { CyberPurple } }
                            val eColor = remember { try { Color(android.graphics.Color.parseColor(song.coverColorEnd)) } catch(e: Exception) { CyberCyan } }

                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Brush.linearGradient(listOf(sColor, eColor))),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.Favorite, contentDescription = "heart", tint = Color.Black.copy(0.4f))
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(song.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(song.artistName, color = TextSecondary, fontSize = 12.sp)
                            }

                            IconButton(onClick = { viewModel.toggleLikeSong(song.id) }) {
                                Icon(imageVector = Icons.Default.Favorite, contentDescription = "unlike", tint = GlowPink)
                            }
                        }
                        Divider(color = LuxuryDarkCardBorder.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 24.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun StatMetricColumn(label: String, valStr: String, glowColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = valStr, color = glowColor, fontSize = 20.sp, fontWeight = FontWeight.Black)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = label, color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

// ==========================================
// 9. PROFILE & ANALYTICS SCREEN
// ==========================================
@Composable
fun ProfileScreen(
    artistId: String,
    viewModel: MusicViewModel,
    onBack: () -> Unit,
    onNavigateToChat: (ArtistProfile) -> Unit
) {
    val artists by viewModel.allArtists.collectAsStateWithLifecycle()
    val songs by viewModel.allSongs.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    val profileArtist = artists.find { it.id == artistId }
    val artistSongs = songs.filter { it.artistId == artistId }

    val isFollowing = viewModel.isFollowingArtist(artistId).collectAsStateWithLifecycle(initialValue = false)

    var currentViewTab by remember { mutableStateOf(0) } // 0 = Music Discography, 1 = Analytics dashboard

    var showEditBioDialog by remember { mutableStateOf(false) }
    var editNameInput by remember { mutableStateOf("") }
    var editBioInput by remember { mutableStateOf("") }
    var editAvatarUri by remember { mutableStateOf<String?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: android.net.Uri? ->
        if (uri != null) {
            editAvatarUri = uri.toString()
        }
    }

    if (profileArtist == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Artist ID '$artistId' not found.", color = Color.White)
        }
        return
    }

    val isSelf = currentUser?.id == artistId

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("profile_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header with back button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("ARTIST STATION PORTAL", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            }

            // Big Card Header with avatar
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                borderColor = if (profileArtist.isVerified) GoldenAccent.copy(0.4f) else LuxuryDarkCardBorder
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AvatarWithGradient(
                        displayName = profileArtist.displayName,
                        startColor = profileArtist.avatarGradientStart,
                        endColor = profileArtist.avatarGradientEnd,
                        modifier = Modifier.size(76.dp),
                        isVerified = profileArtist.isVerified,
                        avatarUri = profileArtist.avatarUri
                    )

                    Spacer(modifier = Modifier.width(18.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(profileArtist.displayName, color = Color.White, fontWeight = FontWeight.Black, fontStyle = FontStyle.Italic, fontSize = 21.sp)
                            if (profileArtist.isVerified) {
                                Spacer(modifier = Modifier.width(6.dp))
                                VerificationBadge(size = 18.dp)
                            }
                        }
                        Text("@${profileArtist.id}", color = TextSecondary, fontSize = 12.sp)
                        Text(
                            text = profileArtist.bio,
                            color = Color.White.copy(0.8f),
                            fontSize = 13.sp,
                            modifier = Modifier.padding(top = 4.dp),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Social Metrics Count
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatMetricColumn("Stream impact", formatStatCount(artistSongs.sumOf { it.plays }), GoldenAccent)
                    StatMetricColumn("Followers", formatStatCount(profileArtist.followers), CyberCyan)
                    StatMetricColumn("Following", formatStatCount(profileArtist.following), CyberPurple)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Major Dynamic Actions profile depending on Self vs other artist
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (isSelf) {
                        Button(
                            onClick = {
                                editNameInput = profileArtist.displayName
                                editBioInput = profileArtist.bio
                                editAvatarUri = profileArtist.avatarUri
                                showEditBioDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = LuxuryDarkCardBorder),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.weight(1f).testTag("edit_profile_bio")
                        ) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "", modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("EDIT BIO", fontSize = 11.sp, fontWeight = FontWeight.Black)
                        }

                        if (!profileArtist.isVerified) {
                            Button(
                                onClick = { viewModel.requestVerification() },
                                colors = ButtonDefaults.buttonColors(containerColor = GoldenAccent),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.weight(1f).testTag("click_verify_badge")
                            ) {
                                Icon(imageVector = Icons.Default.Verified, contentDescription = "", tint = Color.Black, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("GET ACCREDITED", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    } else {
                        // User looking at other artists
                        Button(
                            onClick = { viewModel.toggleFollowArtist(profileArtist.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = if (isFollowing.value) LuxuryDarkCardBorder else CyberCyan),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.weight(1f).testTag("follow_action_btn")
                        ) {
                            Text(
                                text = if (isFollowing.value) "UNFOLLOW" else "FOLLOW CREATOR",
                                color = if (isFollowing.value) Color.White else Color.Black,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = { onNavigateToChat(profileArtist) },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberPurple),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.Chat, contentDescription = "chat", modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("OFFER COLLAB", fontSize = 11.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Sub tabs for profile view: Discography vs Reels vs Metrics
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF101014))
            ) {
                listOf("DISCOGRAPHY", "REELS", "METRICS").forEachIndexed { index, title ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { currentViewTab = index }
                            .background(if (currentViewTab == index) LuxuryDarkCardBorder else Color.Transparent)
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            color = if (currentViewTab == index) Color.White else TextSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            // Sub views rendering
            if (currentViewTab == 0) {
                // Discography list
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 120.dp, top = 8.dp)
                ) {
                    if (artistSongs.isEmpty()) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                                Text("No tracks uploaded. The vaults are dry.", color = TextSecondary, fontSize = 13.sp)
                            }
                        }
                    } else {
                        items(artistSongs) { song ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.playSong(song, artistSongs) }
                                    .padding(horizontal = 24.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val sColor = remember { try { Color(android.graphics.Color.parseColor(song.coverColorStart)) } catch(e: Exception) { CyberPurple } }
                                val eColor = remember { try { Color(android.graphics.Color.parseColor(song.coverColorEnd)) } catch(e: Exception) { CyberCyan } }

                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Brush.linearGradient(listOf(sColor, eColor))),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(imageVector = Icons.Default.MusicNote, contentDescription = "", tint = Color.Black.copy(0.4f))
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(song.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Row {
                                        Text(song.genre, color = TextSecondary, fontSize = 12.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("${song.plays} Plays", color = GoldenAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.White)
                            }
                            Divider(color = LuxuryDarkCardBorder.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 24.dp))
                        }
                    }
                }
            } else if (currentViewTab == 1) {
                // Reels visual grid list (simple beautiful pairs)
                val artistReels by viewModel.getReelsByArtist(profileArtist.id).collectAsStateWithLifecycle(initialValue = emptyList())

                if (artistReels.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                        Text("No reels uploaded by this artist yet.", color = TextSecondary, fontSize = 13.sp)
                    }
                } else {
                    val chunkedReels = remember(artistReels) { artistReels.chunked(2) }
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 120.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(chunkedReels) { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                rowItems.forEach { reel ->
                                    var showReelPlayer by remember { mutableStateOf(false) }
                                    if (showReelPlayer) {
                                        ReelsPlayerDialog(reels = listOf(reel), initialIndex = 0, viewModel = viewModel, onDismiss = { showReelPlayer = false })
                                    }
                                    val sColor = remember(reel) { try { Color(android.graphics.Color.parseColor(reel.coverColorStart)) } catch(e: Exception) { CyberPurple } }
                                    val eColor = remember(reel) { try { Color(android.graphics.Color.parseColor(reel.coverColorEnd)) } catch(e: Exception) { CyberCyan } }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(0.85f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Brush.linearGradient(listOf(sColor, eColor)))
                                            .border(1.dp, Color.White.copy(0.12f), RoundedCornerShape(12.dp))
                                            .clickable { showReelPlayer = true }
                                            .padding(12.dp),
                                        contentAlignment = Alignment.BottomStart
                                    ) {
                                        Column {
                                            Text(
                                                text = reel.title,
                                                color = Color.White,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(imageVector = Icons.Default.Favorite, contentDescription = "", tint = GlowPink, modifier = Modifier.size(10.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "${reel.likes}",
                                                    color = Color.White.copy(0.8f),
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }
                                    }
                                }
                                // Fill of empty space if odd number of items
                                if (rowItems.size < 2) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            } else {
                // Statistical Analytics visualizer panel
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 120.dp)
                ) {
                    item {
                        GlassCard(modifier = Modifier.fillMaxWidth()) {
                            Text("ZM BEATS ANALYTICS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Realtime analytical representation of streamer activity and acoustic footprints across ZM BEATS node databases.", color = TextSecondary, fontSize = 11.sp, modifier = Modifier.padding(bottom = 16.dp))

                            // Custom drawn growth chart using Vector lines Canvas drawing!
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .background(Color(0xFF040406), RoundedCornerShape(12.dp))
                                    .border(1.dp, LuxuryDarkCardBorder, RoundedCornerShape(12.dp))
                                    .padding(12.dp)
                            ) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    val w = size.width
                                    val h = size.height

                                    // Guide lines grids
                                    drawLine(color = Color(0xFF16161C), start = Offset(0f, h*0.25f), end = Offset(w, h*0.25f), strokeWidth = 1f)
                                    drawLine(color = Color(0xFF16161C), start = Offset(0f, h*0.5f), end = Offset(w, h*0.5f), strokeWidth = 1f)
                                    drawLine(color = Color(0xFF16161C), start = Offset(0f, h*0.75f), end = Offset(w, h*0.75f), strokeWidth = 1f)

                                    // Line graph representing streaming impression growth
                                    val points = listOf(
                                        Offset(0f, h * 0.9f),
                                        Offset(w * 0.15f, h * 0.8f),
                                        Offset(w * 0.3f, h * 0.85f),
                                        Offset(w * 0.45f, h * 0.5f),
                                        Offset(w * 0.6f, h * 0.62f),
                                        Offset(w * 0.75f, h * 0.2f),
                                        Offset(w * 0.9f, h * 0.35f),
                                        Offset(w, h * 0.05f)
                                    )

                                    val neonPath = Path().apply {
                                        points.forEachIndexed { idx, pt ->
                                            if (idx == 0) moveTo(pt.x, pt.ptY(h)) else lineTo(pt.x, pt.ptY(h))
                                        }
                                    }

                                    // Draw background shadow gradient below path
                                    val fillPath = Path().apply {
                                        points.forEachIndexed { idx, pt ->
                                            if (idx == 0) moveTo(pt.x, pt.ptY(h)) else lineTo(pt.x, pt.ptY(h))
                                        }
                                        lineTo(w, h)
                                        lineTo(0f, h)
                                        close()
                                    }
                                    drawPath(
                                        fillPath,
                                        brush = Brush.verticalGradient(
                                            listOf(CyberPurple.copy(0.2f), Color.Transparent)
                                        )
                                    )

                                    // Draw bold glowing path
                                    drawPath(
                                        neonPath,
                                        brush = Brush.horizontalGradient(listOf(CyberPurple, CyberCyan)),
                                        style = Stroke(width = 4.dp.toPx())
                                    )

                                    // Draw anchor node particles
                                    points.forEach { pt ->
                                        drawCircle(color = CyberCyan, radius = 5f, center = Offset(pt.x, pt.ptY(h)))
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Mon - week start", color = TextSecondary, fontSize = 10.sp)
                                Text("Growth velocity: +42.1%", color = CyberCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text("Sun - live", color = TextSecondary, fontSize = 10.sp)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))

                        // Numeric Metrics
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(modifier = Modifier.weight(1f).background(Color(0xFF0D0D11), RoundedCornerShape(12.dp)).border(1.dp, LuxuryDarkCardBorder, RoundedCornerShape(12.dp)).padding(16.dp)) {
                                Column {
                                    Text("PRODUCER REVENUE", color = TextSecondary, fontSize = 10.sp)
                                    Text("$482.50", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    Text("Beat marketplace", color = CyberCyan, fontSize = 11.sp)
                                }
                            }

                            Box(modifier = Modifier.weight(1f).background(Color(0xFF0D0D11), RoundedCornerShape(12.dp)).border(1.dp, LuxuryDarkCardBorder, RoundedCornerShape(12.dp)).padding(16.dp)) {
                                Column {
                                    Text("ACOUSTIC REACH", color = TextSecondary, fontSize = 10.sp)
                                    Text("12.4K", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    Text("Unique nodes synced", color = CyberPurple, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Dialog: Edit Bio
        if (showEditBioDialog) {
            Dialog(onDismissRequest = { showEditBioDialog = false }) {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = CyberPurple
                ) {
                    Text("Decompress Station Credentials 🔑", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Avatar Preview and file picker
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AvatarWithGradient(
                            displayName = editNameInput.ifEmpty { profileArtist.displayName },
                            startColor = profileArtist.avatarGradientStart,
                            endColor = profileArtist.avatarGradientEnd,
                            modifier = Modifier.size(54.dp),
                            avatarUri = editAvatarUri
                        )
                        Button(
                            onClick = { imagePickerLauncher.launch("image/*") },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberCyan.copy(0.2f)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).testTag("select_avatar_btn")
                        ) {
                            Text("IMPORT CURRENT PORTRAIT 🖼️", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    TextField(
                        value = editNameInput,
                        onValueChange = { editNameInput = it },
                        label = { Text("Display Name/Alias") },
                        modifier = Modifier.fillMaxWidth().testTag("edit_display_name_field")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    TextField(
                        value = editBioInput,
                        onValueChange = { editBioInput = it },
                        label = { Text("Aesthetic artist description") },
                        modifier = Modifier.fillMaxWidth().testTag("edit_bio_text_field")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    GlowingButton(
                        text = "RE-CALIBRATE ID CREDENTIALS",
                        onClick = {
                            viewModel.updateProfileBio(editNameInput, editBioInput, editAvatarUri)
                            showEditBioDialog = false
                        },
                        modifier = Modifier.fillMaxWidth().testTag("save_profile_btn")
                    )
                }
            }
        }
    }
}

// ==========================================
// 10. GLASSMORPHIC FULL MUSIC PLAYER PANEL
// ==========================================
@Composable
fun MusicPlayerPanel(
    viewModel: MusicViewModel,
    onNavigateToProfile: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val song by viewModel.currentPlayingSong.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
    val positionSeconds by viewModel.playbackPositionSec.collectAsStateWithLifecycle()
    val durationSeconds by viewModel.playbackDurationSec.collectAsStateWithLifecycle()
    val autotuneStrength by viewModel.autotuneStrength.collectAsStateWithLifecycle()
    val playbackSpeed by viewModel.playbackSpeed.collectAsStateWithLifecycle()
    val bassBoost by viewModel.bassBoost.collectAsStateWithLifecycle()

    var isFullSize by remember { mutableStateOf(false) }

    if (song == null) return

    val totalDuration = if (durationSeconds > 0) durationSeconds else 180
    val progressFraction = positionSeconds.toFloat() / totalDuration

    if (!isFullSize) {
        // SMALL FLOATING BOTTOM BAR PLAYER
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(bottom = 8.dp) // Perfect offset from the bottom navigation bar (parent Box already applies Scaffold's innerPadding)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xE60B0B0F))
                .border(1.dp, LuxuryDarkCardBorder, RoundedCornerShape(20.dp))
                .clickable { isFullSize = true }
                .padding(horizontal = 14.dp, vertical = 8.dp)
                .testTag("compact_music_player")
        ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Album artwork gradient representation
                    val sColor = remember(song) { try { Color(android.graphics.Color.parseColor(song!!.coverColorStart)) } catch(e: Exception) { CyberPurple } }
                    val eColor = remember(song) { try { Color(android.graphics.Color.parseColor(song!!.coverColorEnd)) } catch(e: Exception) { CyberCyan } }

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Brush.linearGradient(listOf(sColor, eColor))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.MusicNote, contentDescription = "", tint = Color.Black.copy(0.4f))
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = song!!.title,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "prod. ${song!!.artistName}",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            maxLines = 1
                        )
                    }

                    // Simple visualizer indicator
                    MusicVisualizer(isPlaying = isPlaying, modifier = Modifier.size(32.dp, 16.dp), barCount = 6)

                    Spacer(modifier = Modifier.width(12.dp))

                    IconButton(onClick = { viewModel.togglePlayPause() }) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "play-pause",
                            tint = Color.White
                        )
                    }

                    IconButton(onClick = { viewModel.skipToNext() }) {
                        Icon(imageVector = Icons.Default.SkipNext, contentDescription = "skip-next", tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }

                // Real playing progress bar running along the bottom edge
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .height(2.dp)
                        .background(Color.White.copy(0.1f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progressFraction)
                            .fillMaxHeight()
                            .background(CyberCyan)
                    )
                }
            }
    } else {
        // FULL SCREEN GLASSMORPHIC LUXURY EXPANDED PLAYER SCREEN
        Dialog(
            onDismissRequest = { isFullSize = false },
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("full_screen_player"),
                color = ObsidianBackground
            ) {
                RadialCosmicGlow(color = CyberPurple, modifier = Modifier.fillMaxSize())

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Header control dismiss
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { isFullSize = false }) {
                            Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "hide", tint = Color.White, modifier = Modifier.size(28.dp))
                        }

                        Text("SYNCHRONIZING AUDIO DECK", color = CyberCyan, fontSize = 9.sp, fontWeight = FontWeight.Black, letterSpacing = 1.5.sp)

                        IconButton(onClick = {
                            isFullSize = false
                            onNavigateToProfile(song!!.artistId)
                        }) {
                            Icon(imageVector = Icons.Default.Person, contentDescription = "artist", tint = Color.White)
                        }
                    }

                    // Cover Art huge glowing circle
                    val sColor = remember(song) { try { Color(android.graphics.Color.parseColor(song!!.coverColorStart)) } catch(e: Exception) { CyberPurple } }
                    val eColor = remember(song) { try { Color(android.graphics.Color.parseColor(song!!.coverColorEnd)) } catch(e: Exception) { CyberCyan } }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .padding(24.dp)
                            .clip(CircleShape)
                            .background(Brush.radialGradient(listOf(sColor, eColor)))
                            .border(width = 3.dp, brush = Brush.sweepGradient(listOf(CyberCyan, CyberPurple)), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        // Ambient visualizer pulsing ring
                        if (isPlaying) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .border(10.dp, Color.White.copy(0.04f), CircleShape)
                            )
                        }
                        Icon(imageVector = Icons.Default.MusicNote, contentDescription = "", tint = Color.Black.copy(0.5f), modifier = Modifier.size(80.dp))
                    }

                    // Title + description
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        GlowingText(
                            text = song!!.title,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black, color = Color.White),
                            glowColor = CyberCyan
                        )
                        
                        Text(
                            text = "ZM BEATS ARTIST: ${song!!.artistName}",
                            color = textThemeGradientColor(song!!.genre),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Text(
                            text = "Vessels category: ${song!!.genre}",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    // Lyrics Scrolling Sync mock
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x1F16161D))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val activeLyric = getMockActiveLyric(positionSeconds, durationSeconds)
                        AnimatedContent(
                            targetState = activeLyric,
                            transitionSpec = { fadeIn() togetherWith fadeOut() }
                        ) { lyricText ->
                            Text(
                                text = lyricText,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Real Animated Multi-bar sound visualizer
                    MusicVisualizer(
                        isPlaying = isPlaying,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp)
                            .padding(vertical = 12.dp),
                        glowColor = CyberCyan,
                        barCount = 28
                    )

                    // Position Slider
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Slider(
                            value = positionSeconds.toFloat(),
                            onValueChange = { viewModel.seekToPosition(it.toInt()) },
                            valueRange = 0f..totalDuration.toFloat(),
                            colors = SliderDefaults.colors(
                                activeTrackColor = CyberCyan,
                                inactiveTrackColor = Color.White.copy(0.1f),
                                thumbColor = GoldenAccent
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("seek_playback_slider")
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(formatTimeFormatted(positionSeconds), color = TextSecondary, fontSize = 11.sp)
                            Text(formatTimeFormatted(totalDuration), color = TextSecondary, fontSize = 11.sp)
                        }
                    }

                    // Audio controllers layout
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { viewModel.skipToPrevious() }) {
                            Icon(imageVector = Icons.Default.SkipPrevious, contentDescription = "previous", tint = Color.White, modifier = Modifier.size(36.dp))
                        }

                        // Giant core play trigger
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .clickable { viewModel.togglePlayPause() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Play/pause core",
                                tint = Color.Black,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        IconButton(onClick = { viewModel.skipToNext() }) {
                            Icon(imageVector = Icons.Default.SkipNext, contentDescription = "next", tint = Color.White, modifier = Modifier.size(36.dp))
                        }
                    }

                    // --- ACOUSTIC TRACK EDITING LAB ---
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("acoustic_lab_deck")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            // Header + Reset button
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Tune,
                                        contentDescription = "Acoustic Tuning Desk",
                                        tint = GoldenAccent,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "ACOUSTIC EDITING LAB",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.sp
                                    )
                                }

                                TextButton(
                                    onClick = { viewModel.resetAudioEffects() },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Reset effects",
                                        tint = CyberCyan,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "RESET ALL",
                                        color = CyberCyan,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // 1. AUTO-TUNE SLIDER
                            val autotunePercent = (autotuneStrength * 100).toInt()
                            val autotuneLabel = when {
                                autotuneStrength == 0f -> "Off (Bypassed)"
                                autotuneStrength < 0.35f -> "Mild Correction ($autotunePercent%)"
                                autotuneStrength < 0.70f -> "Studio Hardtune ($autotunePercent%)"
                                autotuneStrength < 0.95f -> "Vocal Tuning ($autotunePercent%)"
                                else -> "Extreme Hardtune ($autotunePercent%)"
                            }
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.KeyboardVoice, contentDescription = "", tint = TextSecondary, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("AUTO-TUNE FX", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Text(autotuneLabel, color = GoldenAccent, fontSize = 11.sp, fontWeight = FontWeight.Black)
                                }
                                Slider(
                                    value = autotuneStrength,
                                    onValueChange = { viewModel.setAutotuneStrength(it) },
                                    valueRange = 0f..1f,
                                    colors = SliderDefaults.colors(
                                        activeTrackColor = GoldenAccent,
                                        inactiveTrackColor = Color.White.copy(0.1f),
                                        thumbColor = GoldenAccent
                                    ),
                                    modifier = Modifier.fillMaxWidth().testTag("autotune_slider")
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // 2. PLAYBACK SPEED SLIDER
                            val speedPercent = (playbackSpeed * 100).toInt()
                            val speedLabel = when {
                                playbackSpeed < 0.75f -> String.format("%.2fx (Slowed & Reverbed)", playbackSpeed)
                                playbackSpeed < 0.95f -> String.format("%.2fx (Chilled / Deep)", playbackSpeed)
                                playbackSpeed in 0.95f..1.05f -> "1.00x (Normal Speed)"
                                playbackSpeed <= 1.4f -> String.format("%.2fx (Nightcore Edit)", playbackSpeed)
                                else -> String.format("%.2fx (Extreme Turbo)", playbackSpeed)
                            }
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.Speed, contentDescription = "", tint = TextSecondary, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("SPEED / TEMPO", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Text(speedLabel, color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.Black)
                                }
                                Slider(
                                    value = playbackSpeed,
                                    onValueChange = { viewModel.setPlaybackSpeed(it) },
                                    valueRange = 0.5f..2.0f,
                                    colors = SliderDefaults.colors(
                                        activeTrackColor = CyberCyan,
                                        inactiveTrackColor = Color.White.copy(0.1f),
                                        thumbColor = CyberCyan
                                    ),
                                    modifier = Modifier.fillMaxWidth().testTag("speed_slider")
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // 3. BASS BOOST SLIDER
                            val bassPercent = (bassBoost * 100).toInt()
                            val bassLabel = when {
                                bassBoost == 0f -> "Flat EQ"
                                bassBoost < 0.35f -> "Warm Warmth ($bassPercent%)"
                                bassBoost < 0.70f -> "Heavy Sub ($bassPercent%)"
                                bassBoost < 0.95f -> "Skull Roller ($bassPercent%)"
                                else -> "Bass Overlimit ($bassPercent%)"
                            }
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.GraphicEq, contentDescription = "", tint = TextSecondary, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("BASS RE-DEEPENER", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Text(bassLabel, color = GlowPink, fontSize = 11.sp, fontWeight = FontWeight.Black)
                                }
                                Slider(
                                    value = bassBoost,
                                    onValueChange = { viewModel.setBassBoost(it) },
                                    valueRange = 0f..1f,
                                    colors = SliderDefaults.colors(
                                        activeTrackColor = GlowPink,
                                        inactiveTrackColor = Color.White.copy(0.1f),
                                        thumbColor = GlowPink
                                    ),
                                    modifier = Modifier.fillMaxWidth().testTag("bass_slider")
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

// ==========================================
// MOCK UTILITIES & LOGGING FORMATTERS
// ==========================================
private fun Offset.ptY(h: Float): Float {
    return h - this.y
}

private fun formatStatCount(count: Int): String {
    return when {
        count >= 1000000 -> String.format("%.1fM", count / 1000000f)
        count >= 1000 -> String.format("%.1fK", count / 1000f)
        else -> count.toString()
    }
}

private fun formatTimeFormatted(sec: Int): String {
    val m = sec / 60
    val s = sec % 60
    return String.format("%d:%02d", m, s)
}

private fun formatTimeAgo(timestamp: Long): String {
    val deltaSec = (System.currentTimeMillis() - timestamp) / 1000
    return when {
         deltaSec < 60 -> "just now"
         deltaSec < 3600 -> "${deltaSec / 60}m ago"
         deltaSec < 86400 -> "${deltaSec / 3600}h ago"
         else -> "${deltaSec / 86400}d ago"
    }
}

private fun textThemeGradientColor(genre: String): Color {
    return when(genre) {
        "Phonk" -> CyberCyan
        "Synthwave" -> GlowPink
        "Dreampop" -> GoldenAccent
        "Lofi Hip Hop" -> Color(0xFFB0FF76)
        "Emo Rap" -> Color(0xFFFF7676)
        else -> CyberPurple
    }
}

private fun getMockActiveLyric(positionSec: Int, durationSec: Int): String {
    val progressPercent = positionSec.toFloat() / durationSec
    return when {
        progressPercent < 0.15f -> "🌌 [Sound Signal Locked] Preparing cosmic soundwaves..."
        progressPercent < 0.35f -> "⚡ I hear the static inside my holographic display..."
        progressPercent < 0.55f -> "🔥 Underground frequencies jumping through the neon lights..."
        progressPercent < 0.75f -> "🧬 Independent artists overriding the main corporate broadcast!"
        progressPercent < 0.90f -> "💎 ZM BEATS nodes connecting together in absolute digital harmony..."
        else -> "💿 [End transmission] Broadcast complete."
    }
}

// ==========================================
// STATUS & REELS DIALOG SERVICES
// ==========================================

@Composable
fun StatusViewerDialog(
    status: Status,
    onDismiss: () -> Unit
) {
    val sColor = remember(status) { try { Color(android.graphics.Color.parseColor(status.bgGradientStart)) } catch (e: Exception) { CyberPurple } }
    val eColor = remember(status) { try { Color(android.graphics.Color.parseColor(status.bgGradientEnd)) } catch (e: Exception) { CyberCyan } }
    val uriHandler = androidx.compose.ui.platform.LocalUriHandler.current

    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .clip(RoundedCornerShape(24.dp))
                .background(Brush.linearGradient(listOf(sColor, sColor, eColor)))
                .border(2.dp, Color.White.copy(0.15f), RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Overlaid Image layer if imageUri exists
            if (!status.imageUri.isNullOrEmpty()) {
                coil.compose.AsyncImage(
                    model = status.imageUri,
                    contentDescription = "Status image background",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
                // Black semi-transparent overlay to ensure text readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(0.55f))
                )
            }

            var showShareSheet by remember { mutableStateOf(false) }

            // Share button top left
            IconButton(
                onClick = { showShareSheet = true },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
                    .background(Color.Black.copy(0.4f), CircleShape)
                    .testTag("status_viewer_share_trigger")
            ) {
                Icon(imageVector = Icons.Default.Share, contentDescription = "Share Status", tint = Color.White)
            }

            // Dismiss button top right
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .background(Color.Black.copy(0.4f), CircleShape)
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }

            if (showShareSheet) {
                com.example.ui.components.MultiShareDialog(
                    title = if (status.text.isNotEmpty()) status.text else "WAVE INTEL AT ${status.artistName}",
                    type = if (!status.videoUri.isNullOrEmpty()) "Wave Active Status Video" else "Wave Active Status",
                    shareUrl = "https://zmbeats.com/status/${status.id}",
                    onDismiss = { showShareSheet = false }
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize().padding(24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = status.artistName.uppercase(),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "@${status.artistId}",
                        color = Color.White.copy(0.6f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                // If video exists, show a beautiful video preview card
                if (!status.videoUri.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(110.dp)
                            .background(Color.Black.copy(0.4f), RoundedCornerShape(12.dp))
                            .border(1.dp, CyberCyan.copy(0.5f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Video playing",
                                tint = CyberCyan,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("STREAM ACTIVE STATUS VIDEO", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    // Regular text content
                    Text(
                        text = "\"${status.text}\"",
                        color = Color.White,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }

                // If link exists, show clickable link button!
                if (!status.linkUrl.isNullOrEmpty()) {
                    Button(
                        onClick = {
                            try {
                                uriHandler.openUri(status.linkUrl)
                            } catch(e: Exception) {
                                // No action
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(0.2f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = "Link", tint = Color.White, modifier = Modifier.size(14.dp))
                            Text("OPEN ATTACHED LINK 🔗", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }

                Text(
                    text = formatTimeAgo(status.timestamp).uppercase(),
                    color = Color.White.copy(0.5f),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun StatusCreatorDialog(
    viewModel: MusicViewModel,
    onDismiss: () -> Unit
) {
    var textInput by remember { mutableStateOf("") }
    var selectedVideoUri by remember { mutableStateOf<String?>(null) }
    var selectedImageUri by remember { mutableStateOf<String?>(null) }
    var statusLinkUrl by remember { mutableStateOf("") }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: android.net.Uri? ->
        if (uri != null) {
            selectedImageUri = uri.toString()
            selectedVideoUri = null // clear video if image selected
        }
    }

    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: android.net.Uri? ->
        if (uri != null) {
            selectedVideoUri = uri.toString()
            selectedImageUri = null // clear image if video selected
        }
    }
    
    val gradients = listOf(
        Pair("#00FFE0", "#0051FF"), // Coral Ocean
        Pair("#FF5E62", "#FF9966"), // Sweet Sunset
        Pair("#00FF90", "#00FFE0"), // Cool Mint
        Pair("#FF0099", "#493240")  // Deep Violet
    )
    var selectedGradientIndex by remember { mutableStateOf(0) }

    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss
    ) {
        val sColor = Color(android.graphics.Color.parseColor(gradients[selectedGradientIndex].first))
        val eColor = Color(android.graphics.Color.parseColor(gradients[selectedGradientIndex].second))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .clip(RoundedCornerShape(24.dp))
                .background(Brush.linearGradient(listOf(sColor, eColor)))
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "BROADCAST WAVE STATUS",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp).background(Color.Black.copy(0.2f), CircleShape)
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(14.dp))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Text Input
                TextField(
                    value = textInput,
                    onValueChange = { if (it.length <= 80) textInput = it },
                    placeholder = { Text("What is vibrating on your channel?... (Max 80 chars)", color = Color.White.copy(0.6f), fontSize = 14.sp, fontStyle = FontStyle.Italic) },
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic, textAlign = TextAlign.Center),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("status_text_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Media Attachment Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(0.25f), RoundedCornerShape(16.dp))
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("RICH GRAPHICS & MEDIA", color = Color.White.copy(0.8f), fontSize = 9.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (selectedImageUri != null) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, Color.White.copy(0.3f), RoundedCornerShape(8.dp))
                        ) {
                            coil.compose.AsyncImage(
                                model = selectedImageUri,
                                contentDescription = "Selected media status image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            IconButton(
                                onClick = { selectedImageUri = null },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(22.dp)
                                    .background(Color.Black.copy(0.6f), CircleShape)
                            ) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Remove", tint = Color.White, modifier = Modifier.size(10.dp))
                            }
                        }
                    } else if (selectedVideoUri != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .background(Color.Black.copy(0.4f), RoundedCornerShape(8.dp))
                                .border(1.dp, Color.White.copy(0.2f), RoundedCornerShape(8.dp))
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Video", tint = CyberCyan, modifier = Modifier.size(20.dp))
                                Text("Video Status Attached 🎥", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.weight(1f))
                                IconButton(
                                    onClick = { selectedVideoUri = null },
                                    modifier = Modifier
                                        .size(22.dp)
                                        .background(Color.Black.copy(0.6f), CircleShape)
                                ) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = "Remove", tint = Color.White, modifier = Modifier.size(10.dp))
                                }
                            }
                        }
                    } else {
                        Text("No media attachment selected", color = Color.White.copy(0.5f), fontSize = 10.sp, fontStyle = FontStyle.Italic)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Button(
                            onClick = { imagePickerLauncher.launch("image/*") },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(0.12f)),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp)
                        ) {
                            Text("ADD IMAGE 🖼️", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { videoPickerLauncher.launch("video/*") },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(0.12f)),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp)
                        ) {
                            Text("ADD VIDEO 🎥", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // External Link attachment
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(0.25f), RoundedCornerShape(16.dp))
                        .padding(12.dp)
                ) {
                    Text("ATTACH HYPERLINK 🔗", color = Color.White.copy(0.8f), fontSize = 9.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
                    Spacer(modifier = Modifier.height(6.dp))
                    TextField(
                        value = statusLinkUrl,
                        onValueChange = { statusLinkUrl = it },
                        placeholder = { Text("https://collab-channel.com/project", color = Color.White.copy(0.4f), fontSize = 11.sp) },
                        textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 11.sp),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Black.copy(0.2f),
                            unfocusedContainerColor = Color.Black.copy(0.2f),
                            focusedIndicatorColor = Color.White.copy(0.3f),
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Gradients & publish
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("TAP RADIAL ACCENTS", color = Color.White.copy(0.7f), fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        gradients.forEachIndexed { idx, p ->
                            val sG = Color(android.graphics.Color.parseColor(p.first))
                            val eG = Color(android.graphics.Color.parseColor(p.second))
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Brush.linearGradient(listOf(sG, eG)))
                                    .border(
                                        width = if (selectedGradientIndex == idx) 2.dp else 0.dp,
                                        color = Color.White,
                                        shape = CircleShape
                                    )
                                    .clickable { selectedGradientIndex = idx }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    GlowingButton(
                        text = "BROADCAST STATUS NOW",
                        onClick = {
                            if (textInput.isNotEmpty()) {
                                viewModel.uploadStatus(
                                    text = textInput,
                                    bgStart = gradients[selectedGradientIndex].first,
                                    bgEnd = gradients[selectedGradientIndex].second,
                                    videoUri = selectedVideoUri,
                                    imageUri = selectedImageUri,
                                    linkUrl = statusLinkUrl.ifEmpty { null }
                                )
                                onDismiss()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().testTag("status_submit_btn")
                    )
                }
            }
        }
    }
}

@Composable
fun ReelsPlayerDialog(
    reels: List<Reel>,
    initialIndex: Int,
    viewModel: MusicViewModel,
    onDismiss: () -> Unit
) {
    var currentIndex by remember { mutableStateOf(initialIndex.coerceIn(0, maxOf(0, reels.size - 1))) }
    val reel = reels.getOrNull(currentIndex) ?: return

    val sColor = remember(reel) { try { Color(android.graphics.Color.parseColor(reel.coverColorStart)) } catch (e: Exception) { CyberPurple } }
    val eColor = remember(reel) { try { Color(android.graphics.Color.parseColor(reel.coverColorEnd)) } catch (e: Exception) { CyberCyan } }

    var localLikes by remember(reel) { mutableStateOf(reel.likes) }
    var isLikedLocal by remember(reel) { mutableStateOf(false) }
    var showShareSheet by remember(reel) { mutableStateOf(false) }

    androidx.compose.ui.window.Dialog(
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = ObsidianBackground
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(listOf(sColor.copy(0.3f), ObsidianBackground)))
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onDismiss) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                        
                        Text(
                            text = "AURA REELS MODE",
                            color = GoldenAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.5.sp
                        )

                        IconButton(onClick = { /* info */ }) {
                            Icon(imageVector = Icons.Default.Info, contentDescription = "Info", tint = Color.White.copy(0.5f))
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(240.dp)
                            .clip(CircleShape)
                            .background(Brush.radialGradient(listOf(sColor.copy(0.4f), Color.Transparent)))
                            .border(width = 2.dp, brush = Brush.sweepGradient(listOf(CyberCyan, CyberPurple, GlowPink)), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.MusicNote,
                                contentDescription = "",
                                tint = Color.White,
                                modifier = Modifier.size(60.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "SOUND ACTIVE",
                                color = CyberCyan,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(bottom = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    AvatarWithGradient(
                                        displayName = reel.artistName,
                                        startColor = reel.coverColorStart,
                                        endColor = reel.coverColorEnd,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = reel.artistName,
                                            color = Color.White,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Black,
                                            fontStyle = FontStyle.Italic
                                        )
                                        Text(
                                            text = "@${reel.artistId}",
                                            color = TextSecondary,
                                            fontSize = 10.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = reel.title,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = 20.sp,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.White.copy(0.08f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.MusicNote, contentDescription = "", tint = GoldenAccent, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = reel.soundName,
                                        color = GoldenAccent,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.padding(start = 16.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(
                                        onClick = {
                                            if (!isLikedLocal) {
                                                isLikedLocal = true
                                                localLikes += 1
                                                viewModel.likeReel(reel.id)
                                            }
                                        },
                                        modifier = Modifier
                                            .background(if (isLikedLocal) GlowPink.copy(0.2f) else Color.White.copy(0.08f), CircleShape)
                                            .border(1.dp, if (isLikedLocal) GlowPink else Color.Transparent, CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Favorite,
                                            contentDescription = "Like",
                                            tint = if (isLikedLocal) GlowPink else Color.White
                                        )
                                    }
                                    Text(localLikes.toString(), color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(
                                        onClick = { showShareSheet = true },
                                        modifier = Modifier.background(Color.White.copy(0.08f), CircleShape).testTag("reel_share_trigger")
                                    ) {
                                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = Color.White)
                                    }
                                    Text("Share", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }

                                if (showShareSheet) {
                                    com.example.ui.components.MultiShareDialog(
                                        title = reel.title.ifEmpty { "WAVE REEL AT ${reel.artistName}" },
                                        type = "Aura Reel Video",
                                        shareUrl = "https://zmbeats.com/reels/${reel.id}",
                                        onDismiss = { showShareSheet = false }
                                    )
                                }

                                if (currentIndex < reels.size - 1) {
                                    IconButton(
                                        onClick = { currentIndex += 1 },
                                        modifier = Modifier
                                            .background(CyberCyan.copy(0.2f), CircleShape)
                                            .border(1.dp, CyberCyan, CircleShape)
                                    ) {
                                        Icon(imageVector = Icons.Default.ArrowDownward, contentDescription = "Next Reel", tint = CyberCyan)
                                    }
                                } else {
                                    IconButton(
                                        onClick = { currentIndex = 0 },
                                        modifier = Modifier
                                            .background(CyberPurple.copy(0.2f), CircleShape)
                                            .border(1.dp, CyberPurple, CircleShape)
                                    ) {
                                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Rewind", tint = CyberPurple)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
