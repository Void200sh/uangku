package com.zyvo.uangku.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zyvo.uangku.data.Goal
import com.zyvo.uangku.data.PhotoStore
import com.zyvo.uangku.data.asRupiah
import com.zyvo.uangku.ui.EmptyState
import com.zyvo.uangku.ui.GoalPhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun GoalsScreen(
    goals: List<Goal>,
    onAdd: (Goal) -> Unit,
    onFund: (Long, Long) -> Unit,
    onDelete: (Goal) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var showAdd by remember { mutableStateOf(false) }
    var draftId by remember { mutableStateOf(0L) }
    var draftTitle by remember { mutableStateOf("") }
    var draftTarget by remember { mutableStateOf("") }
    var draftPhoto by remember { mutableStateOf<String?>(null) }
    var selected by remember { mutableStateOf<Goal?>(null) }

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            scope.launch {
                val path = withContext(Dispatchers.IO) { PhotoStore.save(context, uri, draftId) }
                if (path != null) draftPhoto = path
            }
        }
    }

    fun openDraft() {
        draftId = System.currentTimeMillis()
        draftTitle = ""
        draftTarget = ""
        draftPhoto = null
        showAdd = true
    }

    val totalTarget = goals.sumOf { it.target }
    val totalSaved = goals.sumOf { it.saved }
    val overall = if (totalTarget > 0) totalSaved.toFloat() / totalTarget else 0f

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item("header") {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        "Target",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.3).sp
                    )
                    Text(
                        "Barang impian & tabungannya",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                FilledTonalButton(
                    onClick = { openDraft() },
                    shape = CircleShape,
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Target", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        item("summary") {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text(
                        "TOTAL TERKUMPUL",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.3.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                    Spacer(Modifier.height(5.dp))
                    Text(
                        totalSaved.asRupiah(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.4).sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "dari " + totalTarget.asRupiah() + " · " + goals.size + " target",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                    Spacer(Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress = { overall.coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.16f)
                    )
                }
            }
        }

        if (goals.isEmpty()) {
            item("empty") {
                EmptyState(
                    icon = Icons.Outlined.Flag,
                    title = "Belum ada target",
                    hint = "Simpan foto barang impianmu, tentukan harganya, lalu tabung sedikit-sedikit.",
                    actionLabel = "Buat target",
                    onAction = { openDraft() }
                )
            }
        } else {
            items(goals, key = { it.id }) { goal ->
                GoalCard(goal) { selected = goal }
            }
        }
    }

    if (showAdd) {
        AddGoalDialog(
            photo = draftPhoto,
            title = draftTitle,
            targetText = draftTarget,
            onTitleChange = { draftTitle = it },
            onTargetChange = { input -> draftTarget = input.filter { it.isDigit() }.take(12) },
            onPickPhoto = {
                picker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            onDismiss = { showAdd = false },
            onSave = {
                val targetValue = draftTarget.toLongOrNull() ?: 0L
                if (draftTitle.isNotBlank() && targetValue > 0) {
                    onAdd(Goal(draftId, draftTitle.trim(), targetValue, 0L, draftPhoto, draftId))
                    showAdd = false
                }
            }
        )
    }

    selected?.let { goal ->
        GoalDetailDialog(
            goal = goal,
            onDismiss = { selected = null },
            onFund = { amount ->
                onFund(goal.id, amount)
                selected = null
            },
            onDelete = {
                onDelete(goal)
                selected = null
            }
        )
    }
}

@Composable
private fun GoalCard(goal: Goal, onClick: () -> Unit) {
    val pct = if (goal.target > 0) (goal.saved * 100 / goal.target).toInt() else 0
    val progress = if (goal.target > 0) (goal.saved.toFloat() / goal.target).coerceIn(0f, 1f) else 0f

    Card(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column {
            Box(Modifier.fillMaxWidth().height(148.dp)) {
                GoalPhoto(goal.photo, Modifier.fillMaxSize())
                Box(
                    Modifier.fillMaxSize().background(
                        Brush.verticalGradient(
                            0.4f to Color.Transparent,
                            1f to Color.Black.copy(alpha = 0.68f)
                        )
                    )
                )
                Column(Modifier.align(Alignment.BottomStart).padding(14.dp)) {
                    Text(
                        goal.title,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        "Target " + goal.target.asRupiah(),
                        color = Color.White.copy(alpha = 0.82f),
                        fontSize = 11.sp
                    )
                }
                Box(
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.4f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        "$pct%",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Column(Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        goal.saved.asRupiah(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        "sisa " + (goal.target - goal.saved).coerceAtLeast(0L).asRupiah(),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(7.dp).clip(CircleShape),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
    }
}

@Composable
private fun AddGoalDialog(
    photo: String?,
    title: String,
    targetText: String,
    onTitleChange: (String) -> Unit,
    onTargetChange: (String) -> Unit,
    onPickPhoto: () -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Target baru") },
        text = {
            Column {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable(onClick = onPickPhoto),
                    contentAlignment = Alignment.Center
                ) {
                    if (photo != null) {
                        GoalPhoto(photo, Modifier.fillMaxSize())
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Filled.AddPhotoAlternate,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                "Pilih foto barang",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = onTitleChange,
                    label = { Text("Nama target") },
                    placeholder = { Text("Contoh: iPhone 16") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = targetText,
                    onValueChange = onTargetChange,
                    label = { Text("Harga target") },
                    prefix = { Text("Rp ") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onSave,
                enabled = title.isNotBlank() && (targetText.toLongOrNull() ?: 0L) > 0
            ) { Text("Simpan") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}

@Composable
private fun GoalDetailDialog(
    goal: Goal,
    onDismiss: () -> Unit,
    onFund: (Long) -> Unit,
    onDelete: () -> Unit
) {
    var amountText by remember { mutableStateOf("") }
    val amount = amountText.toLongOrNull() ?: 0L
    val progress = if (goal.target > 0) (goal.saved.toFloat() / goal.target).coerceIn(0f, 1f) else 0f

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(goal.title, maxLines = 2, overflow = TextOverflow.Ellipsis) },
        text = {
            Column {
                Box(Modifier.fillMaxWidth().height(130.dp).clip(RoundedCornerShape(14.dp))) {
                    GoalPhoto(goal.photo, Modifier.fillMaxSize())
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    goal.saved.asRupiah() + " dari " + goal.target.asRupiah(),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(7.dp).clip(CircleShape),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Spacer(Modifier.height(14.dp))
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { input -> amountText = input.filter { it.isDigit() }.take(12) },
                    label = { Text("Tambah tabungan") },
                    prefix = { Text("Rp ") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onFund(amount) }, enabled = amount > 0) { Text("Tambah") }
        },
        dismissButton = {
            Row {
                TextButton(onClick = onDelete) {
                    Text("Hapus", color = MaterialTheme.colorScheme.error)
                }
                TextButton(onClick = onDismiss) { Text("Tutup") }
            }
        }
    )
}
