package com.zyvo.uangku.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zyvo.uangku.data.Tx
import com.zyvo.uangku.data.TxType
import com.zyvo.uangku.data.asCompact
import com.zyvo.uangku.data.asRupiah
import com.zyvo.uangku.data.balanceSeries
import com.zyvo.uangku.data.dayLabel
import com.zyvo.uangku.data.greeting
import com.zyvo.uangku.data.monthRange
import com.zyvo.uangku.ui.ConfirmDeleteDialog
import com.zyvo.uangku.ui.EmptyState
import com.zyvo.uangku.ui.SectionLabel
import com.zyvo.uangku.ui.StatTile
import com.zyvo.uangku.ui.TxRow
import com.zyvo.uangku.ui.theme.LocalBrand
import kotlin.math.abs

@Composable
fun HomeScreen(
    txs: List<Tx>,
    onOpenSettings: () -> Unit,
    onDelete: (Tx) -> Unit,
    onAdd: () -> Unit
) {
    val brand = LocalBrand.current
    val balance = txs.sumOf { if (it.type == TxType.INCOME) it.amount else -it.amount }
    val (start, end) = monthRange(0)
    val monthTxs = txs.filter { it.date in start until end }
    val income = monthTxs.filter { it.type == TxType.INCOME }.sumOf { it.amount }
    val expense = monthTxs.filter { it.type == TxType.EXPENSE }.sumOf { it.amount }
    val grouped = remember(txs) { txs.groupBy { it.date.dayLabel() } }
    val series = remember(txs) { balanceSeries(txs, 30) }
    var pendingDelete by remember { mutableStateOf<Tx?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 96.dp)
    ) {
        item("header") {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        greeting(),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "UangKu",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.3).sp
                    )
                }
                IconButton(onClick = onOpenSettings) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Pengaturan",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(brand.soft),
                    contentAlignment = Alignment.Center
                ) {
                    Text("U", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        item("hero") {
            val diff = (series.lastOrNull() ?: 0L) - (series.firstOrNull() ?: 0L)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(26.dp))
                    .background(Brush.linearGradient(listOf(brand.heroTop, brand.heroBottom)))
                    .padding(20.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "SALDO TOTAL",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            color = Color.White.copy(alpha = 0.72f),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            "30 HARI",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        balance.asRupiah(),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.6).sp,
                        color = Color.White
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = when {
                            txs.isEmpty() -> "Belum ada transaksi"
                            diff == 0L -> "Belum ada perubahan"
                            else -> (if (diff > 0) "+" else "-") +
                                abs(diff).asCompact() + " dalam 30 hari"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.82f)
                    )
                    Spacer(Modifier.height(10.dp))
                    BalanceCurve(series, Modifier.fillMaxWidth().height(62.dp))
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        HeroChip("Masuk", income, Icons.Filled.ArrowDownward)
                        HeroChip("Keluar", expense, Icons.Filled.ArrowUpward)
                    }
                }
            }
        }

        item("tiles") {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatTile("Pemasukan bulan ini", income.asRupiah(), brand.income, Modifier.weight(1f))
                StatTile("Pengeluaran bulan ini", expense.asRupiah(), brand.expense, Modifier.weight(1f))
            }
        }

        item("section") {
            Row(
                Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionLabel("Transaksi terbaru", Modifier.weight(1f))
                Text(
                    "Tahan untuk hapus",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (txs.isEmpty()) {
            item("empty") {
                EmptyState(
                    icon = Icons.Outlined.ReceiptLong,
                    title = "Belum ada transaksi",
                    hint = "Tekan tombol + untuk mencatat pemasukan atau pengeluaran pertamamu.",
                    actionLabel = "Catat sekarang",
                    onAction = onAdd
                )
            }
        } else {
            grouped.forEach { (day, dayTxs) ->
                item(key = "day-$day") {
                    Text(
                        text = day,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 10.dp, bottom = 2.dp)
                    )
                }
                items(dayTxs, key = { it.id }) { tx ->
                    TxRow(
                        tx = tx,
                        onLongClick = { pendingDelete = tx }
                    )
                }
            }
        }
    }

    pendingDelete?.let { tx ->
        ConfirmDeleteDialog(
            tx = tx,
            onDismiss = { pendingDelete = null },
            onConfirm = {
                onDelete(tx)
                pendingDelete = null
            }
        )
    }
}

@Composable
private fun HeroChip(label: String, amount: Long, icon: ImageVector) {
    Surface(color = Color.White.copy(alpha = 0.16f), shape = CircleShape) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
            Spacer(Modifier.width(4.dp))
            Text(
                "$label ${amount.asCompact()}",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun BalanceCurve(series: List<Long>, modifier: Modifier = Modifier) {
    if (series.size < 2) return
    val minValue = series.minOrNull() ?: 0L
    val maxValue = series.maxOrNull() ?: 0L
    val span = (maxValue - minValue).coerceAtLeast(1L)

    Canvas(modifier) {
        val width = size.width
        val height = size.height
        if (width <= 0f || height <= 0f) return@Canvas
        val stepX = width / (series.size - 1)
        val topPad = height * 0.14f
        val usable = height * 0.72f
        val ys = series.map { value ->
            topPad + usable - ((value - minValue).toFloat() / span) * usable
        }
        val line = Path().apply {
            moveTo(0f, ys[0])
            for (i in 1 until ys.size) {
                val x0 = (i - 1) * stepX
                val x1 = i * stepX
                val mid = (x0 + x1) / 2f
                cubicTo(mid, ys[i - 1], mid, ys[i], x1, ys[i])
            }
        }
        val area = Path().apply {
            addPath(line)
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }
        drawPath(
            path = area,
            brush = Brush.verticalGradient(
                colors = listOf(Color.White.copy(alpha = 0.26f), Color.Transparent),
                startY = topPad,
                endY = height
            )
        )
        drawPath(
            path = line,
            color = Color.White.copy(alpha = 0.95f),
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
        drawCircle(
            color = Color.White,
            radius = 3.dp.toPx(),
            center = Offset(width - 1.dp.toPx(), ys.last())
        )
    }
}
