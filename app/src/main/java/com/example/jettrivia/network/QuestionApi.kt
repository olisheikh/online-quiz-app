package com.example.jettrivia.network

import com.example.jettrivia.model.Questions
import retrofit2.http.GET

interface QuestionApi {
    @GET("world.json")
    suspend fun getAllQuestions(): Questions
}