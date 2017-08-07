package fr.openium.template.dialog

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.android.appKodein
import rx.subscriptions.CompositeSubscription

/**
 * Created by t.coulange on 17/08/2016.
 */
abstract class AbstractDialog : AppCompatDialogFragment() {
    protected val onCreateSubscriptions: CompositeSubscription = CompositeSubscription()

    protected val kodeinInjector = KodeinInjector()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        kodeinInjector.inject(appKodein())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onCreateSubscriptions.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        onCreateSubscriptions.clear()
    }
}