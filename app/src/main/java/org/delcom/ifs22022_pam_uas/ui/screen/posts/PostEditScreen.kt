package org.delcom.ifs22022_pam_uas.ui.screen.posts

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.delcom.ifs22022_pam_uas.data.repository.DelcomRepository

@Composable
fun PostEditScreen(
    navController: NavController,
    repository: DelcomRepository,
    token: String,
    postId: String,
    initialCaption: String
) {
    var caption by remember { mutableStateOf(initialCaption) }
    var error by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Edit Post", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = caption,
            onValueChange = { caption = it },
            label = { Text("Caption") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch {
                    try {
                        val res = repository.updatePost(token, postId, caption)
                        if (res.isSuccessful) {
                            navController.popBackStack()
                        } else {
                            error = "Gagal update: ${res.message()}"
                        }
                    } catch (e: Exception) {
                        error = "Kesalahan: ${e.message}"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan Perubahan")
        }

        if (error.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(error, color = MaterialTheme.colorScheme.error)
        }
    }
}
