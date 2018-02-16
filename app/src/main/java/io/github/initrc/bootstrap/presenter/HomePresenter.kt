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
    private val feedList = _feedList
    private val homeFab = _homeFab
    private val feedAdapter = FeedAdapter()
    private val disposables = CompositeDisposable()
    val features = mutableListOf<String>("popular", "editors", "upcoming", "fresh_today")
    var currentFeature = features[0]
    var nextPage = 1

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

    fun refreshPhotos(feature: String) {
        if (currentFeature.equals(feature)) return
        feedAdapter.clear()
        nextPage = 1
        loadPhotos(feature)
    }

    fun loadPhotos(feature: String = currentFeature) : Disposable {
        val featureChange = !currentFeature.equals(feature)
        currentFeature = feature
        return PhotoRepo.getPhotos(feature, nextPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { photoResponse ->
                        nextPage = photoResponse.current_page + 1
                        feedAdapter.addPhotos(photoResponse.photos)
                        if (featureChange) feedList.smoothScrollToPosition(0)
                        if (photoResponse.current_page == 1) AnimationUtils.showFab(homeFab)
                    },
                    { error -> feedList.snack(error.message) }
                )
    }

    fun setGridColumnWidth(width: Int) {
        feedAdapter.gridColumnWidth = width
    }

    fun setImageOnly(imageOnly: Boolean) {
        feedAdapter.imageOnly = imageOnly
    }
}
