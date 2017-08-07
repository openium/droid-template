package fr.openium.template.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPropertyAnimatorCompat
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.android.appKodein
import io.realm.Realm
import rx.subscriptions.CompositeSubscription

abstract class AbstractFragment : Fragment() {
    protected var oneTimeSubscriptions: CompositeSubscription = CompositeSubscription() //only subscribe one time and unsubscribe later
    protected var rebindSubscriptions: CompositeSubscription = CompositeSubscription() //Resubscribe in onstart

    protected var isAlive: Boolean = false

    open protected val customToolbarFragment: Toolbar? = null

    protected val injector = KodeinInjector()
    protected var realm: Realm? = null

    // =================================================================================================================
    // Life cycle
    // =================================================================================================================

    override fun onAttach(context: Context) {
        super.onAttach(context)
        isAlive = true
        injector.inject(appKodein())
        realm = Realm.getDefaultInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var layoutInflater = inflater

        val layoutId = layoutId
        val view: View = layoutInflater.inflate(layoutId, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        startSubscription(rebindSubscriptions)
    }

    override fun onStop() {
        super.onStop()
        rebindSubscriptions.clear()
    }

    protected open fun startSubscription(onStartSubscriptions: CompositeSubscription) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        oneTimeSubscriptions.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        oneTimeSubscriptions.clear()
    }

    override fun onDetach() {
        super.onDetach()
        isAlive = false
        realm?.close()
        realm = null
    }

    protected abstract val layoutId: Int

    inline fun ViewPropertyAnimatorCompat.withEndActionSafe(crossinline body: () -> Unit): ViewPropertyAnimatorCompat {
        return withEndAction {
            if (view != null) {
                body()
            }
        }
    }

    inline fun ViewPropertyAnimatorCompat.withStartActionSafe(crossinline body: () -> Unit): ViewPropertyAnimatorCompat {
        return withStartAction {
            if (view != null) {
                body()
            }
        }
    }
}
