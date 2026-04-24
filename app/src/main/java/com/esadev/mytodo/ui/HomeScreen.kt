package com.esadev.mytodo.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.esadev.mytodo.model.TodoFolder
import com.esadev.mytodo.ui.theme.MytodoTheme
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onFolderClick: (TodoFolder) -> Unit
) {
    // Default list "Tugas"
    val defaultFolders = listOf(
        TodoFolder("tasks", "Tugas", Icons.Default.CheckCircle, 0)
    )

    // User created lists (Work, Education, etc)
    val userFolders = remember {
        mutableStateListOf(
            TodoFolder("work", "Work", Icons.Default.List, 0),
            TodoFolder("education", "Education", Icons.Default.List, 0)
        )
    }

    var showDialog by remember { mutableStateOf(false) }
    var newFolderName by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { 
                showDialog = false
                newFolderName = ""
            },
            title = { Text("Daftar baru") },
            text = {
                OutlinedTextField(
                    value = newFolderName,
                    onValueChange = { newFolderName = it },
                    label = { Text("Masukkan nama daftar") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newFolderName.isNotBlank()) {
                            userFolders.add(
                                TodoFolder(
                                    id = UUID.randomUUID().toString(),
                                    name = newFolderName,
                                    icon = Icons.Default.List,
                                    count = 0
                                )
                            )
                            showDialog = false
                            newFolderName = ""
                        }
                    }
                ) {
                    Text("Buat")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showDialog = false
                        newFolderName = ""
                    }
                ) {
                    Text("Batal")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("To Do") },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: Open drawer */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Daftar baru") }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Bagian "Tugas"
            items(defaultFolders) { folder ->
                FolderItem(folder, onClick = { onFolderClick(folder) })
            }

            // Divider pemisah antara list bawaan dan list buatan user
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }

            // List yang dibuat oleh user
            items(userFolders) { folder ->
                FolderItem(
                    folder = folder,
                    onClick = { onFolderClick(folder) },
                    onDelete = { userFolders.remove(folder) }
                )
            }
        }
    }
}

@Composable
fun FolderItem(
    folder: TodoFolder,
    onClick: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = folder.icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = folder.name,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )
        if (folder.count > 0) {
            Text(
                text = folder.count.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
        if (onDelete != null) {
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Hapus Daftar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MytodoTheme {
        HomeScreen(onFolderClick = {})
    }
}
