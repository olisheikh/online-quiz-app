package com.example.jettrivia.data

data class DataOrException<T, Boolean, e: Exception>(
    var data: T? = null,
    var loading: Boolean? = null,
    var e: Exception? = null
)