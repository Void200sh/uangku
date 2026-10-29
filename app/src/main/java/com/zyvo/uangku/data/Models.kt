package com.zyvo.uangku.data

import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

enum class TxType { INCOME, EXPENSE }

/** Barang/impian yang sedang ditabung, lengkap dengan fotonya. */
data class Goal(
    val id: Long,
    val title: String,
    val target: Long,
    val saved: Long,
    val photo: String?,
    val createdAt: Long
)

data class Tx(
    val id: Long,
    val type: TxType,
    val amount: Long,
    val category: String,
    val note: String,
    val date: Long
)

val INCOME_CATEGORIES = listOf("Gaji", "Freelance", "Bonus", "Investasi", "Lainnya")
val EXPENSE_CATEGORIES = listOf(
    "Makanan", "Transport", "Belanja", "Tagihan", "Hiburan",
    "Kesehatan", "Pendidikan", "Lainnya"
)

private val idr: NumberFormat = NumberFormat.getCurrencyInstance(Locale("in", "ID")).apply {
    maximumFractionDigits = 0
}

fun Long.asRupiah(): String = idr.format(this).replace("Rp", "Rp ").trim()

/** 6500000 -> "6,5jt" for tight spaces */
fun Long.asCompact(): String = when {
    this >= 1_000_000_000 -> String.format(Locale.US, "%.1fM", this / 1_000_000_000.0)
        .replace(".0", "")
    this >= 1_000_000 -> String.format(Locale.US, "%.1fjt", this / 1_000_000.0)
        .replace(".0", "")
    this >= 1_000 -> String.format(Locale.US, "%.0frb", this / 1_000.0)
    else -> toString()
}

fun Long.isSameMonthAs(other: Long): Boolean {
    val a = Calendar.getInstance().apply { timeInMillis = this@isSameMonthAs }
    val b = Calendar.getInstance().apply { timeInMillis = other }
    return a.get(Calendar.YEAR) == b.get(Calendar.YEAR) &&
        a.get(Calendar.MONTH) == b.get(Calendar.MONTH)
}

fun Long.monthKey(): String {
    val c = Calendar.getInstance().apply { timeInMillis = this@monthKey }
    return "${c.get(Calendar.YEAR)}-${c.get(Calendar.MONTH)}"
}

fun Long.dayLabel(): String {
    val now = Calendar.getInstance()
    val then = Calendar.getInstance().apply { timeInMillis = this@dayLabel }
    val sameYear = now.get(Calendar.YEAR) == then.get(Calendar.YEAR)
    val dayOfYear = now.get(Calendar.DAY_OF_YEAR)
    val diff = dayOfYear - then.get(Calendar.DAY_OF_YEAR)
    if (sameYear && diff == 0) return "Hari ini"
    if (sameYear && diff == 1) return "Kemarin"
    val fmt = java.text.SimpleDateFormat("d MMM", Locale("in", "ID"))
    return fmt.format(java.util.Date(this))
}

val MONTH_NAMES = listOf(
    "Januari", "Februari", "Maret", "April", "Mei", "Juni",
    "Juli", "Agustus", "September", "Oktober", "November", "Desember"
)

/** Saldo kumulatif di akhir tiap hari, dari yang paling lama ke hari ini. */
fun balanceSeries(txs: List<Tx>, days: Int): List<Long> {
    val cal = Calendar.getInstance()
    cal.set(Calendar.HOUR_OF_DAY, 23)
    cal.set(Calendar.MINUTE, 59)
    cal.set(Calendar.SECOND, 59)
    cal.set(Calendar.MILLISECOND, 999)
    val endToday = cal.timeInMillis
    val dayMs = 24L * 60 * 60 * 1000
    return (days - 1 downTo 0).map { back ->
        val limit = endToday - back * dayMs
        txs.filter { it.date <= limit }
            .sumOf { if (it.type == TxType.INCOME) it.amount else -it.amount }
    }
}

/** Returns first millis of the month (inclusive) and first millis of the next month (exclusive). */
fun monthRange(offset: Int): Pair<Long, Long> {
    val c = Calendar.getInstance()
    c.set(Calendar.DAY_OF_MONTH, 1)
    c.set(Calendar.HOUR_OF_DAY, 0)
    c.set(Calendar.MINUTE, 0)
    c.set(Calendar.SECOND, 0)
    c.set(Calendar.MILLISECOND, 0)
    c.add(Calendar.MONTH, offset)
    val start = c.timeInMillis
    c.add(Calendar.MONTH, 1)
    return start to c.timeInMillis
}

fun monthName(offset: Int): String {
    val c = Calendar.getInstance()
    c.set(Calendar.DAY_OF_MONTH, 1)
    c.add(Calendar.MONTH, offset)
    return "${MONTH_NAMES[c.get(Calendar.MONTH)]} ${c.get(Calendar.YEAR)}"
}

fun monthShort(offset: Int): String {
    val c = Calendar.getInstance()
    c.set(Calendar.DAY_OF_MONTH, 1)
    c.add(Calendar.MONTH, offset)
    return MONTH_NAMES[c.get(Calendar.MONTH)].take(3)
}

fun greeting(): String {
    val h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when {
        h < 11 -> "Selamat pagi"
        h < 15 -> "Selamat siang"
        h < 19 -> "Selamat sore"
        else -> "Selamat malam"
    }
}

/** DatePicker returns UTC midnight; map it to the local day instead. */
fun localDateFromUtcPick(millis: Long): Long {
    val utc = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC")).apply { timeInMillis = millis }
    val now = Calendar.getInstance()
    return Calendar.getInstance().apply {
        set(utc.get(Calendar.YEAR), utc.get(Calendar.MONTH), utc.get(Calendar.DAY_OF_MONTH),
            now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}
