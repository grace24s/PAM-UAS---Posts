package org.delcom.ifs22022_pam_uas.ui.nav

import androidx.compose.runtime.*
import androidx.navigation.*
import androidx.navigation.compose.*
import org.delcom.ifs22022_pam_uas.data.repository.DelcomRepository
import org.delcom.ifs22022_pam_uas.ui.screen.auth.*
import org.delcom.ifs22022_pam_uas.ui.screen.posts.*

@Composable
fun AppNavGraph(
    repository: DelcomRepository
) {
    val navController = rememberNavController()
    var token by remember { mutableStateOf<String?>(null) }

    NavHost(
        navController = navController,
        startDestination = if (token == null) "login" else "posts"
    ) {
        composable("login") {
            LoginScreen(
                navController = navController,
                repository = repository,
                onLoginSuccess = {
                    token = it
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
            token?.let {
                PostListScreen(
                    navController = navController,
                    repository = repository,
                    token = it
                )
            }
        }

        composable("create_post") {
            token?.let {
                PostCreateScreen(
                    navController = navController,
                    repository = repository,
                    token = it
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
            token?.let {
                PostEditScreen(
                    navController = navController,
                    repository = repository,
                    token = it,
                    postId = postId,
                    initialCaption = caption
                )
            }
        }
    }
}
