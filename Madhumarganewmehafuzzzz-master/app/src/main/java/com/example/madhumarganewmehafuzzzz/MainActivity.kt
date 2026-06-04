package com.example.madhumarganewmehafuzzzz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.madhumarganewmehafuzzzz.ui.MadhuMargaNavigation
import com.example.madhumarganewmehafuzzzz.ui.theme.MadhuMargaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MadhuMargaTheme {
                MadhuMargaNavigation()
            }
        }
    }
}
