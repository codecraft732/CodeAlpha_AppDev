package com.example.flashcardquiz.ui.screens.quiz

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.flashcardquiz.ui.components.FlashcardItem
import com.example.flashcardquiz.ui.theme.PrimaryPurple
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    viewModel: QuizViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showAddDialog) {
        AddCardDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { q, a ->
                viewModel.onEvent(QuizEvent.AddCard(q, a))
                showAddDialog = false
            }
        )
    }

    if (showEditDialog) {
        uiState.currentCard?.let { card ->
            EditCardDialog(
                card = card,
                onDismiss = { showEditDialog = false },
                onConfirm = { updatedCard ->
                    viewModel.onEvent(QuizEvent.UpdateCard(updatedCard))
                    showEditDialog = false
                }
            )
        }
    }

    if (showDeleteDialog) {
        uiState.currentCard?.let { card ->
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Card") },
                text = { Text("Are you sure you want to delete this card?") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.onEvent(QuizEvent.DeleteCard(card))
                        showDeleteDialog = false
                    }) {
                        Text("Delete", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                ManageDeckDrawerContent(
                    onAddClick = {
                        showAddDialog = true
                        scope.launch { drawerState.close() }
                    },
                    onEditClick = {
                        showEditDialog = true
                        scope.launch { drawerState.close() }
                    },
                    onDeleteClick = {
                        showDeleteDialog = true
                        scope.launch { drawerState.close() }
                    },
                    onClose = { scope.launch { drawerState.close() } }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Flash Cards",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryPurple
                            )
                        )
                    },
                    actions = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = PrimaryPurple)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            bottomBar = {
                QuizBottomBar(
                    onPrevious = { viewModel.onEvent(QuizEvent.PreviousCard) },
                    onNext = { viewModel.onEvent(QuizEvent.NextCard) }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    uiState.currentCard?.let { card ->
                        FlashcardItem(
                            flashcard = card,
                            isAnswerVisible = uiState.isAnswerVisible,
                            progress = uiState.progress,
                            onFlip = { viewModel.onEvent(QuizEvent.ToggleAnswer) },
                            onDelete = { showDeleteDialog = true }
                        )
                    } ?: run {
                        if (!uiState.isLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(0.8f),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("No cards available.", color = Color.Gray)
                                    Button(onClick = { showAddDialog = true }) {
                                        Text("Add New Card")
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { viewModel.onEvent(QuizEvent.ToggleAnswer) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (uiState.isAnswerVisible) Icons.Default.History else Icons.Default.Visibility,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                if (uiState.isAnswerVisible) "Hide Answer" else "Show Answer",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        InfoItem(
                            icon = Icons.Default.Layers,
                            text = uiState.currentPositionText
                        )
                        InfoItem(
                            icon = Icons.Default.AccessTime,
                            text = uiState.timeLeft
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoItem(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.Gray)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
    }
}

@Composable
fun QuizBottomBar(onPrevious: () -> Unit, onNext: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onPrevious,
                modifier = Modifier.weight(1f)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.KeyboardArrowLeft, contentDescription = null)
                    Text("Previous", style = MaterialTheme.typography.labelMedium)
                }
            }
            
            VerticalDivider(modifier = Modifier.height(40.dp).width(1.dp), color = Color.LightGray)

            TextButton(
                onClick = onNext,
                modifier = Modifier.weight(1f)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
                    Text("Next", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

@Composable
fun ManageDeckDrawerContent(
    onAddClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Manage Deck",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryPurple
                )
            )
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        DrawerItem(
            icon = Icons.Default.AddCircleOutline, 
            text = "Add Card",
            onClick = onAddClick
        )
        DrawerItem(
            icon = Icons.Default.Edit, 
            text = "Edit Current Card",
            onClick = onEditClick
        )
        DrawerItem(
            icon = Icons.Default.DeleteOutline, 
            text = "Delete Current Card",
            onClick = onDeleteClick
        )
    }
}

@Composable
fun DrawerItem(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp), tint = Color.DarkGray)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun AddCardDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var question by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Flashcard") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = question,
                    onValueChange = { question = it },
                    label = { Text("Question") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = answer,
                    onValueChange = { answer = it },
                    label = { Text("Answer") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (question.isNotBlank() && answer.isNotBlank()) onConfirm(question, answer) },
                enabled = question.isNotBlank() && answer.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun EditCardDialog(
    card: com.example.flashcardquiz.data.model.FlashcardData,
    onDismiss: () -> Unit,
    onConfirm: (com.example.flashcardquiz.data.model.FlashcardData) -> Unit
) {
    var question by remember { mutableStateOf(card.question) }
    var answer by remember { mutableStateOf(card.answer) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Flashcard") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = question,
                    onValueChange = { question = it },
                    label = { Text("Question") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = answer,
                    onValueChange = { answer = it },
                    label = { Text("Answer") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { 
                    if (question.isNotBlank() && answer.isNotBlank()) {
                        onConfirm(card.copy(question = question, answer = answer))
                    }
                },
                enabled = question.isNotBlank() && answer.isNotBlank()
            ) {
                Text("Update")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

//@SuppressLint("ViewModelConstructorInComposable")
//@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
//@Composable
//fun QuizScreenPreview() {
////    val repository = com.example.flashcardquiz.data.repository.FakeFlashcardRepository()
//    val viewModel = QuizViewModel(repository)
//    com.example.flashcardquiz.ui.theme.FlashcardQuizTheme {
//        QuizScreen(viewModel = viewModel)
//    }
//}
