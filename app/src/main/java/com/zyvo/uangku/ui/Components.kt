package com.zyvo.uangku.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zyvo.uangku.data.Tx
import com.zyvo.uangku.data.TxType
import com.zyvo.uangku.data.asRupiah
import com.zyvo.uangku.data.dayLabel
import com.zyvo.uangku.ui.theme.AppStyle
import com.zyvo.uangku.ui.theme.LocalBrand
import com.zyvo.uangku.ui.theme.LocalLook

@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        modifier = modifier,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.2.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun lookBorderColor(): Color = when (LocalLook.current.style) {
    AppStyle.NIGHT -> MaterialTheme.colorScheme.outline
    else -> MaterialTheme.colorScheme.onSurface
}

/** Border putus-putus untuk gaya Sand Wallet. */
fun Modifier.dashedBorder(color: Color, width: Dp, radius: Dp): Modifier = drawBehind {
    val stroke = width.toPx()
    drawRoundRect(
        color = color,
        topLeft = Offset(stroke / 2f, stroke / 2f),
        size = Size(size.width - stroke, size.height - stroke),
        cornerRadius = CornerRadius(radius.toPx()),
        style = Stroke(
            width = stroke,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(9.dp.toPx(), 6.dp.toPx()))
        )
    )
}

/**
 * Kartu serba-gaya: radius, border tebal, dan bayangan "stiker" mengikuti
 * gaya yang sedang aktif (Ledger / Night Mint / Sand Wallet / Pixel).
 */
@Composable
fun LookCard(
    modifier: Modifier = Modifier,
    container: Color = MaterialTheme.colorScheme.surface,
    padding: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val look = LocalLook.current
    val shape = RoundedCornerShape(look.radius)
    Box(modifier = modifier) {
        if (look.hardShadow > 0.dp) {
            Box(
                Modifier
                    .matchParentSize()
                    .offset(x = look.hardShadow, y = look.hardShadow)
                    .clip(shape)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.22f))
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(container)
                .then(
                    if (look.border > 0.dp) {
                        Modifier.border(look.border, lookBorderColor(), shape)
                    } else {
                        Modifier
                    }
                )
                .padding(padding),
            content = content
        )
    }
}

@Composable
fun CategoryBadge(name: String, size: Dp = 40.dp) {
    val style = categoryStyle(name)
    val look = LocalLook.current
    Box(
        modifier = Modifier
            .size(size)
            .clip(if (look.circleIcons) CircleShape else RoundedCornerShape(size * 0.32f))
            .background(style.color.copy(alpha = 0.14f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = style.icon,
            contentDescription = null,
            tint = style.color,
            modifier = Modifier.size(size * 0.5f)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TxRow(
    tx: Tx,
    onClick: () -> Unit = {},
    onLongClick: (() -> Unit)? = null
) {
    val brand = LocalBrand.current
    val look = LocalLook.current
    val dividerColor = MaterialTheme.colorScheme.outlineVariant
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .padding(vertical = 10.dp)
            .then(
                if (look.rowDivider) {
                    Modifier.drawBehind {
                        drawLine(
                            color = dividerColor,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                } else {
                    Modifier
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CategoryBadge(tx.category)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = if (tx.note.isNotBlank()) tx.note else tx.category,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = tx.category + " · " + tx.date.dayLabel(),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = (if (tx.type == TxType.INCOME) "+" else "-") + tx.amount.asRupiah(),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = if (look.mono) FontFamily.Monospace else FontFamily.Default,
            color = if (tx.type == TxType.INCOME) brand.income else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun StatTile(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    val look = LocalLook.current
    val shape = RoundedCornerShape(look.radius)
    Card(
        modifier = modifier.then(
            when {
                look.dashed -> Modifier.dashedBorder(MaterialTheme.colorScheme.outline, 2.dp, look.radius)
                look.border > 0.dp -> Modifier.border(look.border, lookBorderColor(), shape)
                else -> Modifier
            }
        ),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(horizontal = 14.dp, vertical = 13.dp)) {
            Text(
                text = label.uppercase(),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = if (look.mono) FontFamily.Monospace else FontFamily.Default,
                color = valueColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun EmptyState(
    icon: ImageVector,
    title: String,
    hint: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 36.dp, horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(68.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(30.dp)
            )
        }
        Spacer(Modifier.height(14.dp))
        Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text(
            text = hint,
            fontSize = 12.5.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        if (actionLabel != null && onAction != null) {
            Spacer(Modifier.height(16.dp))
            androidx.compose.material3.Button(
                onClick = onAction,
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(actionLabel, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun ConfirmDeleteDialog(
    tx: Tx,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Hapus transaksi?") },
        text = {
            Text(
                "\"" + (if (tx.note.isNotBlank()) tx.note else tx.category) +
                    "\" sebesar " + tx.amount.asRupiah() + " akan dihapus permanen."
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Hapus", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}
