package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlin.math.sin
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.painter.ColorPainter

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    borderColor: Color = LuxuryDarkCardBorder,
    borderWidth: Dp = 1.dp,
    glowColor: Color? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0x14FFFFFF), // Translucent bg-white/5 equivalent
                        Color(0x05FFFFFF)  // Fades down beautifully
                    )
                )
            )
            .border(
                width = borderWidth,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        borderColor.copy(alpha = 0.5f),
                        borderColor.copy(alpha = 0.12f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .drawBehind {
                if (glowColor != null) {
                    drawCircle(
                        color = glowColor.copy(alpha = 0.08f),
                        radius = size.width / 1.5f,
                        center = Offset(size.width / 2f, size.height / 2f)
                    )
                }
            }
            .padding(16.dp)
    ) {
        Column {
            content()
        }
    }
}

@Composable
fun GlowingButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    glowColor: Color = CyberPurple,
    containerColor: Color = ObsidianBackground,
    enabled: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ButtonGlow")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "AlphaGlow"
    )

    Box(
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(containerColor)
            .border(
                width = 2.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        glowColor,
                        CyberCyan.copy(alpha = alphaAnim),
                        glowColor
                    )
                ),
                shape = RoundedCornerShape(26.dp)
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        // Glowing Background Drop
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(8.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(glowColor.copy(alpha = 0.2f), Color.Transparent),
                        radius = 120f
                    )
                )
        )

        Text(
            text = text,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                shadow = Shadow(
                    color = glowColor,
                    offset = Offset(0f, 0f),
                    blurRadius = 12f
                )
            )
        )
    }
}

@Composable
fun GlowingText(
    text: String,
    style: TextStyle,
    glowColor: Color = GoldenAccent,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE
) {
    Text(
        text = text,
        style = style.copy(
            shadow = Shadow(
                color = glowColor.copy(alpha = 0.8f),
                offset = Offset(0f, 0f),
                blurRadius = 16f
            )
        ),
        modifier = modifier,
        maxLines = maxLines
    )
}

@Composable
fun VerificationBadge(
    modifier: Modifier = Modifier,
    size: Dp = 16.dp,
    tint: Color = GoldenAccent
) {
    Icon(
        imageVector = Icons.Default.CheckCircle,
        contentDescription = "Verified Creator Account",
        tint = tint,
        modifier = modifier.size(size)
    )
}

@Composable
fun AvatarWithGradient(
    displayName: String,
    startColor: String,
    endColor: String,
    modifier: Modifier = Modifier,
    isVerified: Boolean = false,
    avatarUri: String? = null
) {
    val initials = if (displayName.isNotEmpty()) displayName.take(2).uppercase() else "IN"

    Box(
        modifier = modifier
            .clip(CircleShape)
            .border(1.5.dp, Color.White.copy(alpha = 0.2f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (!avatarUri.isNullOrEmpty()) {
            AsyncImage(
                model = avatarUri,
                contentDescription = displayName,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                error = ColorPainter(Color.Gray.copy(alpha = 0.3f))
            )
        } else {
            val startHex = remember(startColor) { try { Color(android.graphics.Color.parseColor(startColor)) } catch(e: Exception) { CyberPurple } }
            val endHex = remember(endColor) { try { Color(android.graphics.Color.parseColor(endColor)) } catch(e: Exception) { CyberCyan } }
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(startHex, endHex)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    fontSize = (modifier.toString().hashCode() % 6 + 14).sp, // dynamic proportion based on layout
                    fontWeight = FontWeight.Black,
                    color = Color.Black
                )
            }
        }

        if (isVerified) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                VerificationBadge(size = 13.dp)
            }
        }
    }
}

@Composable
fun MusicVisualizer(
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    glowColor: Color = CyberCyan,
    barCount: Int = 18
) {
    val transition = rememberInfiniteTransition(label = "VisualizerTransition")
    
    // Create an animated offset for each bar
    val animations = (0 until barCount).map { index ->
        if (isPlaying) {
            transition.animateFloat(
                initialValue = 0.1f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = (200..600).random(),
                        easing = FastOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "Bar_$index"
            )
        } else {
            remember { mutableStateOf(0.12f) }
        }
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val spacing = 4.dp.toPx()
        val totalSpacing = spacing * (barCount - 1)
        val barWidth = (width - totalSpacing) / barCount

        for (i in 0 until barCount) {
            val progress = animations[i].value
            val barHeight = progress * height
            val x = i * (barWidth + spacing)
            val y = height - barHeight

            // Draw rounded bars
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        glowColor,
                        CyberPurple.copy(alpha = 0.7f)
                    )
                ),
                topLeft = Offset(x, y),
                size = androidx.compose.ui.geometry.Size(barWidth, barHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(barWidth / 2, barWidth / 2)
            )
        }
    }
}

@Composable
fun RadialCosmicGlow(
    color: Color = CyberPurple,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Top-right Fuchsia background glow (from design HTML)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .fillMaxWidth(0.85f)
                .fillMaxHeight(0.6f)
                .blur(100.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            CyberPurple.copy(alpha = 0.12f),
                            Color.Transparent
                        )
                    )
                )
        )

        // Bottom-left Indigo background glow (from design HTML)
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.85f)
                .fillMaxHeight(0.6f)
                .blur(100.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            CyberCyan.copy(alpha = 0.12f),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}

@Composable
fun MultiShareDialog(
    title: String,
    type: String, // e.g. "Active Status Video", "Aura Reel", "Vibrating Track"
    shareUrl: String,
    onDismiss: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
    val uriHandler = androidx.compose.ui.platform.LocalUriHandler.current

    val shareMessage = "🎵 ZM BEATS BROADCAST 🎵\n\nCheck out this amazing $type on ZM BEATS:\n\"$title\"\n\nLink: $shareUrl"

    val platforms = listOf(
        SharePlatformItem("WhatsApp", "com.whatsapp", Color(0xFF25D366), "💬", "whatsapp://send?text="),
        SharePlatformItem("Facebook", "com.facebook.katana", Color(0xFF1877F2), "👥", "https://www.facebook.com/sharer/sharer.php?u="),
        SharePlatformItem("Instagram", "com.instagram.android", Color(0xFFE4405F), "📸", "https://www.instagram.com/"),
        SharePlatformItem("TikTok", "com.zhiliaoapp.musically", Color(0xFF010101), "🎵", "https://www.tiktok.com/"),
        SharePlatformItem("YouTube", "com.google.android.youtube", Color(0xFFFF0000), "🎥", "https://www.youtube.com/")
    )

    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xE60B0B0F))
                .border(2.dp, LuxuryDarkCardBorder, RoundedCornerShape(24.dp))
                .padding(20.dp)
                .testTag("multi_share_dialog_sheet")
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "MULTI-PLATFORM DISPATCH",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Propagate $type frequency",
                            color = TextSecondary,
                            fontSize = 9.sp
                        )
                    }

                    androidx.compose.material3.IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color.White.copy(0.08f), CircleShape)
                    ) {
                        androidx.compose.material3.Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Close,
                            contentDescription = "Dismiss dispatch",
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }

                // Shared preview box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(0.04f))
                        .border(1.dp, Color.White.copy(0.08f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = type.uppercase(),
                                color = CyberCyan,
                                fontWeight = FontWeight.Black,
                                fontSize = 8.sp,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "• SHARED ITEM PREVIEW",
                                color = Color.White.copy(alpha = 0.4f),
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "\"$title\"",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                        Text(
                            text = shareUrl,
                            color = TextSecondary,
                            fontSize = 10.sp,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                }

                // Grid of platforms
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val rows = platforms.chunked(3)
                    rows.forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowItems.forEach { platform ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(68.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color.White.copy(0.05f))
                                        .border(
                                            width = 1.dp,
                                            color = platform.brandColor.copy(alpha = 0.4f),
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .clickable {
                                            // 1. Copy payload to clipboard
                                            clipboardManager.setText(
                                                androidx.compose.ui.text.AnnotatedString(shareMessage)
                                            )
                                            android.widget.Toast
                                                .makeText(
                                                    context,
                                                    "Dispatch Payload copied! Redirecting to ${platform.name}...",
                                                    android.widget.Toast.LENGTH_SHORT
                                                )
                                                .show()

                                            // 2. Intent trigger target
                                            try {
                                                val platformIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                                    putExtra(android.content.Intent.EXTRA_TEXT, shareMessage)
                                                    setType("text/plain")
                                                    setPackage(platform.packageName)
                                                }
                                                context.startActivity(platformIntent)
                                            } catch (e: Exception) {
                                                // If platform package isn't installed, trigger targeted web view or open custom URI standard fallback
                                                try {
                                                    val urlToLaunch = if (platform.customUriScheme.startsWith("http")) {
                                                        platform.customUriScheme + java.net.URLEncoder.encode(shareMessage, "UTF-8")
                                                    } else {
                                                        platform.customUriScheme + String(android.util.Base64.encode(shareMessage.toByteArray(), android.util.Base64.DEFAULT))
                                                    }
                                                    uriHandler.openUri(urlToLaunch)
                                                } catch (ex: Exception) {
                                                    // Ultimate fallback: open standard browser to the platform
                                                    try {
                                                        uriHandler.openUri("https://www.google.com/search?q=${platform.name}+sharing")
                                                    } catch (ey: Exception) {
                                                        // Fallback completed
                                                    }
                                                }
                                            }
                                        }
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = platform.emojiLabel,
                                            fontSize = 20.sp
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = platform.name.uppercase(),
                                            color = Color.White,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 9.sp
                                        )
                                    }
                                }
                            }

                            // Pad the last row if it's not complete to maintain aligned widths
                            if (rowItems.size < 3) {
                                repeat(3 - rowItems.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }

                androidx.compose.material3.HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.White.copy(0.08f)
                )

                // Native Android System Share Button ("And all of that")
                androidx.compose.material3.Button(
                    onClick = {
                        try {
                            val systemShareIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                putExtra(android.content.Intent.EXTRA_TEXT, shareMessage)
                                setType("text/plain")
                            }
                            val chooserIntent = android.content.Intent.createChooser(systemShareIntent, "SHARE AUDIO NODE")
                            context.startActivity(chooserIntent)
                        } catch (e: Exception) {
                            android.widget.Toast.makeText(context, "System share failed", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = CyberCyan.copy(alpha = 0.15f)
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("system_share_btn"),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        androidx.compose.material3.Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Share,
                            contentDescription = "System broadcast share",
                            tint = CyberCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "BROADCAST TO OTHER CONDUITS (ALL APPS)",
                            color = CyberCyan,
                            fontWeight = FontWeight.Black,
                            fontSize = 10.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }
    }
}

data class SharePlatformItem(
    val name: String,
    val packageName: String,
    val brandColor: Color,
    val emojiLabel: String,
    val customUriScheme: String
)
