package com.example.jettrivia.model

data class QuestionItems(
    val answer: String,
    val category: String,
    val choices: List<String>,
    val question: String
)