package org.delcom.ifs22022_pam_uas.data.repository

import org.delcom.ifs22022_pam_uas.data.api.ApiService
import org.delcom.ifs22022_pam_uas.data.model.*
import java.io.File

class DelcomRepository(private val api: ApiService) {

    suspend fun login(email: String, password: String) =
        api.login(LoginRequest(email, password))

    suspend fun register(name: String, email: String, password: String) =
        api.register(RegisterRequest(name, email, password))

    suspend fun getPosts(token: String) = api.getPosts("Bearer $token")

    suspend fun createPost(token: String, caption: String, image: File) =
        api.createPost("Bearer $token", caption, image)

    suspend fun updatePost(token: String, id: String, caption: String) =
        api.updatePost("Bearer $token", id, UpdatePostRequest(caption))

    suspend fun deletePost(token: String, id: String) =
        api.deletePost("Bearer $token", id)
}
