package com.guilhermelucas.moviedatabase.detail.movie

import com.guilhermelucas.data.BaseSchedulerProvider
import com.guilhermelucas.moviedatabase.model.MovieVO
import com.guilhermelucas.moviedatabase.util.SchedulerProvider
import io.reactivex.disposables.Disposable

class DetailPresenter(
    private val itemId: Int,
    private val repository: DetailRepository,
    private val tracker: DetailTracker,
    private val schedulers: BaseSchedulerProvider = SchedulerProvider()
) : DetailContract.Presenter {

    private var view: DetailContract.View? = null
    private var disposable: Disposable? = null

    /******************************************/
    /**   DetailPromotionAdContract.Presenter methods   **/
    /******************************************/
    override fun attachView(view: DetailContract.View) {
        this.view = view
        tracker.logScreenOpen(itemId)
    }

    override fun detachView() {
        view = null
    }

    override fun onResume() {
        disposable = repository.loadMovie(itemId.toLong())
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe { movieVO ->
                view?.loadMovieDetail(movieVO)
                disposable?.dispose()
            }
    }

    override fun seeMovieBackdrop(movie: MovieVO) {
        tracker.logClickSeeBackdrop(itemId)
        if (movie.backdropUrl != null)
            view?.goToBackdropViewer(movie.backdropUrl)
        else
            view?.showError(DetailContract.Errors.EMPTY_BACKDROP)
    }

}