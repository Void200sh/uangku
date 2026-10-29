package com.zyvo.uangku.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class CategoryStyle(val icon: ImageVector, val color: Color)

fun categoryStyle(name: String): CategoryStyle = when (name) {
    "Makanan" -> CategoryStyle(Icons.Filled.Restaurant, Color(0xFFE8871E))
    "Transport" -> CategoryStyle(Icons.Filled.DirectionsCar, Color(0xFF3B6CF6))
    "Belanja" -> CategoryStyle(Icons.Filled.ShoppingBag, Color(0xFF8B5CF6))
    "Tagihan" -> CategoryStyle(Icons.Filled.ReceiptLong, Color(0xFF0E9F8A))
    "Hiburan" -> CategoryStyle(Icons.Filled.Movie, Color(0xFFE0453A))
    "Kesehatan" -> CategoryStyle(Icons.Filled.HealthAndSafety, Color(0xFFE05A7A))
    "Pendidikan" -> CategoryStyle(Icons.Filled.School, Color(0xFF2563EB))
    "Gaji" -> CategoryStyle(Icons.Filled.Payments, Color(0xFF1B7A43))
    "Freelance" -> CategoryStyle(Icons.Filled.Work, Color(0xFF0E9F8A))
    "Bonus" -> CategoryStyle(Icons.Filled.CardGiftcard, Color(0xFFF5A623))
    "Investasi" -> CategoryStyle(Icons.Filled.TrendingUp, Color(0xFF7C3AED))
    else -> CategoryStyle(Icons.Filled.MoreHoriz, Color(0xFF6B7280))
}
