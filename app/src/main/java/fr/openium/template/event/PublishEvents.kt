package fr.openium.template.event

import rx.Observable
import rx.subjects.PublishSubject

/**
 * Created by t.coulange on 15/03/2017.
 */
abstract class PublishEvents<T> {
    private val subject: rx.subjects.PublishSubject<T> = rx.subjects.PublishSubject.create()
    val obs: rx.Observable<T>
        get() {
            return subject
        }

    fun set(t: T) {
        subject.onNext(t)
    }
}