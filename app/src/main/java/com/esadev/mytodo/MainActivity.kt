package com.esadev.mytodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.esadev.mytodo.ui.HomeScreen
import com.esadev.mytodo.ui.TaskScreen
import com.esadev.mytodo.ui.theme.MytodoTheme

sealed class Screen {
    object Home : Screen()
    data class Tasks(val folderId: String, val folderName: String) : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MytodoTheme {
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

                when (val screen = currentScreen) {
                    is Screen.Home -> {
                        HomeScreen(
                            onFolderClick = { folder ->
                                currentScreen = Screen.Tasks(folder.id, folder.name)
                            }
                        )
                    }
                    is Screen.Tasks -> {
                        TaskScreen(
                            listName = screen.folderName,
                            onBack = { currentScreen = Screen.Home }
                        )
                    }
                }
            }
        }
    }
}
