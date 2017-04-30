package io.github.initrc.bootstrap.presenter

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.initrc.bootstrap.adapter.FeedAdapter
import io.github.initrc.bootstrap.repo.PhotoRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import util.AnimationUtils
import util.snack

/**
 * Presenter for home view.
 */
class HomePresenter (_feedList: RecyclerView, gridColumnWidth: Int, _homeFab: View) : Presenter {
    val feedList = _feedList
    val homeFab = _homeFab
    val feedAdapter = FeedAdapter()
    val disposables = CompositeDisposable()
    var feature = Feature.popular
    var nextPage = 1

    companion object Feature {
        val popular = "popular"
        val editors = "editors"
    }

    init {
        feedAdapter.gridColumnWidth = gridColumnWidth
        feedList.adapter = feedAdapter
    }

    override fun onBind() {
        disposables.add(loadPhotos())
    }

    override fun onUnbind() {
        disposables.clear()
    }

    fun loadPhotos() : Disposable {
        return PhotoRepo.getPhotos(feature, nextPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { photoResponse ->
                        nextPage = photoResponse.current_page + 1
                        feedAdapter.addPhotos(photoResponse.photos)
                        if (photoResponse.current_page == 1) AnimationUtils.showFab(homeFab)
                    },
                    { error -> feedList.snack(error.message) }
                )
    }
}
