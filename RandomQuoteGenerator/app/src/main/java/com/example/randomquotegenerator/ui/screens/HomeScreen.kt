package com.example.randomquotegenerator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.randomquotegenerator.data.QuoteRepository
import com.example.randomquotegenerator.ui.theme.GlassWhite
import com.example.randomquotegenerator.ui.theme.OnSurfaceVariant


@Composable
fun HomeScreen() {
    var currentQuote by remember { mutableStateOf(QuoteRepository.getRandomQuote()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                        Color.Transparent
                    ),
                    radius = 1000f
                )
            )
            .padding(32.dp) // stack-lg / generous margin
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "“",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = currentQuote.text,
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp)) // stack-sm

            Text(
                text = "— ${currentQuote.author}",
                style = MaterialTheme.typography.titleMedium,
                color = OnSurfaceVariant
            )

            Spacer(modifier = Modifier.height(48.dp)) // stack-lg

            Surface(
                onClick = { currentQuote = QuoteRepository.getRandomQuote() },
                shape = RoundedCornerShape(8.dp),
                color = GlassWhite,
                border = ButtonDefaults.outlinedButtonBorder,
                modifier = Modifier.height(48.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "NEW QUOTE",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeScreenPrev(){
    HomeScreen()
}