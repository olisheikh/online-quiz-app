package com.example.jettrivia.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jettrivia.data.DataOrException
import com.example.jettrivia.model.QuestionItems
import com.example.jettrivia.repository.QuestionRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    val questionRepo: QuestionRepo
): ViewModel(){
    /*
     * This data member will maintain the state of the data we are getting from repo
     */
    val dataFromRepo: MutableState<DataOrException<ArrayList<QuestionItems>, Boolean, Exception>>
    = mutableStateOf(DataOrException(null, false, Exception("")))

    // This will help to call the function and ready our state to use in our UI
    init {
        getAllQuestionViewModel()
    }

    /*
     * Getting data from QuestionRepo and set that to our state.
     */
    fun getAllQuestionViewModel() {
        viewModelScope.launch {
            dataFromRepo.value.loading = true

            dataFromRepo.value = questionRepo.getAllQuestionsRepo()

            if (dataFromRepo.value.data.toString().isNotEmpty()) {
                dataFromRepo.value.loading = false
            }
        }
    }

}