package com.guilhermelucas.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.guilhermelucas.data.api.MovieDataSource
import com.guilhermelucas.data.api.MovieDataSourceSettings
import com.guilhermelucas.data.api.MovieRemoteRepository
import com.guilhermelucas.data.api.TmdbApi
import com.guilhermelucas.data.model.MovieDetailModel
import com.guilhermelucas.domain.Movie
import io.reactivex.Observable
import io.reactivex.internal.schedulers.TrampolineScheduler
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.*
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*
import org.mockito.Mockito.*

@RunWith(MockitoJUnitRunner::class)
class MovieRemoteRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var movieRemoteRepository: MovieRemoteRepository

    @Mock
    private lateinit var movieDataSource: MovieDataSource

    @Mock
    private lateinit var dataSourceSettings: MovieDataSourceSettings

    @Mock
    private lateinit var tmdbApi: TmdbApi

    private val dateMayTenTwoThousandNineteenTimestamp = 1557450060000

    private val movieDetailModel = MovieDetailModel(
        id = 1,
        title = "Alladin",
        overview = "Alladin",
        genres = arrayListOf(),
        voteAverage = 9.2,
        posterPath = null,
        backdropPath = null,
        releaseDate = Date(dateMayTenTwoThousandNineteenTimestamp)
    )

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(dataSourceSettings.getServerUrl()).thenReturn("http://remoteteste.com")
        Mockito.`when`(dataSourceSettings.getApiKey()).thenReturn("apikey")
        Mockito.`when`(dataSourceSettings.getLanguage()).thenReturn("us-US")
        movieRemoteRepository = MovieRemoteRepository(
            movieDataSource
        )
    }

    @Test
    fun `should convert to domain model when request valid movie`() {

        `when`(movieDataSource.tmdbApi).thenReturn(tmdbApi)
        `when`(movieDataSource.dataSourceSettings).thenReturn(dataSourceSettings)
        `when`(
            tmdbApi.movie(
                movieDetailModel.id.toLong(),
                dataSourceSettings.getApiKey(),
                dataSourceSettings.getLanguage()
            )
        ).thenReturn(Observable.create { movieDetailModel })

        val observer = TestObserver<Movie>()
        val observable = movieRemoteRepository.getMovie(movieDetailModel.id.toLong())

        observable.subscribe(observer)
        observer.awaitTerminalEvent()
        observer.assertComplete()
        observer.assertValue { it.title == movieDetailModel.title }
    }
}