package com.example.jettrivia.repository

import android.util.Log
import com.example.jettrivia.data.DataOrException
import com.example.jettrivia.model.QuestionItems
import com.example.jettrivia.model.Questions
import com.example.jettrivia.network.QuestionApi
import javax.inject.Inject

class QuestionRepo @Inject constructor(
    private val questionApi: QuestionApi
) {
    private val listOfQuestion = ArrayList<Questions>()
    /*
     * DataOrException instance will check the current stage of the api response
     */
    private val dataOrException = DataOrException<
            ArrayList<QuestionItems>,
            Boolean,
            Exception>()

    /*
     * Retrieving all questions from the network call
     */

    suspend fun getAllQuestionsRepo(): DataOrException<ArrayList<QuestionItems>, Boolean, Exception> {
        try {
            dataOrException.loading = true

            dataOrException.data = questionApi.getAllQuestions()
            if (dataOrException.data!!.isNotEmpty()) dataOrException.loading = false

        } catch(e: Exception) {
            Log.d("TAG", "Data or Exception: ${dataOrException.e}")
        }

        return dataOrException
    }
}