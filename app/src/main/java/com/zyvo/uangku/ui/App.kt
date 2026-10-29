package com.zyvo.uangku.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zyvo.uangku.data.PhotoStore
import com.zyvo.uangku.data.Store
import com.zyvo.uangku.data.Tx
import com.zyvo.uangku.ui.screens.BudgetScreen
import com.zyvo.uangku.ui.screens.GoalsScreen
import com.zyvo.uangku.ui.screens.HomeScreen
import com.zyvo.uangku.ui.screens.SettingsScreen
import com.zyvo.uangku.ui.screens.StatsScreen
import kotlinx.coroutines.launch

private data class TabItem(val label: String, val icon: ImageVector)

private val TABS = listOf(
    TabItem("Beranda", Icons.Filled.Home),
    TabItem("Statistik", Icons.Filled.BarChart),
    TabItem("Target", Icons.Filled.Flag),
    TabItem("Anggaran", Icons.Filled.Savings)
)

@Composable
fun App() {
    val txs by Store.txs.collectAsState()
    val budgets by Store.budgets.collectAsState()
    val goals by Store.goals.collectAsState()

    var tab by rememberSaveable { mutableIntStateOf(0) }
    var showAdd by rememberSaveable { mutableStateOf(false) }
    var showSettings by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (showSettings) {
        SettingsScreen(onClose = { showSettings = false })
        return
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp
            ) {
                TABS.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = tab == index,
                        onClick = { tab = index },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = {
                            Text(item.label, fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        },
        floatingActionButton = {
            if (tab != 2) {
                FloatingActionButton(
                    onClick = { showAdd = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Tambah transaksi")
                }
            }
        }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            when (tab) {
                0 -> HomeScreen(
                    txs = txs,
                    onOpenSettings = { showSettings = true },
                    onDelete = { tx ->
                        Store.delete(tx.id)
                        scope.launch { snackbarHostState.showSnackbar("Transaksi dihapus") }
                    },
                    onAdd = { showAdd = true }
                )

                1 -> StatsScreen(txs = txs)

                2 -> GoalsScreen(
                    goals = goals,
                    onAdd = { goal ->
                        Store.addGoal(goal)
                        scope.launch { snackbarHostState.showSnackbar("Target dibuat") }
                    },
                    onFund = { id, amount ->
                        Store.addToGoal(id, amount)
                        scope.launch { snackbarHostState.showSnackbar("Tabungan ditambahkan") }
                    },
                    onDelete = { goal ->
                        Store.deleteGoal(goal.id)
                        PhotoStore.delete(goal.photo)
                        scope.launch { snackbarHostState.showSnackbar("Target dihapus") }
                    }
                )

                else -> BudgetScreen(
                    txs = txs,
                    budgets = budgets,
                    onSetBudget = { category, limit -> Store.setBudget(category, limit) }
                )
            }
        }
    }

    if (showAdd) {
        AddTxSheet(
            onDismiss = { showAdd = false },
            onSave = { type, amount, category, note, date ->
                Store.add(Tx(Store.newId(), type, amount, category, note, date))
                showAdd = false
                scope.launch { snackbarHostState.showSnackbar("Transaksi tersimpan") }
            }
        )
    }
}
