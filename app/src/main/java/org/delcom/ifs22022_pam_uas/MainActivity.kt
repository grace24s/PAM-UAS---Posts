package org.delcom.ifs22022_pam_uas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import org.delcom.ifs22022_pam_uas.data.api.ApiClient
import org.delcom.ifs22022_pam_uas.data.repository.DelcomRepository
import org.delcom.ifs22022_pam_uas.ui.nav.NavGraph
import org.delcom.ifs22022_pam_uas.ui.theme.PamUasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiService = ApiClient.apiService
        val repository = DelcomRepository(apiService)

        setContent {
            PamUasTheme {
                val navController = rememberNavController()
                var token by remember { mutableStateOf("") }

                NavGraph(
                    navController = navController,
                    repository = repository,
                    token = token,
                    onTokenChange = { token = it }
                )
            }
        }
    }
}

class PamUasTheme(function: () -> Unit) {

}
