package com.example.jettrivia.di

import androidx.compose.ui.unit.Constraints
import com.example.jettrivia.network.QuestionApi
import com.example.jettrivia.repository.QuestionRepo
import com.example.jettrivia.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiModule {

    /*
     * Provides a retrofit instance with a BASE_URL contained in Constants
     * This instant is used to create API interfaces and implement them
     *
     * @Provides ensure that it becomes a dependencies for DI.
     * @Singleton make sure this instance is created only once throughout the-
     * app
     */
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    /*
     * Provide an implementation for QuestionApi for making an network request
     * Retrofit's create method generate the implementation of the endpoints
     *
     * @Provide creating a QuestionApi instance for DI
     * @Singleton ensures the QuestionApi is created a single instance throughout the app
     */
    @Provides
    @Singleton
    fun provideQuestionApi(retrofit: Retrofit): QuestionApi {
        return retrofit.create(QuestionApi::class.java)
    }

    /*
     * Provide repository dependency to inject inside the viewmodel
     */
    @Provides
    @Singleton
    fun provideRepository(questionApi: QuestionApi) = QuestionRepo(questionApi)
}