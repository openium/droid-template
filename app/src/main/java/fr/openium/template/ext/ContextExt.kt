package fr.openium.template.ext

import android.content.Context
import android.net.ConnectivityManager
import android.provider.Settings
import android.util.TypedValue

/**
 * Created by t.coulange on 22/04/16.
 */
val Context.hasNetwork: Boolean
    get() {
        val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager;
        return cm.activeNetworkInfo?.isConnectedOrConnecting ?: false;
    }

val Context.secureId: String
    get() {
        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)
    }

fun Context.dip(value: Float, type: Int = TypedValue.COMPLEX_UNIT_DIP): Float {
    val metrics = getResources().getDisplayMetrics()
    val resultPix = TypedValue.applyDimension(type, value, metrics)
    return resultPix
}