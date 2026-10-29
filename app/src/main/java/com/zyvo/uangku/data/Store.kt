package com.zyvo.uangku.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import org.json.JSONArray
import org.json.JSONObject

/**
 * Local-only storage. Everything lives in SharedPreferences as JSON so the
 * app works fully offline with no database dependency.
 */
object Store {

    private const val PREFS = "uangku_store"
    private const val KEY_TX = "transactions"
    private const val KEY_BUDGET = "budgets"
    private const val KEY_THEME = "theme_mode"

    private lateinit var prefs: SharedPreferences

    val txs = MutableStateFlow<List<Tx>>(emptyList())
    val budgets = MutableStateFlow<Map<String, Long>>(emptyMap())

    /** 0 = follow system, 1 = light, 2 = dark */
    val themeMode = MutableStateFlow(0)

    fun init(context: Context) {
        if (::prefs.isInitialized) return
        prefs = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        txs.value = readTxs()
        budgets.value = readBudgets()
        themeMode.value = prefs.getInt(KEY_THEME, 0)
    }

    fun setThemeMode(mode: Int) {
        themeMode.value = mode
        prefs.edit().putInt(KEY_THEME, mode).apply()
    }

    fun add(tx: Tx) {
        val next = (txs.value + tx).sortedWith(compareByDescending<Tx> { it.date }.thenByDescending { it.id })
        txs.value = next
        writeTxs(next)
    }

    fun delete(id: Long) {
        val next = txs.value.filterNot { it.id == id }
        txs.value = next
        writeTxs(next)
    }

    fun setBudget(category: String, limit: Long) {
        val next = budgets.value.toMutableMap()
        if (limit <= 0) next.remove(category) else next[category] = limit
        budgets.value = next
        writeBudgets(next)
    }

    fun newId(): Long = System.currentTimeMillis()

    // ---------- persistence ----------

    private fun readTxs(): List<Tx> = runCatching {
        val raw = prefs.getString(KEY_TX, null) ?: return emptyList()
        val arr = JSONArray(raw)
        buildList {
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                add(
                    Tx(
                        id = o.getLong("id"),
                        type = if (o.getString("type") == "INCOME") TxType.INCOME else TxType.EXPENSE,
                        amount = o.getLong("amount"),
                        category = o.getString("category"),
                        note = o.optString("note", ""),
                        date = o.getLong("date")
                    )
                )
            }
        }
    }.getOrElse { emptyList() }

    private fun writeTxs(list: List<Tx>) {
        val arr = JSONArray()
        list.forEach { t ->
            arr.put(
                JSONObject().apply {
                    put("id", t.id)
                    put("type", t.type.name)
                    put("amount", t.amount)
                    put("category", t.category)
                    put("note", t.note)
                    put("date", t.date)
                }
            )
        }
        prefs.edit().putString(KEY_TX, arr.toString()).apply()
    }

    private fun readBudgets(): Map<String, Long> = runCatching {
        val raw = prefs.getString(KEY_BUDGET, null) ?: return emptyMap()
        val o = JSONObject(raw)
        buildMap {
            o.keys().forEach { k -> put(k, o.getLong(k)) }
        }
    }.getOrElse { emptyMap() }

    private fun writeBudgets(map: Map<String, Long>) {
        val o = JSONObject()
        map.forEach { (k, v) -> o.put(k, v) }
        prefs.edit().putString(KEY_BUDGET, o.toString()).apply()
    }
}
