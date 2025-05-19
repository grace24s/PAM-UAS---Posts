package org.delcom.ifs22022_pam_uas.ui.nav

import androidx.compose.runtime.*
import androidx.navigation.*
import androidx.navigation.compose.*
import org.delcom.ifs22022_pam_uas.data.repository.DelcomRepository
import org.delcom.ifs22022_pam_uas.ui.screen.auth.*
import org.delcom.ifs22022_pam_uas.ui.screen.posts.*

@Composable
fun AppNavGraph(
    repository: DelcomRepository,
    navController: NavHostController,
    token: String,
    onTokenChange: (String) -> Unit // <-- ubah jadi menerima token baru
) {
    // Hapus ini: val navController = rememberNavController()
    // Hapus ini: var token by remember { mutableStateOf<String?>(null) }

    NavHost(
        navController = navController,
        startDestination = if (token.isEmpty()) "login" else "posts"
    ) {
        composable("login") {
            LoginScreen(
                navController = navController,
                repository = repository,
                onLoginSuccess = { newToken ->
                    onTokenChange(newToken) // <- gunakan fungsi yang dikirim dari MainActivity
                    navController.navigate("posts") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            RegisterScreen(
                navController = navController,
                repository = repository
            )
        }

        composable("posts") {
            if (token.isNotEmpty()) {
                PostListScreen(
                    navController = navController,
                    repository = repository,
                    token = token
                )
            }
        }

        composable("create_post") {
            if (token.isNotEmpty()) {
                PostCreateScreen(
                    navController = navController,
                    repository = repository,
                    token = token
                )
            }
        }

        composable("edit_post/{postId}/{caption}",
            arguments = listOf(
                navArgument("postId") { type = NavType.StringType },
                navArgument("caption") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: ""
            val caption = backStackEntry.arguments?.getString("caption") ?: ""
            if (token.isNotEmpty()) {
                PostEditScreen(
                    navController = navController,
                    repository = repository,
                    token = token,
                    postId = postId,
                    initialCaption = caption
                )
            }
        }
    }
}

