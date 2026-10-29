package com.zyvo.uangku.ui.screens

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zyvo.uangku.data.Backup
import com.zyvo.uangku.data.Store
import com.zyvo.uangku.ui.SectionLabel
import com.zyvo.uangku.ui.theme.Accent
import com.zyvo.uangku.ui.theme.AppStyle
import com.zyvo.uangku.ui.theme.accentSwatch
import com.zyvo.uangku.ui.theme.styleHero
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SettingsScreen(onClose: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    val themeId by Store.themeId.collectAsState()
    val accentId by Store.accentId.collectAsState()
    val themeMode by Store.themeMode.collectAsState()
    val txs by Store.txs.collectAsState()
    val goals by Store.goals.collectAsState()
    val budgets by Store.budgets.collectAsState()

    val isDark = when (themeMode) {
        1 -> false
        2 -> true
        else -> isSystemInDarkTheme()
    }
    var pendingImport by remember { mutableStateOf<String?>(null) }
    var busy by remember { mutableStateOf(false) }

    fun snack(message: String) {
        scope.launch { snackbar.showSnackbar(message) }
    }

    val exportLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            busy = true
            val ok = withContext(Dispatchers.IO) {
                runCatching {
                    val text = Backup.export()
                    context.contentResolver.openOutputStream(uri)?.use {
                        it.write(text.toByteArray())
                    } ?: error("tidak bisa menulis file")
                }.isSuccess
            }
            busy = false
            snack(if (ok) "Backup tersimpan" else "Gagal menyimpan backup")
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            busy = true
            val text = withContext(Dispatchers.IO) {
                runCatching {
                    context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
                }.getOrNull()
            }
            busy = false
            if (text.isNullOrBlank()) snack("File tidak bisa dibaca") else pendingImport = text
        }
    }

    BackHandler(onBack = onClose)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 40.dp)
        ) {
            item("header") {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onClose) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(Modifier.width(2.dp))
                    Column {
                        Text(
                            "Pengaturan",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.3).sp
                        )
                        Text(
                            "Tema, tampilan & data",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            item("theme-label") {
                SectionLabel("Gaya tampilan", Modifier.padding(top = 18.dp, bottom = 8.dp))
            }

            item("themes") {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(AppStyle.values().toList(), key = { it.id }) { style ->
                        StyleCard(
                            style = style,
                            accent = Accent.fromId(accentId),
                            dark = isDark,
                            selected = themeId == style.id,
                            onClick = { Store.setThemeId(style.id) }
                        )
                    }
                }
            }

            item("accent-label") {
                SectionLabel("Warna aksen", Modifier.padding(top = 22.dp, bottom = 8.dp))
            }

            item("accents") {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(Accent.values().toList(), key = { it.id }) { accent ->
                        val swatch = accentSwatch(accent, isDark)
                            ?: styleHero(AppStyle.fromId(themeId), Accent.AUTO, isDark).first
                        FilterChip(
                            selected = accentId == accent.id,
                            onClick = { Store.setAccentId(accent.id) },
                            shape = CircleShape,
                            leadingIcon = {
                                Box(
                                    Modifier
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(swatch)
                                )
                            },
                            label = { Text(accent.label, fontSize = 12.sp) }
                        )
                    }
                }
            }

            item("mode-label") {
                SectionLabel("Mode gelap", Modifier.padding(top = 22.dp, bottom = 8.dp))
            }

            item("modes") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ModeChip("Sistem", themeMode == 0) { Store.setThemeMode(0) }
                    ModeChip("Terang", themeMode == 1) { Store.setThemeMode(1) }
                    ModeChip("Gelap", themeMode == 2) { Store.setThemeMode(2) }
                }
            }

            item("data-label") {
                SectionLabel("Data", Modifier.padding(top = 22.dp, bottom = 8.dp))
            }

            item("data-card") {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        DataRow(
                            icon = Icons.Outlined.FileUpload,
                            title = "Export data",
                            subtitle = "Simpan transaksi, anggaran & target ke file .json",
                            enabled = !busy,
                            onClick = {
                                val stamp = SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
                                exportLauncher.launch("uangku-backup-$stamp.json")
                            }
                        )
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        DataRow(
                            icon = Icons.Outlined.FileDownload,
                            title = "Import data",
                            subtitle = "Pulihkan semua data dari file backup",
                            enabled = !busy,
                            onClick = {
                                importLauncher.launch(arrayOf("application/json", "text/plain", "*/*"))
                            }
                        )
                    }
                }
            }

            item("storage-label") {
                SectionLabel("Penyimpanan", Modifier.padding(top = 22.dp, bottom = 8.dp))
            }

            item("storage-card") {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "${txs.size} transaksi · ${goals.size} target · ${budgets.size} anggaran",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Semua data tersimpan lokal di HP ini. Tidak ada server, tidak ada akun.",
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            item("about-label") {
                SectionLabel("Tentang", Modifier.padding(top = 22.dp, bottom = 8.dp))
            }

            item("about") {
                Column {
                    Text(
                        "UangKu 2.0",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "Dibuat dengan Kotlin + Jetpack Compose.",
                        fontSize = 11.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    pendingImport?.let { raw ->
        AlertDialog(
            onDismissRequest = { pendingImport = null },
            title = { Text("Import data?") },
            text = {
                Text(
                    "Semua transaksi, anggaran, dan target saat ini akan DIGANTI " +
                        "dengan isi file backup. Lanjutkan?"
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    val text = raw
                    pendingImport = null
                    scope.launch {
                        busy = true
                        val result = withContext(Dispatchers.IO) { Backup.import(context, text) }
                        busy = false
                        snack(
                            if (result == null) {
                                "File backup tidak valid"
                            } else {
                                "Import selesai: ${result.txCount} transaksi, ${result.goalCount} target"
                            }
                        )
                    }
                }) { Text("Import") }
            },
            dismissButton = {
                TextButton(onClick = { pendingImport = null }) { Text("Batal") }
            }
        )
    }
}

@Composable
private fun StyleCard(
    style: AppStyle,
    accent: Accent,
    dark: Boolean,
    selected: Boolean,
    onClick: () -> Unit
) {
    val (top, bottom) = styleHero(style, accent, dark)
    val previewRadius = when (style) {
        AppStyle.PIXEL -> 6.dp
        AppStyle.NIGHT, AppStyle.SAND -> 12.dp
        AppStyle.LEDGER -> 16.dp
    }
    val shape = RoundedCornerShape(previewRadius)
    Column(
        modifier = Modifier
            .width(108.dp)
            .clip(shape)
            .then(
                if (selected) {
                    Modifier.border(2.dp, MaterialTheme.colorScheme.primary, shape)
                } else {
                    Modifier.border(1.dp, MaterialTheme.colorScheme.outlineVariant, shape)
                }
            )
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(previewRadius - 3.dp))
                .then(
                    if (style == AppStyle.LEDGER) {
                        Modifier.background(Brush.linearGradient(listOf(top, bottom)))
                    } else {
                        Modifier.background(top)
                    }
                )
                .then(
                    if (style == AppStyle.PIXEL || style == AppStyle.SAND) {
                        Modifier.border(
                            1.5.dp,
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                            RoundedCornerShape(previewRadius - 3.dp)
                        )
                    } else {
                        Modifier
                    }
                ),
            contentAlignment = Alignment.TopEnd
        ) {
            if (selected) {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(6.dp).size(16.dp)
                )
            }
        }
        Spacer(Modifier.height(7.dp))
        Text(
            style.label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 3.dp)
        )
        Text(
            style.tagline,
            fontSize = 9.5.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 3.dp, vertical = 1.dp)
        )
    }
}

@Composable
private fun ModeChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label, fontSize = 12.sp) },
        shape = CircleShape
    )
}

@Composable
private fun DataRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(19.dp)
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(title, fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold)
            Text(
                subtitle,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
