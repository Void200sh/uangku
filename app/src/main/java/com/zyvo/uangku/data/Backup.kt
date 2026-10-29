package com.zyvo.uangku.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.Base64

/**
 * Import / export seluruh data (transaksi, anggaran, target + fotonya) dalam
 * satu file JSON. Foto ikut dikodekan base64 supaya backup tetap utuh walau
 * file aslinya sudah tidak ada.
 */
object Backup {

    private const val MAGIC = "uangku"

    data class ImportResult(val txCount: Int, val goalCount: Int)

    fun export(): String {
        val root = JSONObject()
        root.put("app", MAGIC)
        root.put("version", 2)
        root.put("exportedAt", System.currentTimeMillis())
        root.put("themeId", Store.themeId.value)
        root.put("accentId", Store.accentId.value)
        root.put("themeMode", Store.themeMode.value)

        val txsArr = JSONArray()
        Store.txs.value.forEach { t ->
            txsArr.put(
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
        root.put("transactions", txsArr)

        val budgetsObj = JSONObject()
        Store.budgets.value.forEach { (k, v) -> budgetsObj.put(k, v) }
        root.put("budgets", budgetsObj)

        val goalsArr = JSONArray()
        Store.goals.value.forEach { g ->
            goalsArr.put(
                JSONObject().apply {
                    put("id", g.id)
                    put("title", g.title)
                    put("target", g.target)
                    put("saved", g.saved)
                    put("createdAt", g.createdAt)
                    put("photo", encodePhoto(g.photo))
                }
            )
        }
        root.put("goals", goalsArr)
        return root.toString()
    }

    fun import(context: Context, raw: String): ImportResult? = runCatching {
        val root = JSONObject(raw)
        require(root.optString("app") == MAGIC) { "bukan file backup UangKu" }

        val txs = buildList {
            val arr = root.optJSONArray("transactions") ?: JSONArray()
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

        val budgets = buildMap {
            val o = root.optJSONObject("budgets") ?: JSONObject()
            o.keys().forEach { k -> put(k, o.getLong(k)) }
        }

        // Foto target lama dibersihkan dulu supaya tidak menumpuk.
        Store.goals.value.forEach { PhotoStore.delete(it.photo) }

        val goals = buildList {
            val arr = root.optJSONArray("goals") ?: JSONArray()
            val dir = File(context.filesDir, "goals").apply { mkdirs() }
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                val id = o.getLong("id")
                var photoPath: String? = null
                val b64 = o.optString("photo", "")
                if (b64.isNotBlank()) {
                    runCatching {
                        val bytes = Base64.getDecoder().decode(b64)
                        val file = File(dir, "goal_$id.jpg")
                        file.writeBytes(bytes)
                        photoPath = file.absolutePath
                    }
                }
                add(
                    Goal(
                        id = id,
                        title = o.getString("title"),
                        target = o.getLong("target"),
                        saved = o.optLong("saved", 0L),
                        photo = photoPath,
                        createdAt = o.optLong("createdAt", id)
                    )
                )
            }
        }

        Store.replaceAll(txs, budgets, goals)
        ImportResult(txCount = txs.size, goalCount = goals.size)
    }.getOrNull()

    private fun encodePhoto(path: String?): String {
        if (path.isNullOrBlank()) return ""
        val file = File(path)
        if (!file.exists()) return ""
        return runCatching {
            Base64.getEncoder().encodeToString(file.readBytes())
        }.getOrDefault("")
    }
}
