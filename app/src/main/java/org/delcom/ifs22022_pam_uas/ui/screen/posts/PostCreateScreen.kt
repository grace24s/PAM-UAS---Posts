package org.delcom.ifs22022_pam_uas.ui.screen.posts

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import org.delcom.ifs22022_pam_uas.data.repository.DelcomRepository
import java.io.File
import java.io.InputStream

@Composable
fun PostCreateScreen(
    navController: NavController,
    repository: DelcomRepository,
    token: String
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var caption by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var error by remember { mutableStateOf("") }

    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri = uri
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Buat Post", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = caption,
            onValueChange = { caption = it },
            label = { Text("Caption") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { imagePicker.launch("image/*") }) {
            Text("Pilih Gambar")
        }

        imageUri?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (imageUri != null) {
                    scope.launch {
                        try {
                            val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri!!)
                            if (inputStream != null) {
                                val file = File.createTempFile("upload", ".jpg", context.cacheDir)
                                file.outputStream().use { output -> inputStream.copyTo(output) }

                                val res = repository.createPost(token, caption, file)
                                if (res.isSuccessful) {
                                    navController.popBackStack()
                                } else {
                                    error = "Gagal upload: ${res.message()}"
                                }
                            }
                        } catch (e: Exception) {
                            error = "Kesalahan: ${e.message}"
                        }
                    }
                } else {
                    error = "Gambar belum dipilih."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Upload Post")
        }

        if (error.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(error, color = MaterialTheme.colorScheme.error)
        }
    }
}
