package com.zyvo.uangku.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.unit.Dp
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
import com.zyvo.uangku.ui.lookBorderColor
import com.zyvo.uangku.ui.theme.AppStyle
import com.zyvo.uangku.ui.theme.LocalBrand
import com.zyvo.uangku.ui.theme.LocalLook
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.text.font.FontFamily
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
            val look = LocalLook.current
            val diff = (series.lastOrNull() ?: 0L) - (series.firstOrNull() ?: 0L)
            val heroShape = RoundedCornerShape(look.heroRadius)
            val onHero = when (look.style) {
                AppStyle.LEDGER, AppStyle.PIXEL -> Color.White
                else -> MaterialTheme.colorScheme.onSurface
            }
            val curveColor = when (look.style) {
                AppStyle.LEDGER, AppStyle.PIXEL -> Color.White
                AppStyle.NIGHT -> brand.income
                AppStyle.SAND -> MaterialTheme.colorScheme.onSurface
            }
            val chipBg = when (look.style) {
                AppStyle.LEDGER -> Color.White.copy(alpha = 0.16f)
                AppStyle.NIGHT -> MaterialTheme.colorScheme.surfaceVariant
                AppStyle.SAND -> Color.Transparent
                AppStyle.PIXEL -> Color.White.copy(alpha = 0.2f)
            }
            val chipBorder = if (look.style == AppStyle.SAND) 1.5.dp else 0.dp
            Box(Modifier.fillMaxWidth()) {
                if (look.hardShadow > 0.dp) {
                    Box(
                        Modifier
                            .matchParentSize()
                            .offset(x = look.hardShadow, y = look.hardShadow)
                            .clip(heroShape)
                            .background(MaterialTheme.colorScheme.onSurface)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(heroShape)
                        .then(
                            if (look.style == AppStyle.LEDGER) {
                                Modifier.background(
                                    Brush.linearGradient(listOf(brand.heroTop, brand.heroBottom))
                                )
                            } else {
                                Modifier.background(
                                    when (look.style) {
                                        AppStyle.NIGHT -> MaterialTheme.colorScheme.surface
                                        AppStyle.PIXEL -> MaterialTheme.colorScheme.primary
                                        else -> brand.heroTop
                                    }
                                )
                            }
                        )
                        .then(
                            if (look.border > 0.dp) {
                                Modifier.border(look.border, lookBorderColor(), heroShape)
                            } else {
                                Modifier
                            }
                        )
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "SALDO TOTAL",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            color = onHero.copy(alpha = 0.72f),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            "30 HARI",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = onHero.copy(alpha = 0.5f)
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        balance.asRupiah(),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.6).sp,
                        fontFamily = if (look.mono) FontFamily.Monospace else FontFamily.Default,
                        color = onHero
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
                        color = onHero.copy(alpha = 0.82f)
                    )
                    Spacer(Modifier.height(10.dp))
                    BalanceCurve(
                        series = series,
                        color = curveColor,
                        sharp = look.sharpCharts,
                        modifier = Modifier.fillMaxWidth().height(62.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        HeroChip("Masuk", income, Icons.Filled.ArrowDownward, chipBg, onHero, chipBorder)
                        HeroChip("Keluar", expense, Icons.Filled.ArrowUpward, chipBg, onHero, chipBorder)
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
private fun HeroChip(
    label: String,
    amount: Long,
    icon: ImageVector,
    background: Color,
    contentColor: Color,
    borderWidth: Dp
) {
    Surface(
        color = background,
        shape = CircleShape,
        border = if (borderWidth > 0.dp) BorderStroke(borderWidth, contentColor) else null
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(13.dp))
            Spacer(Modifier.width(4.dp))
            Text(
                "$label ${amount.asCompact()}",
                color = contentColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun BalanceCurve(
    series: List<Long>,
    color: Color,
    sharp: Boolean,
    modifier: Modifier = Modifier
) {
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
                val x1 = i * stepX
                if (sharp) {
                    lineTo(x1, ys[i])
                } else {
                    val x0 = (i - 1) * stepX
                    val mid = (x0 + x1) / 2f
                    cubicTo(mid, ys[i - 1], mid, ys[i], x1, ys[i])
                }
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
                colors = listOf(color.copy(alpha = 0.26f), Color.Transparent),
                startY = topPad,
                endY = height
            )
        )
        drawPath(
            path = line,
            color = color,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
        drawCircle(
            color = color,
            radius = 3.dp.toPx(),
            center = Offset(width - 1.dp.toPx(), ys.last())
        )
    }
}
