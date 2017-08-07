package fr.openium.template.activity

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.android.appKodein
import fr.openium.template.R
import fr.openium.template.fragment.OnBackPressedListener
import io.realm.Realm
import kotlinx.android.synthetic.main.toolbar.*
import rx.subscriptions.CompositeSubscription
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

abstract class AbstractActivity : AppCompatActivity() {
    protected val subscriptions: CompositeSubscription = CompositeSubscription()
    protected var rebindSubscriptions: CompositeSubscription = CompositeSubscription() //Resubscribe in onstart
    protected open val handleFragmentBackPressed: Boolean = true
    protected val injector = KodeinInjector()
    protected var realm: Realm? = null

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(appKodein())
        realm = Realm.getDefaultInstance()
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        beforeSetContentView()
        setContentView(layoutId)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
        }

        setHomeAsUp(showHomeAsUp)
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

    protected fun setHomeAsUp(enabled: Boolean = true) {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(enabled)
            setHomeButtonEnabled(enabled)
        }
    }


    open protected fun beforeSetContentView() {

    }

    override fun onDestroy() {
        super.onDestroy()
        realm?.close()
        realm = null
        subscriptions.clear()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onArrowPressed()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    open fun onArrowPressed() {
        onBackPressed()
    }

    override fun onBackPressed() {
        if (handleFragmentBackPressed) {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.container_framelayout)
            if (currentFragment !is OnBackPressedListener || !currentFragment.onBackPressed()) {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    // Si true la fleche back est affichée
    protected open val showHomeAsUp: Boolean = false

    // Retourne le layout qui est associé à l'activité
    protected open val layoutId: Int = R.layout.container_linear
}
