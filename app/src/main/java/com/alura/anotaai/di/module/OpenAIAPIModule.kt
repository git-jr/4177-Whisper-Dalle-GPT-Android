package com.alura.anotaai.di.module

import com.alura.anotaai.OpenAIAPI
import com.alura.anotaai.media.FileUtils
import com.alura.anotaai.network.DownloadService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class OpenAIAPIModule {
    @Singleton
    @Provides
    fun provideOpenAIAPI(fileUtils: FileUtils, downloadService: DownloadService): OpenAIAPI {
        return OpenAIAPI(fileUtils, downloadService)
    }
}

