package com.guilhermelucas.moviedatabase.detail.movie

import com.guilhermelucas.moviedatabase.model.MovieVO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DetailPresenter(
    private val itemId: Int,
    private val repository: DetailRepository,
    private val tracker: DetailTracker
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
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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