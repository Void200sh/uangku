package com.zyvo.uangku.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zyvo.uangku.data.Tx
import com.zyvo.uangku.data.TxType
import com.zyvo.uangku.data.asRupiah
import com.zyvo.uangku.data.monthName
import com.zyvo.uangku.data.monthRange
import com.zyvo.uangku.data.monthShort
import com.zyvo.uangku.ui.CategoryBadge
import com.zyvo.uangku.ui.EmptyState
import com.zyvo.uangku.ui.LookCard
import com.zyvo.uangku.ui.SectionLabel
import com.zyvo.uangku.ui.StatTile
import com.zyvo.uangku.ui.categoryStyle
import com.zyvo.uangku.ui.theme.LocalBrand

private data class ChartPoint(val label: String, val income: Long, val expense: Long)

@Composable
fun StatsScreen(txs: List<Tx>) {
    val brand = LocalBrand.current
    var offset by rememberSaveable { mutableIntStateOf(0) }

    val monthTxs = remember(txs, offset) {
        val (start, end) = monthRange(offset)
        txs.filter { it.date in start until end }
    }
    val income = monthTxs.filter { it.type == TxType.INCOME }.sumOf { it.amount }
    val expense = monthTxs.filter { it.type == TxType.EXPENSE }.sumOf { it.amount }

    val chart = remember(txs, offset) {
        (5 downTo 0).map { i ->
            val (s, e) = monthRange(offset - i)
            val list = txs.filter { it.date in s until e }
            ChartPoint(
                label = monthShort(offset - i),
                income = list.filter { it.type == TxType.INCOME }.sumOf { it.amount },
                expense = list.filter { it.type == TxType.EXPENSE }.sumOf { it.amount }
            )
        }
    }
    val maxValue = maxOf(1L, chart.maxOf { maxOf(it.income, it.expense) })

    val byCategory = remember(monthTxs) {
        monthTxs.filter { it.type == TxType.EXPENSE }
            .groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
            .toList()
            .sortedByDescending { it.second }
    }
    val maxCategory = byCategory.maxOfOrNull { it.second } ?: 1L

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp)
    ) {
        item("title") {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Statistik",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.3).sp,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item("month") {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    monthName(offset),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { offset -= 1 }, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Filled.ChevronLeft, contentDescription = "Bulan sebelumnya", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                IconButton(
                    onClick = { if (offset < 0) offset += 1 },
                    enabled = offset < 0,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Filled.ChevronRight,
                        contentDescription = "Bulan berikutnya",
                        tint = if (offset < 0) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.outline
                    )
                }
            }
        }

        item("tiles") {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatTile("Masuk", income.asRupiah(), brand.income, Modifier.weight(1f))
                StatTile("Keluar", expense.asRupiah(), brand.expense, Modifier.weight(1f))
            }
        }

        item("chart") {
            LookCard(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                padding = 16.dp
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "6 bulan terakhir",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        LegendDot(brand.income, "Masuk")
                        Spacer(Modifier.width(10.dp))
                        LegendDot(brand.expense, "Keluar")
                    }
                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().height(128.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        chart.forEach { point ->
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    modifier = Modifier.height(104.dp),
                                    verticalAlignment = Alignment.Bottom,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Bar(point.income.toFloat() / maxValue, brand.income)
                                    Bar(point.expense.toFloat() / maxValue, brand.expense)
                                }
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    point.label,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        item("breakdown-title") {
            SectionLabel("Pengeluaran per kategori", Modifier.padding(top = 24.dp, bottom = 8.dp))
        }

        if (byCategory.isEmpty()) {
            item("empty") {
                EmptyState(
                    icon = Icons.Outlined.BarChart,
                    title = "Belum ada pengeluaran",
                    hint = "Tidak ada pengeluaran tercatat di " + monthName(offset) + "."
                )
            }
        } else {
            item("breakdown") {
                LookCard(modifier = Modifier.fillMaxWidth(), padding = 16.dp) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        byCategory.forEach { (category, value) ->
                            CategoryBreakdownRow(
                                category = category,
                                value = value,
                                fraction = value.toFloat() / maxCategory,
                                percent = if (expense > 0) value * 100 / expense else 0
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(Modifier.width(4.dp))
        Text(label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun Bar(fraction: Float, color: Color) {
    val animated by animateFloatAsState(
        targetValue = fraction.coerceIn(0.03f, 1f),
        label = "bar"
    )
    Box(
        Modifier
            .width(9.dp)
            .fillMaxHeight(animated)
            .clip(RoundedCornerShape(4.dp))
            .background(color)
    )
}

@Composable
private fun CategoryBreakdownRow(
    category: String,
    value: Long,
    fraction: Float,
    percent: Long
) {
    val style = categoryStyle(category)
    Row(verticalAlignment = Alignment.CenterVertically) {
        CategoryBadge(category, 34.dp)
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(category, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                Text(value.asRupiah(), fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { fraction.coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                color = style.color,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "$percent% dari total pengeluaran",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
