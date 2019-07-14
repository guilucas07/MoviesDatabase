package com.guilhermelucas.moviedatabase.model

sealed class RequestFailure(val errorMessage: String) {
    class NetworkConnection : RequestFailure("Verify your internet connection and try again!")
    class ItemNotFound : RequestFailure("Item not found on database")
    class GenericFailure(errorMessage: String) : RequestFailure(errorMessage)
}