package com.zyvo.uangku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.zyvo.uangku.data.Store
import com.zyvo.uangku.ui.App
import com.zyvo.uangku.ui.theme.Accent
import com.zyvo.uangku.ui.theme.AppStyle
import com.zyvo.uangku.ui.theme.UangKuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Store.init(applicationContext)
        enableEdgeToEdge()
        setContent {
            val mode by Store.themeMode.collectAsState()
            val themeId by Store.themeId.collectAsState()
            val accentId by Store.accentId.collectAsState()
            val dark = when (mode) {
                1 -> false
                2 -> true
                else -> isSystemInDarkTheme()
            }
            UangKuTheme(
                style = AppStyle.fromId(themeId),
                accent = Accent.fromId(accentId),
                darkTheme = dark
            ) {
                App()
            }
        }
    }
}
