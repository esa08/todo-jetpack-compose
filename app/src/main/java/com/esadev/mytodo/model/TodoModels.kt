package com.esadev.mytodo.model

import androidx.compose.ui.graphics.vector.ImageVector

data class TodoFolder(
    val id: String,
    val name: String,
    val icon: ImageVector,
    val count: Int = 0
)

data class TodoTask(
    val id: String,
    val title: String,
    val isCompleted: Boolean = false,
    val deadline: Long? = null,
    val note: String = ""
)
