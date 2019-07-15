package com.guilhermelucas.moviedatabase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.guilhermelucas.domain.Movie
import com.guilhermelucas.moviedatabase.home.HomeContract
import com.guilhermelucas.moviedatabase.home.HomePresenter
import com.guilhermelucas.moviedatabase.home.HomeRepository
import com.guilhermelucas.moviedatabase.home.adapter.item.AdapterItem
import com.guilhermelucas.moviedatabase.model.toMovieVO
import com.guilhermelucas.moviedatabase.util.MovieImageUrlBuilder
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.net.UnknownHostException

@RunWith(MockitoJUnitRunner::class)
class HomePresenterTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var homePresenter: HomePresenter

    @Mock
    private lateinit var repository: HomeRepository

    @Mock
    private lateinit var view: HomeContract.View

    @Mock
    private lateinit var movieImageUrlBuilder: MovieImageUrlBuilder

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        homePresenter = HomePresenter(
            repository,
            TrampolineSchedulerProvider()
        )

        Mockito.`when`(movieImageUrlBuilder.buildBackdropUrl(ArgumentMatchers.anyString())).thenReturn("testeurl")
        Mockito.`when`(movieImageUrlBuilder.buildPosterUrl(ArgumentMatchers.anyString())).thenReturn("testeurl")
    }

    @Test
    fun `should notify error when network is not available`() {

        Mockito.`when`(repository.loadMoreData())
            .thenReturn(Observable.create { it.onError(UnknownHostException()) })

        homePresenter.attachView(view)
        homePresenter.loadMoreItems()

        Mockito.verify(view).showError(HomeContract.Error.NETWORK)
    }

    @Test
    fun `should notify error when some request return a error`() {

        Mockito.`when`(repository.loadMoreData())
            .thenReturn(Observable.create { it.onError(Exception()) })

        homePresenter.attachView(view)
        homePresenter.loadMoreItems()

        Mockito.verify(view).showError(HomeContract.Error.REQUEST_GENERIC_ERROR)
    }

    @Test
    fun `should load first page when user close search view`() {

        Mockito.`when`(repository.loadMoreData(HomeRepository.RequestStrategy.FIRST_PAGE))
            .thenReturn(Observable.create { listOf(movie) })

        homePresenter.attachView(view)
        homePresenter.onCloseSearchBar()

        Mockito.verify(view).loading(true)
        Mockito.verify(repository).loadMoreData(HomeRepository.RequestStrategy.FIRST_PAGE)
    }

    @Test
    fun `should load first page when user swipe to refresh`() {

        Mockito.`when`(repository.loadMoreData(HomeRepository.RequestStrategy.FIRST_PAGE))
            .thenReturn(Observable.create { listOf(movie) })

        homePresenter.attachView(view)
        homePresenter.onSwipeToRefresh()

        Mockito.verify(view).loading(true)
        Mockito.verify(repository).loadMoreData(HomeRepository.RequestStrategy.FIRST_PAGE)

    }

    private val movie = Movie(
        1, "title", "Overview", arrayListOf(), 9.0, "posterpath", null, null
    )
}