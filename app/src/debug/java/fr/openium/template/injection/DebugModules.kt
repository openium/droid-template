package fr.openium.template.injection

import android.content.Context
import com.github.salomonbrys.kodein.*
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import fr.openium.template.R
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by t.coulange on 07/12/2016.
 */
object DebugModules {
    val mock = false
    val prod = true
    val configModule = Kodein.Module {
        bind<RefWatcher>(overrides = true) with singleton { LeakCanary.install(instance()) }
        constant("mock") with mock
    }
    val serviceModule = Kodein.Module {
    }
    val restModule = Kodein.Module {
        bind<OkHttpClient>(overrides = true) with provider {
            OkHttpClient.Builder().cache(instance()).addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) }).build()
        }
        bind<OkHttpClient>("logged", overrides = true) with provider {
            OkHttpClient.Builder().cache(instance()).addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
                    .addInterceptor(instance())
                    .build()
        }

        if (!prod) {
            bind<HttpUrl>(overrides = true) with provider {
                HttpUrl.parse(instance<Context>().getString(R.string.url_debug))!!
            }
        }

        bind<Retrofit>(overrides = true) with provider {
            Retrofit.Builder().baseUrl(instance<HttpUrl>()).client(instance()).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build()
        }

//        bind<Api>(overrides = true) with provider {
//            if (mock) {
//                val networkBehaviour = NetworkBehavior.create()
//                networkBehaviour.setDelay(250, TimeUnit.MILLISECONDS)
//                networkBehaviour.setFailurePercent(0)
//                networkBehaviour.setVariancePercent(0)
//                val apiMock = object : MockApi() {
//                    override fun login(login: PostLogin): Single<ResponseLogin> {
//                        return delegate.returningResponse(ResponseLogin("eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2p3dC1pZHAuZXhhbXBsZS5jb20iLCJzdWIiOiJtYWlsdG86bWlrZUBleGFtcGxlLmNvbSIsIm5iZiI6MTQ4OTM5OTU1OSwiZXhwIjoxNTIwOTM1NTU5LCJpYXQiOjE0ODkzOTk1NTksImp0aSI6ImlkMTIzNDU2IiwidHlwIjoiaHR0cHM6Ly9leGFtcGxlLmNvbS9yZWdpc3RlciJ9.kgr1QvEIMjjVvA1UbNwtkAerRLS1G3YH4WVCIa6qQwi0CiyICiG8DxNruuW7jZ2DvidFT5mAps9kODGnKEaThw"
//                                , ApplicationData(listOf(PanelInfo("panel1", "Panel 1"), PanelInfo("panel2", "Panel 2")),
//                                listOf(VehicleInfo("9999", "Audi", "A5"), VehicleInfo("777", "Peugeot", "404"))))).login(login)
//                    }
//                }
//                ddiMock.delegate = MockRetrofit.Builder(Retrofit.Builder().baseUrl(instance<HttpUrl>()).addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                        .addConverterFactory(GsonConverterFactory.create()).build()).networkBehavior(networkBehaviour).build().create(DDIApi::class.java)
//                ddiMock
//            } else {
//                instance<Retrofit>().create(Api::class.java)
//            }
//        }

        bind<Retrofit>("logged", overrides = true) with provider {
            Retrofit.Builder().baseUrl(instance<HttpUrl>()).client(instance("logged")).addConverterFactory(GsonConverterFactory.create(instance())).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build()
        }
//
//        bind<ApiLogged>(overrides = true) with provider {
//            if (mock) {
//                val context = instance<Context>()
//                val networkBehaviour = NetworkBehavior.create()
//                networkBehaviour.setDelay(250, TimeUnit.MILLISECONDS)
//                networkBehaviour.setFailurePercent(0)
//                networkBehaviour.setVariancePercent(0)
//                val apiMock = object : MockApiLogged() {
//
//                }
//                apiMock.delegate = MockRetrofit.Builder(Retrofit.Builder().baseUrl(instance<HttpUrl>()).addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                        .addConverterFactory(GsonConverterFactory.create()).build()).networkBehavior(networkBehaviour).build().create(ApiLogged::class.java)
//                apiMock
//            } else {
//                instance<Retrofit>("logged").create(ApiLogged::class.java)
//            }
//        }


    }
}