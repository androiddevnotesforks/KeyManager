package dev.yash.keymanager.api

import dev.yash.keymanager.models.GpgKey
import dev.yash.keymanager.models.SshKey
import dev.yash.keymanager.models.SshModel
import retrofit2.http.*

interface GitHubService {
    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/user/keys")
    suspend fun getSshKeys(
        @Header("Authorization") token: String
    ): List<SshKey>

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/user/gpg_keys")
    suspend fun getGpgKeys(
        @Header("Authorization") token: String
    ): List<GpgKey>

    @Headers("Accept: application/vnd.github.v3+json")
    @POST("/user/keys")
    suspend fun postSshKey(
        @Header("Authorization") token: String,
        @Body key: SshModel
    )

    companion object {
        const val BASE_URL = "https://api.github.com"
    }
}
