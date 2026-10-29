package com.zyvo.uangku.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zyvo.uangku.data.EXPENSE_CATEGORIES
import com.zyvo.uangku.data.Tx
import com.zyvo.uangku.data.TxType
import com.zyvo.uangku.data.asRupiah
import com.zyvo.uangku.data.monthName
import com.zyvo.uangku.data.monthRange
import com.zyvo.uangku.ui.CategoryBadge
import com.zyvo.uangku.ui.SectionLabel

@Composable
fun BudgetScreen(
    txs: List<Tx>,
    budgets: Map<String, Long>,
    onSetBudget: (String, Long) -> Unit
) {
    val monthTxs = remember(txs) {
        val (start, end) = monthRange(0)
        txs.filter { it.date in start until end }
    }
    val spentByCategory = remember(monthTxs) {
        monthTxs.filter { it.type == TxType.EXPENSE }
            .groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
    }

    val totalLimit = budgets.values.sum()
    val spentInBudget = budgets.keys.sumOf { spentByCategory[it] ?: 0L }
    val remaining = totalLimit - spentInBudget
    val fraction = if (totalLimit > 0) spentInBudget.toFloat() / totalLimit else 0f

    val ordered = remember(budgets, spentByCategory) {
        EXPENSE_CATEGORIES.sortedWith(
            compareByDescending<String> { budgets.containsKey(it) }
                .thenByDescending { spentByCategory[it] ?: 0L }
        )
    }

    var editing by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp)
    ) {
        item("title") {
            Column(Modifier.padding(vertical = 6.dp)) {
                Text(
                    "Anggaran",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.3).sp
                )
                Text(
                    monthName(0),
                    fontSize = 12.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item("summary") {
            Card(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text(
                        "TOTAL ANGGARAN BULAN INI",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.3.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                    Spacer(Modifier.height(5.dp))
                    Text(
                        totalLimit.asRupiah(),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { fraction.coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                        color = if (fraction > 1f) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.16f)
                    )
                    Spacer(Modifier.height(10.dp))
                    Row(Modifier.fillMaxWidth()) {
                        Text(
                            "Terpakai " + spentInBudget.asRupiah(),
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            "Sisa " + remaining.asRupiah(),
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }

        item("section") {
            Row(Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 8.dp)) {
                SectionLabel("Anggaran per kategori", Modifier.weight(1f))
                Text(
                    "Ketuk untuk atur",
                    fontSize = 10.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item("list") {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    ordered.forEach { category ->
                        val limit = budgets[category]
                        val spent = spentByCategory[category] ?: 0L
                        BudgetRow(
                            category = category,
                            limit = limit,
                            spent = spent,
                            onClick = { editing = category }
                        )
                    }
                }
            }
        }
    }

    editing?.let { category ->
        BudgetDialog(
            category = category,
            current = budgets[category],
            onDismiss = { editing = null },
            onSave = { value ->
                onSetBudget(category, value)
                editing = null
            }
        )
    }
}

@Composable
private fun BudgetRow(category: String, limit: Long?, spent: Long, onClick: () -> Unit) {
    val fraction = if (limit != null && limit > 0) spent.toFloat() / limit else 0f
    val over = limit != null && limit > 0 && spent > limit
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CategoryBadge(category, 34.dp)
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(category, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(2.dp))
                Text(
                    text = if (limit == null || limit <= 0) "Belum diatur"
                    else spent.asRupiah() + " dari " + limit.asRupiah(),
                    fontSize = 10.5.sp,
                    color = if (over) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (limit == null || limit <= 0) {
                Box(
                    Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        "Atur",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            } else {
                Text(
                    if (over) "Lewat" else "${(fraction * 100).toInt()}%",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (over) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            }
        }
        if (limit != null && limit > 0) {
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { fraction.coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                color = if (over) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}

@Composable
private fun BudgetDialog(
    category: String,
    current: Long?,
    onDismiss: () -> Unit,
    onSave: (Long) -> Unit
) {
    var text by remember { mutableStateOf(current?.toString() ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Anggaran $category") },
        text = {
            Column {
                Text(
                    "Tentukan batas pengeluaran per bulan.",
                    fontSize = 12.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = text,
                    onValueChange = { input ->
                        text = input.filter { it.isDigit() }.take(12)
                    },
                    label = { Text("Batas per bulan") },
                    prefix = { Text("Rp ") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(text.toLongOrNull() ?: 0L) }) {
                Text("Simpan")
            }
        },
        dismissButton = {
            Row {
                if (current != null) {
                    TextButton(onClick = { onSave(0L) }) {
                        Text("Hapus", color = MaterialTheme.colorScheme.error)
                    }
                }
                TextButton(onClick = onDismiss) { Text("Batal") }
            }
        }
    )
}
