package fr.openium.template

import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.lazy
import com.squareup.leakcanary.LeakCanary
import fr.openium.template.injection.DebugModules
import io.fabric.sdk.android.Fabric
import io.realm.RealmConfiguration

/**
 * Created by t.coulange on 06/03/2017.
 */
class ApplicationImpl : CustomApplication() {
    override val kodein: Kodein by Kodein.lazy {
        extend(super.kodein)
        import(DebugModules.configModule, true)
        import(DebugModules.restModule, true)
        import(DebugModules.serviceModule, true)
    }

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(applicationContext)) {
            return
        }
    }

    override fun initRealm(builder: RealmConfiguration.Builder): RealmConfiguration.Builder {
        val newBuilder = super.initRealm(builder)
        return if (instance<Boolean>("mock")) newBuilder.inMemory() else newBuilder
    }

    override fun initializeCrashlytics() {
        val core = CrashlyticsCore.Builder().disabled(true).build()
        val crashlytics = Crashlytics.Builder().core(core).build()
        Fabric.with(this, crashlytics)
    }
}