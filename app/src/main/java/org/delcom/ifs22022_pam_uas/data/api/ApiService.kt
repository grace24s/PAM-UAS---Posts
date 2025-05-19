package org.delcom.ifs22022_pam_uas.data.api

import retrofit2.Response
import retrofit2.http.*
import org.delcom.ifs22022_pam_uas.data.model.*
import java.io.File

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @GET("posts")
    suspend fun getPosts(@Header("Authorization") token: String): Response<List<Post>>

    @Multipart
    @POST("posts")
    suspend fun createPost(
        @Header("Authorization") token: String,
        @Part("caption") caption: String,
        @Part image: File
    ): Response<Post>

    @PUT("posts/{id}")
    suspend fun updatePost(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body request: UpdatePostRequest
    ): Response<Post>

    @DELETE("posts/{id}")
    suspend fun deletePost(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Unit>
}
