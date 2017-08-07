package fr.openium.template

import android.app.Application
import android.content.Context
import com.crashlytics.android.Crashlytics
import com.github.salomonbrys.kodein.*
import com.squareup.leakcanary.LeakCanary
import fr.openium.template.injection.Modules
import fr.openium.template.log.CrashReportingTree
import io.fabric.sdk.android.Fabric
import io.realm.Realm
import io.realm.RealmConfiguration
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

/**
 * Created by t.coulange on 05/07/2017.
 */

abstract class CustomApplication : Application(), KodeinAware {
    override val kodein by Kodein.lazy {
        bind<Context>() with singleton { applicationContext }
        import(Modules.configModule)
        import(Modules.eventModule)
//        import(Modules.preferenceModule)
        import(Modules.restModule)
        import(Modules.serviceModule)
    }

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }

        initializeCrashlytics()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }

        Realm.init(this)
        val builder = RealmConfiguration.Builder()
        val config = initRealm(builder).build()
        Realm.setDefaultConfiguration(config)
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath)
                .build())
    }


    open fun initRealm(builder: RealmConfiguration.Builder): RealmConfiguration.Builder {
        return builder.deleteRealmIfMigrationNeeded()
    }

    open fun initializeCrashlytics() {
        Fabric.with(applicationContext, Crashlytics())
    }
}
