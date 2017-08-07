package fr.openium.template.injection

import android.content.Context
import com.github.salomonbrys.kodein.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.leakcanary.RefWatcher
import fr.openium.template.R
import okhttp3.Cache
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by t.coulange on 07/12/2016.
 */
object Modules {
    val configModule = Kodein.Module {
        bind<RefWatcher>() with singleton { RefWatcher.DISABLED }
    }

    val serviceModule = Kodein.Module {
    }

    val eventModule = Kodein.Module {
    }

    val restModule = Kodein.Module {
        bind<Cache>() with provider {
            val cacheSize = (20 * 1024 * 1024).toLong() // 20 MiB
            Cache(instance<Context>().cacheDir, cacheSize)
        }

        bind<HttpUrl>() with singleton {
            HttpUrl.parse(instance<Context>().getString(R.string.url_prod))!! // TODO
        }

        bind<OkHttpClient>() with provider {
            OkHttpClient.Builder().cache(instance()).build()
        }

        bind<Retrofit>() with singleton {
            Retrofit.Builder().baseUrl(instance<HttpUrl>()).client(instance()).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build()
        }

//        bind<Api>() with singleton {
//            instance<Retrofit>().create(Api::class.java)
//        }

        bind<OkHttpClient>("logged") with provider {
            OkHttpClient.Builder().cache(instance())
                    .addInterceptor(instance())
                    .build()
        }

        bind<Gson>() with singleton {
            GsonBuilder().setLenient().serializeSpecialFloatingPointValues().create()
        }

        bind<Retrofit>("logged") with singleton {
            Retrofit.Builder().baseUrl(instance<HttpUrl>()).client(instance("logged")).addConverterFactory(GsonConverterFactory.create(instance())).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build()
        }
//
//        bind<ApiLogged>() with singleton {
//            instance<Retrofit>("logged").create(ApiLogged::class.java)
//        }

//        bind<ApiHelper>() with singleton { ApiHelper(instance(), instance(), instance(), instance()) }
    }

//    val utilsModule = Kodein.Module {
//        bind<TokenTester>() with singleton { TokenTesterImpl() }
//    }


}
