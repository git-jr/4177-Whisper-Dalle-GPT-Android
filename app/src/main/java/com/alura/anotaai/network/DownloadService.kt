package com.alura.anotaai.network

import android.accounts.NetworkErrorException
import com.alura.anotaai.media.FileUtils
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.FileNotFoundException
import java.net.UnknownHostException
import javax.inject.Inject

class DownloadService @Inject constructor(
    private val fileUtils: FileUtils,
) {
    suspend fun makeDownloadByURL(
        url: String,
        onSaved: (String) -> Unit,
        onFailureDownload: () -> Unit = {},
    ) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        try {
            val response = withContext(IO) { client.newCall(request).execute() }
            val fileData = response.body?.byteStream()

            if (fileData != null) {
                withContext(Main) {
                    fileUtils.saveOnInternalStorage(
                        fileData,
                        onSuccess = { path -> onSaved(path) },
                        onFailure = { onFailureDownload() }
                    )
                }
            } else {
                onFailureDownload()
            }
        } catch (e: Exception) {
            when (e) {
                is NetworkErrorException,
                is UnknownHostException,
                is FileNotFoundException -> onFailureDownload()

                else -> throw e
            }
        }
    }
}