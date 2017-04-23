package io.github.initrc.bootstrap.presenter

import android.support.v7.widget.RecyclerView
import io.github.initrc.bootstrap.adapter.FeedAdapter
import io.github.initrc.bootstrap.repo.PhotoRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import util.snack

/**
 * Presenter for home view.
 */
class HomePresenter (_feedList: RecyclerView) : Presenter {
    val feedList = _feedList
    val feedAdapter = FeedAdapter()
    val disposables = CompositeDisposable()

    init {
        feedList.adapter = feedAdapter
    }

    override fun onBind() {
        disposables.add(loadPhotos())
    }

    override fun onUnbind() {
        disposables.clear()
    }

    fun loadPhotos() : Disposable {
        return PhotoRepo.getPhotos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { photos -> feedAdapter.addPhotos(photos) },
                    { error -> feedList.snack(error.message) }
                )
    }
}
