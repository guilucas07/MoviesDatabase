package com.guilhermelucas.moviedatabase.base

interface BasePresenter<T> {
    fun attachView(view: T)
    fun detachView()

}