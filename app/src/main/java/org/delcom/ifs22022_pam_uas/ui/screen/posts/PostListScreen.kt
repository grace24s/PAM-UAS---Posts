package org.delcom.ifs22022_pam_uas.ui.screen.posts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import org.delcom.ifs22022_pam_uas.data.model.Post
import org.delcom.ifs22022_pam_uas.data.repository.DelcomRepository

@Composable
fun PostListScreen(
    navController: NavController,
    repository: DelcomRepository,
    token: String
) {
    val scope = rememberCoroutineScope()
    var posts by remember { mutableStateOf<List<Post>>(emptyList()) }
    var error by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val response = repository.getPosts(token)
            if (response.isSuccessful) {
                posts = response.body() ?: emptyList()
            } else {
                error = "Gagal mengambil data: ${response.message()}"
            }
        } catch (e: Exception) {
            error = "Kesalahan: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("create_post") }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .padding(16.dp)) {

            Text("Post Image", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else if (error.isNotEmpty()) {
                Text(error, color = MaterialTheme.colorScheme.error)
            } else {
                LazyColumn {
                    items(posts) { post ->
                        PostCard(
                            post = post,
                            onEdit = {
                                navController.navigate("edit_post/${post.id}/${post.caption}")
                            },
                            onDelete = {
                                scope.launch {
                                    val res = repository.deletePost(token, post.id.toString())
                                    if (res.isSuccessful) {
                                        posts = posts.filterNot { it.id == post.id }
                                    }
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun PostCard(post: Post, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Caption: ${post.caption}")
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = rememberAsyncImagePainter(post.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Button(onClick = onEdit) {
                    Text("Edit")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onDelete, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                    Text("Hapus")
                }
            }
        }
    }
}
