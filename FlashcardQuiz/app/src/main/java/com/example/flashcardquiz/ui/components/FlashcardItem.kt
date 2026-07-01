package com.example.flashcardquiz.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashcardquiz.data.model.FlashcardData
import com.example.flashcardquiz.ui.theme.AnswerGreen
import com.example.flashcardquiz.ui.theme.PrimaryPurple

@Composable
fun FlashcardItem(
    flashcard: FlashcardData,
    isAnswerVisible: Boolean,
    progress: Float,
    onFlip: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isAnswerVisible) AnswerGreen else PrimaryPurple,
        animationSpec = tween(durationMillis = 500),
        label = "backgroundColor"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.8f)
            .padding(horizontal = 4.dp)
    ) {
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(60.dp),
            color = PrimaryPurple.copy(alpha = 0.8f),
            strokeWidth = 6.dp,
            trackColor = Color.LightGray.copy(alpha = 0.3f)
        )

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, end = 10.dp)
                .clickable { onFlip() },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.align(Alignment.TopStart).padding(8.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.White.copy(alpha = 0.6f)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isAnswerVisible) "ANSWER" else "QUESTION",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White.copy(alpha = 0.7f),
                        letterSpacing = 2.sp
                    )

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isAnswerVisible) flashcard.answer else flashcard.question,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                lineHeight = 32.sp
                            ),
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }

                    if (!isAnswerVisible) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.TouchApp,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "Tap to Flip",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FeedbackIcon(icon = Icons.Default.SentimentDissatisfied, onClick = {})
                            Spacer(modifier = Modifier.width(16.dp))
                            FeedbackIcon(icon = Icons.Default.SentimentNeutral, onClick = {})
                            Spacer(modifier = Modifier.width(16.dp))
                            FeedbackIcon(icon = Icons.Default.SentimentVerySatisfied, onClick = {})
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FeedbackIcon(icon: ImageVector, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .size(48.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = Color.White.copy(alpha = 0.2f)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
